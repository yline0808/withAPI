package net.ddns.yline.withAPI.service.contract;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.domain.contractMap.Opinion;
import net.ddns.yline.withAPI.repository.contract.ContractDto;
import net.ddns.yline.withAPI.repository.contract.ContractQueryRepository;
import net.ddns.yline.withAPI.repository.contract.ContractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContractService {
    private final ContractRepository contractRepository;
    private final ContractQueryRepository contractQueryRepository;

    public Contract findById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
    }

    public Page<ContractDto> findByTitleOrContent(
            String searchType, String searchKeyword,
            Pageable pageable, Principal principal) {
        Page<ContractDto> findContractDto = null;
        findContractDto = contractQueryRepository.findByTitle(searchKeyword, pageable, principal.getName());
//        if (searchType.isEmpty()) {
//            contractQueryRepository.findByEmail(principal.getName());
//        }else{
//            if (searchType.equals("title")) {
//                findContractDto = contractQueryRepository.findByTitle(searchKeyword, pageable, principal.getName());
//            } else if (searchType.equals("content")) {
//                contractQueryRepository.findByContent(searchKeyword, pageable, principal.getName());
//            } else {
//                throw new IllegalArgumentException("검색 타입이 잘못됐습니다.");
//            }
//        }

        return findContractDto;
    }

    @Transactional
    public Long save(Contract contract) {
        contractRepository.save(contract);
        return contract.getId();
    }
}
