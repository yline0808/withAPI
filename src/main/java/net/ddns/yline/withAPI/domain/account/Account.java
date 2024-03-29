package net.ddns.yline.withAPI.domain.account;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.yline.withAPI.domain.address.Address;
import net.ddns.yline.withAPI.domain.base.BaseEntity;
import net.ddns.yline.withAPI.domain.contractMap.ContractMap;
import net.ddns.yline.withAPI.domain.token.Token;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Account extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue
    private Long Id;
    private String name;
    private String birthDate;
    private String email;
    private String phone;
    private String password;
    private Integer failCnt;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "account")
    private List<Token> tokens;
    @OneToMany(mappedBy = "account")
    private List<ContractMap> contractMapList = new ArrayList<>();
    @Embedded
    private Address address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //권한이 1개인 경우
//        return List.of(new SimpleGrantedAuthority(role.name()));

        //권한이 여러개인 경우
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    //===계약서 추가(연관관계 메서드)===
    public void addContract(ContractMap contractMap) {
        this.contractMapList.add(contractMap);

    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
