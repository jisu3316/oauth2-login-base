package com.example.oauth2loginbase.service;

import com.example.oauth2loginbase.model.users.ProviderUser;
import com.example.oauth2loginbase.model.users.User;
import com.example.oauth2loginbase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(String registrationId, ProviderUser providerUser) {
        User user = User.builder()
                .registrationId(registrationId)
                .id(providerUser.getId())
                .username(providerUser.getUsername())
                .password(providerUser.getPassword())
                .provider(providerUser.getProvider())
                .email(providerUser.getEmail())
                .picture(providerUser.getPicture())
                .authorities(providerUser.getAuthorities())
                .build();
        userRepository.register(user);
    }
}
