package com.example.oauth2loginbase.service;

import com.example.oauth2loginbase.certification.SelfCertification;
import com.example.oauth2loginbase.common.converters.ProviderUserConverter;
import com.example.oauth2loginbase.common.converters.ProviderUserRequest;
import com.example.oauth2loginbase.model.PrincipalUser;
import com.example.oauth2loginbase.model.ProviderUser;
import com.example.oauth2loginbase.repository.UserRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends AbstractOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    public CustomOAuth2UserService(UserRepository userRepository, UserService userService, SelfCertification certification, ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter) {
        super(userRepository, userService, certification, providerUserConverter);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        //인증 정보가 담아져 있는 객체
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oAuth2User);

        //유저 객체 반환 받음 converter 를 통해 google, naver, kakao 인지 판별하여 반환 받는다.
        ProviderUser providerUser = providerUser(providerUserRequest);
        //회원 가입 하기
        super.register(providerUser, userRequest);


        return new PrincipalUser(providerUser);
    }
}
