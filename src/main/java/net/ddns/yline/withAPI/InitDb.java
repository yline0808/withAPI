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
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.domain.contract.ContractStatus;
import net.ddns.yline.withAPI.domain.contractMap.ContractMap;
import net.ddns.yline.withAPI.domain.contractMap.Opinion;
import net.ddns.yline.withAPI.domain.token.Token;
import net.ddns.yline.withAPI.domain.token.TokenType;
import net.ddns.yline.withAPI.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            var authInfo3 = AuthInfo.builder()
                    .email("wake45@naver.com")
                    .code("000000")
                    .validType(ValidType.VALID)
                    .authTime(LocalDateTime.now().plusMinutes(3))
                    .build();
            em.persist(authInfo3);

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

            var account3 = Account.builder()
                    .birthDate("19950808")
                    .email(authInfo3.getEmail())
                    .name("lsu")
                    .phone("01000000000")
                    .address(new Address("main", "detail", "9999"))
                    .password(passwordEncoder.encode("dltkddnr1995"))
                    .role(Role.USER)
                    .accountStatus(AccountStatus.NORMAL)
                    .failCnt(0)
                    .build();
            em.persist(account3);

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

            var token3 = Token.builder()
                    .account(account3)
                    .token(jwtService.generateToken(account3))
                    .tokenType(TokenType.BEARER)
                    .expired(false)
                    .revoked(false)
                    .build();
            em.persist(token3);

            List<Contract> contractList = new ArrayList<>();

            for(int i = 1; i <= 12; i++){
                var contract = Contract.builder()
                        .title("["+i+"]title")
                        .content("["+i+"]content")
                        .contractStatus(ContractStatus.INVALID)
                        .build();
                em.persist(contract);
                contractList.add(contract);
            }

            for(int i = 1; i <= 12; i++){
                if( i % 3 == 1){
                    var contractMapA = ContractMap.builder()
                            .contract(contractList.get(i-1))
                            .account(account1)
                            .opinion(Opinion.BEFORE)
                            .build();
                    em.persist(contractMapA);
                    var contractMapB = ContractMap.builder()
                            .contract(contractList.get(i-1))
                            .account(account2)
                            .opinion(Opinion.BEFORE)
                            .build();
                    em.persist(contractMapB);
                }else if( i % 3 == 2 ){
                    var contractMapA = ContractMap.builder()
                            .contract(contractList.get(i-1))
                            .account(account1)
                            .opinion(Opinion.BEFORE)
                            .build();
                    em.persist(contractMapA);
                    var contractMapB = ContractMap.builder()
                            .contract(contractList.get(i-1))
                            .account(account3)
                            .opinion(Opinion.BEFORE)
                            .build();
                    em.persist(contractMapB);
                }else {
                    var contractMapA = ContractMap.builder()
                            .contract(contractList.get(i-1))
                            .account(account2)
                            .opinion(Opinion.BEFORE)
                            .build();
                    em.persist(contractMapA);
                    var contractMapB = ContractMap.builder()
                            .contract(contractList.get(i-1))
                            .account(account3)
                            .opinion(Opinion.BEFORE)
                            .build();
                    em.persist(contractMapB);
                }
            }

        }
    }
}
