package net.ddns.yline.withAPI.service;

import lombok.*;
import net.ddns.yline.withAPI.controller.auth.AuthenticationRequest;
import net.ddns.yline.withAPI.controller.auth.AuthenticationResponse;
import net.ddns.yline.withAPI.controller.auth.RegisterRequest;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.account.AccountStatus;
import net.ddns.yline.withAPI.domain.account.Role;
import net.ddns.yline.withAPI.domain.token.Token;
import net.ddns.yline.withAPI.domain.token.TokenType;
import net.ddns.yline.withAPI.repository.AccountRepository;
import net.ddns.yline.withAPI.repository.TokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //사용자 생성 & 데이터베이스 저장 & 토큰생성반환
    public AuthenticationResponse register(RegisterRequest request) {
        var account = Account.builder()
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .accountStatus(AccountStatus.NORMAL)
                .failCnt(0)
                .build();
        var savedAccount = accountRepository.save(account);
        var jwtToken = jwtService.generateToken(account);
        saveAccountToken(savedAccount, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //사용자 계정 비번 체크
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(account);
        revokeAllAccountToken(account);
        saveAccountToken(account, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void revokeAllAccountToken(Account account) {
        var validAccountToken = tokenRepository.findAllValidTokenByAccount(account.getId());
        if (validAccountToken.isEmpty()) {
            return;
        }
        validAccountToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validAccountToken);
    }

    private void saveAccountToken(Account account, String jwtToken) {
        var token = Token.builder()
                .account(account)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
