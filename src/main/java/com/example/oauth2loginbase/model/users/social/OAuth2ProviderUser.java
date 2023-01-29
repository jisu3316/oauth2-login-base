package com.example.oauth2loginbase.model.users.social;


import com.example.oauth2loginbase.model.users.ProviderUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/*
ProviderUser에서 공통적으로 사용하는 값 말고 서비스제공자마다 다른 값들을 사용하는것들은
추상클래스로 만든후 이것을 상속받아 각자 구현한다.
 */
@Data
public abstract class OAuth2ProviderUser implements ProviderUser {
    private final Map<String, Object> attributes;
    private final OAuth2User oAuth2User;
    private final ClientRegistration clientRegistration;
    private boolean isCertificated;

    public OAuth2ProviderUser(Map<String, Object> attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        this.attributes = attributes;
        this.oAuth2User = oAuth2User;
        this.clientRegistration = clientRegistration;
    }

    @Override
    public String getPassword() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getProvider() {
        return clientRegistration.getRegistrationId();
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities().stream().map(authority ->
                new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toList());
    }

    @Override
    public boolean isCertificated() {
        return isCertificated;
    }

    @Override
    public void isCertificated(boolean isCertificated) {
        this.isCertificated = isCertificated;
    }
}
