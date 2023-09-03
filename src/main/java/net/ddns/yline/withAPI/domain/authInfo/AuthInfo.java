package net.ddns.yline.withAPI.domain.authInfo;


import jakarta.persistence.*;
import lombok.*;
import net.ddns.yline.withAPI.domain.account.Role;
import net.ddns.yline.withAPI.domain.base.BaseEntity;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class AuthInfo extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String code;
    @Setter
    @Enumerated(EnumType.STRING)
    private ValidType validType;
    @Column(updatable = false)
    private LocalDateTime authTime;
}
