package net.ddns.yline.withAPI.controller.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import net.ddns.yline.withAPI.domain.address.Address;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterRequest{
    @NotBlank(message = "email은 필수값 입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank(message = "생일은 필수값 입니다.")
    private String birthDate;
    @NotBlank(message = "이름은 필수값 입니다.")
    private String name;
    @NotBlank(message = "핸드폰 번호는 필수값 입니다.")
    private String phone;
    @NotBlank(message = "비밀번호는 필수값 입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
    @NotBlank(message = "메인 주소값은 필수입니다.")
    private String addressMain;
    @NotBlank(message = "세부 주소는 필수값입니다.")
    private String addressDetail;
    @NotBlank(message = "우편번호는 필수값 입니다.")
    private String zoneCode;
}