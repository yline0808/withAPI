package net.ddns.yline.withAPI.controller.contract;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.ddns.yline.withAPI.controller.SuccessResult;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.domain.contract.ContractStatus;
import net.ddns.yline.withAPI.domain.contractMap.ContractMap;
import net.ddns.yline.withAPI.domain.contractMap.Opinion;
import net.ddns.yline.withAPI.service.account.AccountService;
import net.ddns.yline.withAPI.service.contract.ContractService;
import net.ddns.yline.withAPI.service.contractMap.ContractMapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contract")
public class ContractController {

    private final ContractService contractService;
    private final AccountService accountService;
    private final ContractMapService contractMapService;

    @PostMapping
    public ResponseEntity<SuccessResult> saveContract(@RequestBody @Valid CreateContractRequest request, Principal principal) {
        //===계약서 생성===
        Contract contract = new Contract();
        contract.setTitle(request.getTitle());
        contract.setContent(request.getContent());
        contract.setContractStatus(ContractStatus.INVALID);

        //===계약서 저장===
        contractService.save(contract);

        //===계약서 관련된 계정 검색===
        List<String> emails = new ArrayList<>();
        emails.add(principal.getName());
        emails.addAll(request.getReceivers());

        List<Account> findAccountList = accountService.findByEmailList(emails);

        //===이메일 개수와 찾은 account개수 비교===
        List<String> invalidEmailList;
        List<String> findAccountEmailList = findAccountList.stream()
                .map(Account::getEmail)
                .toList();

        if (emails.size() != findAccountList.size()) {
            invalidEmailList = emails.stream().filter(email -> !findAccountEmailList.contains(email)).toList();
            throw new IllegalArgumentException(invalidEmailList + "에 해당하는 계정이 없습니다.");
        }

        //===account에 contract 추가(연관관계 메서드)===
        List<ContractMap> contractMapList = new ArrayList<>();

        findAccountList.forEach(account -> {
            ContractMap contractMap = new ContractMap();
            contractMap.setContract(contract);
            contractMap.setAccount(account);
            contractMap.setOpinion(Opinion.BEFORE);
            contractMapList.add(contractMap);
        });

        //===contractMap save!!===
        contractMapService.saveAll(contractMapList);

        return ResponseEntity.ok(new SuccessResult("Success save contract","계약서 저장 성공"));
    }

    @Data
    static class CreateContractResponse{
        private Long id;

        public CreateContractResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class CreateContractRequest{
        private String title;
        private String content;
        private List<String> receivers = new ArrayList<>();
    }
}
