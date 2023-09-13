package net.ddns.yline.withAPI.service.contract;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.repository.contract.ContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContractService {
    private final ContractRepository contractRepository;

    @Transactional
    public Long save(Contract contract) {
        contractRepository.save(contract);
        return contract.getId();
    }
}
