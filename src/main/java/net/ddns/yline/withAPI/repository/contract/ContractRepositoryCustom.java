package net.ddns.yline.withAPI.repository.contract;

import net.ddns.yline.withAPI.dto.contract.ContractDto;
import net.ddns.yline.withAPI.dto.contract.ContractSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractRepositoryCustom {
    Page<ContractDto> searchContractComplex(ContractSearchCondition condition, Pageable pageable);
}
