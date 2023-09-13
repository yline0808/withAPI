package net.ddns.yline.withAPI.repository.authInfo;

import net.ddns.yline.withAPI.domain.authInfo.AuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {
    Optional<AuthInfo> findFirstByEmailOrderByAuthTimeDesc(String email);
    Optional<AuthInfo> findFirstByEmailAndAuthTimeBetweenOrderByAuthTimeDesc(String email, LocalDateTime startDate, LocalDateTime endDate);
//    @Query(value = """
//            SELECT a
//            FROM AuthInfo a
//            WHERE a.email = :email AND TO_CHAR(a.authTime, 'YYYYMMDD') = TO_CHAR(SYSDATE, 'YYYYMMDD')
//            ORDER BY a.authTime DESC FETCH FIRST 1 ROWS ONLY
//            """)
//    Optional<AuthInfo> findAuthInfoForSignUp(@Param("email") String email);
}