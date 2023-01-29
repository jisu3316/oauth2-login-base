package com.example.oauth2loginbase.config;

import com.example.oauth2loginbase.service.CustomOAuth2UserService;
import com.example.oauth2loginbase.service.CustomOidcUserService;
import com.example.oauth2loginbase.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@EnableWebSecurity
@RequiredArgsConstructor
public class OAuth2ClientConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomOidcUserService customOidcUserService;

    private final CustomUserDetailService customUserDetailsService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/static/js/**", "/static/css/**", "/static/images/**", "/error");
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authRequest -> authRequest
                .antMatchers("/api/user").access("hasAnyRole('SCOPE_profile', 'SCOPE_email')")
                .antMatchers("/api/oidc").access("hasAnyRole('SCOPE_openid')")
                .antMatchers("/").permitAll()
                        .anyRequest().authenticated());
//        http.oauth2Login(Customizer.withDefaults());

        //폼 로그인과 연동하기위한 설정
        http.formLogin().loginPage("/login").loginProcessingUrl("/loginProc").defaultSuccessUrl("/").permitAll();

        //권한등 유저를 검증할때 내가 만든 커스텀 유저 서비스를 타게 설정해주는 부분이다.
        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(
                userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(customOAuth2UserService) //OAuth2
                        .oidcUserService(customOidcUserService))); //OpenID Connect
        http.userDetailsService(customUserDetailsService);
        //폼 로그인에서 인증예외 발생시 이동하기위한 설정
        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
        //로그아웃처리를 컨트롤러에서함
        http.logout().logoutSuccessUrl("/");
        return http.build();
    }
}
