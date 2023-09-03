package net.ddns.yline.withAPI.service.authInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.authInfo.AuthInfo;
import net.ddns.yline.withAPI.domain.authInfo.ValidType;
import net.ddns.yline.withAPI.repository.authInfo.AuthInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthInfoService {
    private final AuthInfoRepository authCodeRepository;

    @Transactional
    public void saveSendAuthInfo(AuthInfo authInfo) {
        authCodeRepository.save(authInfo);
    }

    public AuthInfo findByEmail(String email) {
        return authCodeRepository.findFirstByEmailOrderByAuthTimeDesc(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 유효하지 않습니다. "));
    }

    @Transactional
    public void updateIsValid(AuthInfo authInfo, ValidType validType){
        authInfo.setValidType(validType);
    }

    public AuthInfo findAuth(String email) {
//        return authCodeRepository.findFirstByEmailAndAuthTimeBetweenOrderByAuthTimeDesc(email, LocalDateTime.MIN, LocalDateTime.MAX)
//                .orElseThrow(() -> new IllegalArgumentException("메일 인증을 진행해주세요."));

        return authCodeRepository.findFirstByEmailOrderByAuthTimeDesc(email)
                .orElseThrow(() -> new IllegalArgumentException("메일 인증을 진행해주세요."));
    }

    public String getAuthCode() {
        char[] charSet = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        StringBuilder code = new StringBuilder();

        /* 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 조합 */
        int idx = 0;
        for(int i = 0; i < 6; i++){
            idx = (int) (charSet.length * Math.random());
            code.append(charSet[idx]);
        }

        log.info("임시 비밀번호 생성");

        return code.toString();
    }
}
