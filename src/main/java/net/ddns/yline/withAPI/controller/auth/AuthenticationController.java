package net.ddns.yline.withAPI.controller.auth;

import jakarta.validation.Valid;
import lombok.*;
import net.ddns.yline.withAPI.domain.authInfo.AuthInfo;
import net.ddns.yline.withAPI.domain.authInfo.ValidType;
import net.ddns.yline.withAPI.service.AuthenticationService;
import net.ddns.yline.withAPI.service.authInfo.AuthInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthInfoService authInfoService;

    /**
     * 가입
     * @param request
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        AuthInfo findAuthInfo = authInfoService.findAuth(request.getEmail());

        if(findAuthInfo.getValidType().equals(ValidType.VALID)){
            return ResponseEntity.ok(authenticationService.register(request));
        }else{
            authInfoService.updateIsValid(findAuthInfo, ValidType.EXPIRED);
            throw new IllegalArgumentException("메일 인증이 유효하지 않습니다.");
        }
    }

    /**
     * 인증
     * @param request
     * @return
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
