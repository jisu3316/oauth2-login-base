package com.example.oauth2loginbase.common.converters;


import com.example.oauth2loginbase.common.enums.OAuth2Config;
import com.example.oauth2loginbase.common.util.OAuth2Utils;
import com.example.oauth2loginbase.model.ProviderUser;
import com.example.oauth2loginbase.model.social.GoogleUser;

public class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
        //registrationId 는 구글, 네이버등 pathVariable 로 처음에 넘어간 값을 가져온다.
        if (!OAuth2Config.SocialType.GOOGLE.getSocialName().equals(providerUserRequest.clientRegistration().getRegistrationId())) {
            return null;
        }

        return new GoogleUser(OAuth2Utils.getMainAttributes(providerUserRequest.oAuth2User()),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration()
        );
    }
}
