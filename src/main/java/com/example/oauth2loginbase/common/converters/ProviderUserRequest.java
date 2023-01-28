package com.example.oauth2loginbase.common.converters;

import com.example.oauth2loginbase.model.users.User;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
//자바 17에서 사용하는 record란 롬복에서 사용하는 게터를 롬복의존성 없이 사용할 수 있고 불변 객체로서 멀티 쓰레드 환경에서 쓰레드 세이프하게 사용할 수 있다. equals, hashcode, toString 를 생성해줍니다.
//이 객체는 CustomOAuth2UserService 에서 컨버터를 통해 유저 정보를 가져오게된다.
public record ProviderUserRequest(ClientRegistration clientRegistration, OAuth2User oAuth2User, User user) {

    //oauth2 유저 전용
    public ProviderUserRequest(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        this(clientRegistration, oAuth2User, null);
    }

    //일반 유저 전용
    public ProviderUserRequest(User user) {
        this(null, null, user);
    }
}
