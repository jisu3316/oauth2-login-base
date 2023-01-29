package com.example.oauth2loginbase.common.converters;

import com.example.oauth2loginbase.common.enums.OAuth2Config;
import com.example.oauth2loginbase.common.util.OAuth2Utils;
import com.example.oauth2loginbase.model.users.ProviderUser;
import com.example.oauth2loginbase.model.users.social.NaverUser;

public class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        if (!OAuth2Config.SocialType.NAVER.getSocialName().equals(providerUserRequest.clientRegistration().getRegistrationId())) {
            return null;
        }

        return new NaverUser(OAuth2Utils.getSubAttributes(
                providerUserRequest.oAuth2User(), "response"),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration()
        );
    }
}
