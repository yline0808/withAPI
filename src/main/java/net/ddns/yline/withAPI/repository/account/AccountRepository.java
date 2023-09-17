package net.ddns.yline.withAPI.repository.account;

import net.ddns.yline.withAPI.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<List<Account>> findByEmailIn(List<String> emails);
    Optional<Long> countByEmail(String email);
}
