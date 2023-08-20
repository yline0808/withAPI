package net.ddns.yline.withAPI.util;

import net.ddns.yline.withAPI.domain.account.Account;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class CustomAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // Spring Security에서 사용자 이름을 추출하는 로직을 여기에 작성
        // 예를 들어, authentication.getPrincipal()로 UserDetails 객체를 얻고, UserDetails.getUsername()으로 사용자 이름을 추출
        // 아래 예시는 UserDetails 인터페이스를 사용한 경우입니다.
        if (authentication.getPrincipal() instanceof Account findAccount) {
            return Optional.of(findAccount.getId()+"_"+findAccount.getEmail());
        }

        return Optional.empty();
    }
}
