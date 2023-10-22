package net.ddns.yline.withAPI.repository.contractFile;

import net.ddns.yline.withAPI.domain.contractFile.ContractFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractFileRepository extends JpaRepository<ContractFile, Long> {
}
