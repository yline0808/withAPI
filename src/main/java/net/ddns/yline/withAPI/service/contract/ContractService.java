package net.ddns.yline.withAPI.service.contract;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.dto.contract.ContractDto;
import net.ddns.yline.withAPI.dto.contract.ContractSearchCondition;
import net.ddns.yline.withAPI.repository.contract.ContractRepository;
import net.ddns.yline.withAPI.repository.contract.ContractRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContractService {
    private final ContractRepository contractRepository;

    public Contract findById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
    }

    public Page<ContractDto> findByTitleOrContent(
            ContractSearchCondition condition, Pageable pageable) {
        return contractRepository.searchContractComplex(condition, pageable);
    }

    @Transactional
    public Long save(Contract contract) {
        contractRepository.save(contract);
        return contract.getId();
    }
}
