package com.example.oauth2loginbase.certification;

import com.example.oauth2loginbase.model.ProviderUser;
import com.example.oauth2loginbase.model.users.User;
import com.example.oauth2loginbase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SelfCertification {

    private final UserRepository userRepository;

    public void checkCertification(ProviderUser providerUser) {
        User user = userRepository.findByUsername(providerUser.getId());
//        if(user != null) {
        boolean bool = "none".equals(providerUser.getProvider()) || "naver".equals(providerUser.getProvider());
        providerUser.isCertificated(bool);
//        }
    }

    public void certificate(ProviderUser providerUser) {

    }
}
