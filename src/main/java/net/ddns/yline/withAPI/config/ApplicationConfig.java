package net.ddns.yline.withAPI.config;

import lombok.RequiredArgsConstructor;
import net.ddns.yline.withAPI.execption.resolver.MyHandlerExceptionResolver;
import net.ddns.yline.withAPI.execption.resolver.UserHandlerExceptionResolver;
import net.ddns.yline.withAPI.repository.account.AccountRepository;
import net.ddns.yline.withAPI.util.CustomAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class ApplicationConfig implements WebMvcConfigurer {

    private final AccountRepository userRepository;

    //사용자 정보를 가져오기 위함
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not fount"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //사용자 정보를 가져오기 위함
        authProvider.setUserDetailsService(userDetailsService());
        //암호화를 위함
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        //직접 만든 HandlerExceptionResolver 등록
        resolvers.add(new UserHandlerExceptionResolver());
        resolvers.add(new MyHandlerExceptionResolver());
    }

    /**
     * Account의 id_email 형식으로 baseEntity id 값 지정
     * @return
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new CustomAuditorAware();
    }
}
