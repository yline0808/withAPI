package net.ddns.yline.withAPI.repository.contract;

import net.ddns.yline.withAPI.domain.contract.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    Optional<Page<Contract>> findByTitle(String title, Pageable pageable);
}
