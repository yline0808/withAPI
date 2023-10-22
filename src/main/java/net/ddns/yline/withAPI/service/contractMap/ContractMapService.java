package net.ddns.yline.withAPI.service.contractMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.contractMap.ContractMap;
import net.ddns.yline.withAPI.repository.contractMap.ContractMapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContractMapService {
    private final ContractMapRepository contractMapRepository;

    @Transactional
    public Long save(ContractMap contractMap) {
        contractMapRepository.save(contractMap);
        return contractMap.getId();
    }

    @Transactional
    public void saveAll(List<ContractMap> contractMaps) {
        contractMapRepository.saveAll(contractMaps);
    }
}
