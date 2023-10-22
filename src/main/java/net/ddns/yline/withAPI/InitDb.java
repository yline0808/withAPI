package net.ddns.yline.withAPI;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.account.AccountStatus;
import net.ddns.yline.withAPI.domain.account.Role;
import net.ddns.yline.withAPI.domain.address.Address;
import net.ddns.yline.withAPI.domain.authInfo.AuthInfo;
import net.ddns.yline.withAPI.domain.authInfo.ValidType;
import net.ddns.yline.withAPI.domain.token.Token;
import net.ddns.yline.withAPI.domain.token.TokenType;
import net.ddns.yline.withAPI.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;

        public void dbInit1(){
            var authInfo1 = AuthInfo.builder()
                    .email("uxtommy@naver.com")
                    .code("000000")
                    .validType(ValidType.VALID)
                    .authTime(LocalDateTime.now().plusMinutes(3))
                    .build();
            em.persist(authInfo1);

            var authInfo2 = AuthInfo.builder()
                    .email("yline0088@gmail.com")
                    .code("000000")
                    .validType(ValidType.VALID)
                    .authTime(LocalDateTime.now().plusMinutes(3))
                    .build();
            em.persist(authInfo2);

            var account1 = Account.builder()
                    .birthDate("19950808")
                    .email(authInfo1.getEmail())
                    .name("rys")
                    .phone("01000000000")
                    .address(new Address("main", "detail", "9999"))
                    .password(passwordEncoder.encode("950808aA!"))
                    .role(Role.USER)
                    .accountStatus(AccountStatus.NORMAL)
                    .failCnt(0)
                    .build();
            em.persist(account1);

            var account2 = Account.builder()
                    .birthDate("19950808")
                    .email(authInfo2.getEmail())
                    .name("rys")
                    .phone("01000000000")
                    .address(new Address("main", "detail", "9999"))
                    .password(passwordEncoder.encode("950808aA!"))
                    .role(Role.USER)
                    .accountStatus(AccountStatus.NORMAL)
                    .failCnt(0)
                    .build();
            em.persist(account2);

            var token1 = Token.builder()
                    .account(account1)
                    .token(jwtService.generateToken(account1))
                    .tokenType(TokenType.BEARER)
                    .expired(false)
                    .revoked(false)
                    .build();
            em.persist(token1);

            var token2 = Token.builder()
                    .account(account2)
                    .token(jwtService.generateToken(account2))
                    .tokenType(TokenType.BEARER)
                    .expired(false)
                    .revoked(false)
                    .build();
            em.persist(token2);
        }
    }
}
