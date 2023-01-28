package com.example.oauth2loginbase.common.converters;

import com.example.oauth2loginbase.model.ProviderUser;
import com.example.oauth2loginbase.model.users.FormUser;
import com.example.oauth2loginbase.model.users.User;

public class UserDetailsProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
        if (providerUserRequest.user() == null) {
            return null;
        }

        User user = providerUserRequest.user();
        return FormUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .provider("none")
                .authorities(user.getAuthorities())
                .build();
    }
}
