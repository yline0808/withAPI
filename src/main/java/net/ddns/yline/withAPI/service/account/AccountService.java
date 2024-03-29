package net.ddns.yline.withAPI.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.repository.account.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean checkEmail(String email) {
        Long emailCnt = accountRepository.countByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자 없음"));
        return emailCnt > 0;
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일 없음"));
    }

    public List<Account> findByEmailList(List<String> emails) {
        return accountRepository.findByEmailIn(emails)
                .orElseThrow(() -> new IllegalArgumentException("이메일 list 검색 오류"));
    }

    @Transactional
    public void updatePassword(String email, String password) {
        String encryptPassword = passwordEncoder.encode(password);
        log.info("[새로운비번:암호화후] => ",encryptPassword);

        Account account = accountRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        account.updatePassword(encryptPassword);
        log.info("비밀번호 업데이트");
    }

    public String getTmpPassword() {
        char[] charSet = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        StringBuilder pwd = new StringBuilder();

        /* 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 조합 */
        int idx = 0;
        for(int i = 0; i < 10; i++){
            idx = (int) (charSet.length * Math.random());
            pwd.append(charSet[idx]);
        }

        log.info("임시 비밀번호 생성");

        return pwd.toString();
    }

    @Transactional
    public void updateTempPassword(String tmpPassword, String email) {

        log.info("[임시비번:암호화전] => ",tmpPassword);
        String encryptPassword = passwordEncoder.encode(tmpPassword);
        log.info("[임시비번:암호화후] => ",encryptPassword);

        Account account = accountRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        account.updatePassword(encryptPassword);
        log.info("임시 비밀번호 업데이트");
    }
}
