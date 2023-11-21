package net.ddns.yline.withAPI.domain.token;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.base.BaseEntity;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Token extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
