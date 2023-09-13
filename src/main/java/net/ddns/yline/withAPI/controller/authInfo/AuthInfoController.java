package net.ddns.yline.withAPI.controller.authInfo;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.controller.SuccessResult;
import net.ddns.yline.withAPI.domain.authInfo.AuthInfo;
import net.ddns.yline.withAPI.domain.authInfo.ValidType;
import net.ddns.yline.withAPI.domain.mailvo.MailVo;
import net.ddns.yline.withAPI.service.account.AccountService;
import net.ddns.yline.withAPI.service.authInfo.AuthInfoService;
import net.ddns.yline.withAPI.service.mail.MailServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authInfo")
@Slf4j
public class AuthInfoController {
    private final AuthInfoService authInfoService;
    private final MailServiceImpl mailService;
    private final AccountService accountService;

    @PostMapping("/sendAuthCode")
    public ResponseEntity<SuccessResult> sendAuthCode(@RequestBody @Valid AuthInfoSendRequest request) {
        boolean isMailExist = accountService.checkEmail(request.getEmail());
        if(isMailExist) throw new IllegalArgumentException("이미 가입된 메일이 있습니다.");

        String authCode = authInfoService.getAuthCode();

        MailVo mail = mailService.createAuthCodeMail(authCode, request.getEmail());
        mailService.sendMail(mail);

        var authInfo = AuthInfo.builder()
                .email(request.getEmail())
                .code(authCode)
                .validType(ValidType.INVALID)
                .authTime(LocalDateTime.now().plusMinutes(3))
                .build();
        authInfoService.saveSendAuthInfo(authInfo);

        log.info("success send mail");
        return ResponseEntity.ok(new SuccessResult("Success send mail","메일발송 성공"));
    }

    @PostMapping("/isValid")
    public ResponseEntity<Object> isValid(@RequestBody @Valid AuthInfoValidRequest request) {
        AuthInfo findAuthInfo = authInfoService.findByEmail(request.getEmail());
        LocalDateTime now = LocalDateTime.now();

        log.info(now.toString());
        log.info(findAuthInfo.getAuthTime().toString());

        if(LocalDateTime.now().isBefore(findAuthInfo.getAuthTime())){
            if (findAuthInfo.getCode().equals(request.code)) {
                authInfoService.updateIsValid(findAuthInfo, ValidType.VALID);
                return ResponseEntity.ok(new SuccessResult("Success email auth", "인증완료!"));
            } else{
                throw new IllegalArgumentException("코드가 다릅니다.");
            }
        }else{
            authInfoService.updateIsValid(findAuthInfo, ValidType.EXPIRED);
            throw new IllegalArgumentException("시간이 만료됐습니다.");
        }
    }

    @Data
    static class AuthInfoSendRequest{
        private String email;
    }

    @Data
    static class AuthInfoValidRequest{
        private String email;
        private String code;
    }
}
