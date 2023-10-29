package net.ddns.yline.withAPI.service.contractMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.domain.contractMap.ContractMap;
import net.ddns.yline.withAPI.domain.contractMap.Opinion;
import net.ddns.yline.withAPI.execption.exType.UserException;
import net.ddns.yline.withAPI.repository.contractMap.ContractMapRepository;
import net.ddns.yline.withAPI.service.account.AccountService;
import net.ddns.yline.withAPI.service.contract.ContractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContractMapService {
    private final ContractMapRepository contractMapRepository;
    private final ContractService contractService;
    private final AccountService accountService;

    @Transactional
    public Long save(ContractMap contractMap) {
        contractMapRepository.save(contractMap);
        return contractMap.getId();
    }

    @Transactional
    public Long saveAccountMapping(Contract contract, List<String> receivers) {
        //계약서 인원 체크 및 불러오기
        List<Account> findAccountList = checkAndReturnAccount(receivers);

        //계약서 저장
        Long savedContractId = contractService.save(contract);

        //===account에 contract 추가(연관관계 메서드)===
        List<ContractMap> contractMapList = new ArrayList<>();

        findAccountList.forEach(account -> {
            var contractMap = ContractMap.builder()
                    .opinion(Opinion.BEFORE)
                    .build();
            contractMap.setAccount(account);
            contractMap.setContract(contract);

            contractMapList.add(contractMap);
        });

        //계약서 매핑
        contractMapRepository.saveAll(contractMapList);

        return savedContractId;
    }
    private List<Account> checkAndReturnAccount(List<String> receivers) {
        List<Account> findAccountList = accountService.findByEmailList(receivers);

        if (receivers.size() != findAccountList.size()) {
            List<String> invalidEmailList = receivers.stream().filter(email ->
                            !findAccountList.stream().map(Account::getEmail).toList().contains(email)
            ).toList();
            throw new UserException("계정이 없습니다.", invalidEmailList);
        }

        return findAccountList;
    }
}
