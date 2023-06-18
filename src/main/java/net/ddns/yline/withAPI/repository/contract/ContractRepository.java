package net.ddns.yline.withAPI.repository.contract;

import net.ddns.yline.withAPI.domain.contract.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
