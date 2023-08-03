package net.ddns.yline.withAPI.controller.account;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.mailvo.MailVo;
import net.ddns.yline.withAPI.service.account.AccountService;
import net.ddns.yline.withAPI.service.mail.MailServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final MailServiceImpl mailService;

    /**
     * 이메일 존재여부
     * @param email
     * @return
     */
    @GetMapping("/checkEmail")
    public boolean checkEmail(@RequestParam("email") String email) {
        return accountService.checkEmail(email);
    }

    /**
     * 임시 비밀번호 전송
     * @param request
     * @return
     */
    @PostMapping("/sendPwd")
    public ResponseEntity<String> sendPwdEmail(@RequestBody @Valid AccountRequest request) {
        String tmpPassword = accountService.getTmpPassword();
        accountService.updatePassword(tmpPassword, request.getEmail());

        MailVo mail = mailService.createMail(tmpPassword, request.getEmail());
        mailService.sendMail(mail);

        log.info("success send mail!");
        return ResponseEntity.ok("success send mail!");
    }

    @Data
    static class AccountResponse{
        private Long id;

        public AccountResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class AccountRequest{
        private String email;
    }
}
