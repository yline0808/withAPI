package net.ddns.yline.withAPI.repository.authInfo;

import net.ddns.yline.withAPI.domain.authInfo.AuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {
    Optional<AuthInfo> findFirstByEmailOrderByAuthTimeDesc(String email);
//    Optional<AuthInfo> findFirstByEmailAndAuthTimeBetweenOrderByAuthTimeDesc(String email, LocalDateTime startDate, LocalDateTime endDate);
//    @Query(value = "SELECT a " +
//            "FROM AuthInfo a " +
//            "WHERE e.email = :email AND TO_CHAR(e.authTime, 'YYYYMMDD') = TO_CHAR(SYSDATE, 'YYYYMMDD')" +
//            "ORDER BY e.authTime DESC FETCH FIRST 1 ROWS ONLY"
//            )
//    Optional<AuthInfo> findAuthInfoForSignUp(@Param("email") String email);
}