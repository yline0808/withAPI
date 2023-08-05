package net.ddns.yline.withAPI.controller.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest{
    @NotBlank(message = "email은 필수값 입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수값 입니다.")
    private String password;
}