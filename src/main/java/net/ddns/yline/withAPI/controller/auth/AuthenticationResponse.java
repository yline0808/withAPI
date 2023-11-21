package net.ddns.yline.withAPI.controller.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationResponse{
    private String token;
}