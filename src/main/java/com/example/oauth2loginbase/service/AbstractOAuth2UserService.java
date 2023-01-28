package com.example.oauth2loginbase.service;

import com.example.oauth2loginbase.certification.SelfCertification;
import com.example.oauth2loginbase.common.converters.ProviderUserConverter;
import com.example.oauth2loginbase.common.converters.ProviderUserRequest;
import com.example.oauth2loginbase.model.ProviderUser;
import com.example.oauth2loginbase.model.users.User;
import com.example.oauth2loginbase.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

@Getter
@Service
@RequiredArgsConstructor
public class AbstractOAuth2UserService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final SelfCertification certification;
    private final ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;

    public void selfCertificate(ProviderUser providerUser){
        certification.checkCertification(providerUser);
    }

    public void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {

        User user = userRepository.findByUsername(providerUser.getUsername());

        if (user == null) {
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            userService.register(registrationId, providerUser);
        } else {
            System.out.println("user = " + user);
        }
    }

    //최종 반환 객체를 컨버터객체를 통해 반환 받게 된다.
    public ProviderUser providerUser(ProviderUserRequest providerUserRequest) {
        return providerUserConverter.converter(providerUserRequest);
    }
}
