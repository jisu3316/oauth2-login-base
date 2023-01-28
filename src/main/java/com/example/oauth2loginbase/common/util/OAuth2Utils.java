package com.example.oauth2loginbase.common.util;

import com.example.oauth2loginbase.common.enums.OAuth2Config;
import com.example.oauth2loginbase.model.Attributes;
import com.example.oauth2loginbase.model.PrincipalUser;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class OAuth2Utils {

    //OAuth2User 는 상세 속성을 가지고 있다.
    //구글 같은 경우에는 OAuth2User 에서 depth 가 없이 바로 속성들을 가져올 수 있기 때문에 mainAttributes 에 넣어주면된다.
    public static Attributes getMainAttributes(OAuth2User oAuth2User) {

        return Attributes.builder()
                .mainAttributes(oAuth2User.getAttributes())
                .build();
    }

    //네이버 같은 경우에는 OAuth2User 에서 depth 가 있기때문에 (response 밑에 사용자 정보가 있음)  subAttributes 에 넣어 준 후 키 값으로 response 를 받아와 속성정보들을 가져온다.
    public static Attributes getSubAttributes(OAuth2User oAuth2User, String subAttributesKey) {

        Map<String, Object> subAttributes = (Map<String, Object>) oAuth2User.getAttributes().get(subAttributesKey);
        return Attributes.builder()
                .subAttributes(subAttributes)
                .build();
    }

    //카카오는 세개의 계층이 있기때문에 "kakao_account" 및에는 email 정보가 있지만 다른 유저정보는 없다., "profile" 밑에 닉네임및 프로필 이미지 주소가 들어있기 때문에 이 두개 키값을 통해 속성들을 가져와야한다.
    public static Attributes getOtherAttributes(OAuth2User oAuth2User, String subAttributesKey, String otherAttributesKey) {

        Map<String, Object> subAttributes = (Map<String, Object>) oAuth2User.getAttributes().get(subAttributesKey);
        Map<String, Object> otherAttributes = (Map<String, Object>) subAttributes.get(otherAttributesKey);

        return Attributes.builder()
                .subAttributes(subAttributes)
                .otherAttributes(otherAttributes)
                .build();
    }

    public static String oAuth2UserName(OAuth2AuthenticationToken authentication, PrincipalUser principalUser) {

        String userName;
        String registrationId = authentication.getAuthorizedClientRegistrationId();
        OAuth2User oAuth2User = principalUser.providerUser().getOAuth2User();

        // Google, Facebook, Apple
        Attributes attributes = OAuth2Utils.getMainAttributes(oAuth2User);
        userName = (String) attributes.getMainAttributes().get("name");

        // Naver
        if (OAuth2Config.SocialType.NAVER.getSocialName().equals(registrationId)) {
            attributes = OAuth2Utils.getSubAttributes(oAuth2User, "response");
            userName = (String) attributes.getSubAttributes().get("name");

            // Kakao
        } else if (OAuth2Config.SocialType.KAKAO.getSocialName().equals(registrationId)) {

            // OpenID Connect
            if (oAuth2User instanceof OidcUser) {
                attributes = OAuth2Utils.getMainAttributes(oAuth2User);
                userName = (String) attributes.getMainAttributes().get("nickname");

            } else {
                attributes = OAuth2Utils.getOtherAttributes(principalUser, "profile", null);
                userName = (String) attributes.getSubAttributes().get("nickname");
            }
        }
        return userName;
    }
}
