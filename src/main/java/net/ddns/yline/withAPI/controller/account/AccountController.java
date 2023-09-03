package net.ddns.yline.withAPI.controller.account;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.controller.SuccessResult;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.account.AccountStatus;
import net.ddns.yline.withAPI.domain.account.Role;
import net.ddns.yline.withAPI.domain.mailvo.MailVo;
import net.ddns.yline.withAPI.service.account.AccountService;
import net.ddns.yline.withAPI.service.mail.MailServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

    @GetMapping("/currentUser")
    public ResponseEntity<AccountResponse> getCurrentUser(Principal principal) {
        String currentEmail = principal.getName();
        Account findAccount = accountService.findByEmail(currentEmail);
        return ResponseEntity.ok(new AccountResponse(
                findAccount.getName(),
                findAccount.getBirthDate(),
                findAccount.getEmail(),
                findAccount.getPhone(),
                findAccount.getAccountStatus(),
                findAccount.getRole(),
                findAccount.getAddress().getAddressMain(),
                findAccount.getAddress().getAddressDetail(),
                findAccount.getAddress().getZoneCode()
        ));
    }

    @PatchMapping("/currentUser")
    public ResponseEntity<SuccessResult> modifyCurrentUser(@RequestBody @Valid ModifyAccountRequest request) {
        accountService.updatePassword(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new SuccessResult("Success change password", "비밀번호 변경 완료"));
    }

    /**
     * 임시 비밀번호 전송
     * @param request
     * @return
     */
    @PostMapping("/sendPwd")
    public ResponseEntity<SuccessResult> sendPwdEmail(@RequestBody @Valid AccountRequest request) {
        String tmpPassword = accountService.getTmpPassword();
        accountService.updateTempPassword(tmpPassword, request.getEmail());

        MailVo mail = mailService.createPwUpdateMail(tmpPassword, request.getEmail());
        mailService.sendMail(mail);

        log.info("success send mail!");
        return ResponseEntity.ok(new SuccessResult("Success send mail","메일발송 성공"));
    }

    @Data
    @AllArgsConstructor
    static class AccountResponse{
        private String name;
        private String birthDate;
        private String email;
        private String phone;
        private AccountStatus accountStatus;
        private Role role;
        private String addressMain;
        private String addressDetail;
        private String zoneCode;
    }

    @Data
    static class ModifyAccountRequest{
        private String email;
        private String password;
        private String phone;
        private AccountStatus accountStatus;
        private Role role;
        private String addressMain;
        private String addressDetail;
        private String zoneCode;
    }

    @Data
    static class AccountRequest{
        private String email;
    }
}
