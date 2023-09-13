package net.ddns.yline.withAPI.controller.contract;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.ddns.yline.withAPI.controller.SuccessResult;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.domain.contractmap.ContractMap;
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
        Contract contract = new Contract();
        contract.setTitle(request.getTitle());
        contract.setContent(request.getContent());

        contractService.save(contract);

        String email = principal.getName();
        Account findAccount = accountService.findByEmail(email);

        ContractMap contractMap = new ContractMap();
        contractMap.setContract(contract);
        contractMap.setAccount(findAccount);

        contractMapService.save(contractMap);

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
