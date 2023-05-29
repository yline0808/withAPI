package net.ddns.yline.withAPI.repository;

import net.ddns.yline.withAPI.domain.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = """
            select t from Token t inner join Account a\s
            on t.account.id = a.id\s
            where a.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByAccount(@Param("id") Long id);

    Optional<Token> findByToken(String token);
}
