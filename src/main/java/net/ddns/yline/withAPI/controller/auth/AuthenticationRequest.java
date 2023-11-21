package net.ddns.yline.withAPI.controller.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationRequest{
    @NotBlank(message = "email은 필수값 입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수값 입니다.")
    private String password;
}