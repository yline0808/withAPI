package net.ddns.yline.withAPI.controller.account;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.account.AccountStatus;
import net.ddns.yline.withAPI.domain.account.Role;
import net.ddns.yline.withAPI.domain.address.Address;
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
    public ResponseEntity<AccountResponse> currentUser(Principal principal) {
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
    static class AccountRequest{
        private String email;
    }
}
