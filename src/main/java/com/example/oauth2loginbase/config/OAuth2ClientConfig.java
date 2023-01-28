package com.example.oauth2loginbase.config;

import com.example.oauth2loginbase.service.CustomOAuth2UserService;
import com.example.oauth2loginbase.service.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@RequiredArgsConstructor
public class OAuth2ClientConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomOidcUserService customOidcUserService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/static/js/**", "/static/css/**", "/static/images");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authRequest -> authRequest
                .requestMatchers(HttpMethod.GET, "/api/user").hasAnyRole("SCOPE_profile", "SCOPE_email")
                .requestMatchers(HttpMethod.GET, "/api/oidc").hasAnyRole("SCOPE_openid")
                .requestMatchers("/home", "/logout", "/login", "/").permitAll()
                .anyRequest().authenticated());
        http.oauth2Login(Customizer.withDefaults());

        //폼 로그인과 연동하기위한 설정
        http.formLogin().loginPage("/login").loginProcessingUrl("/loginProc").defaultSuccessUrl("/").permitAll();
        //폼 로그인에서 인증예외 발생시 이동하기위한 설정
        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
        //권한등 유저를 검증할때 내가 만든 커스텀 유저 서비스를 타게 설정해주는 부분이다.
        http.oauth2Login(oauth2 ->
                oauth2.userInfoEndpoint(userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(customOAuth2UserService) //OAuth2
                                .oidcUserService(customOidcUserService))); //OpenID Connect

        //로그아웃처리를 컨트롤러에서함
//        http.logout().logoutSuccessUrl("/");
        return http.build();
    }
}
