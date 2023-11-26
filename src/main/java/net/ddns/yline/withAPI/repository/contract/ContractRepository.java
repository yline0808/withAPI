package net.ddns.yline.withAPI.repository.contract;

import net.ddns.yline.withAPI.domain.contract.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {

}
