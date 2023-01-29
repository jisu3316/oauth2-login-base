package com.example.oauth2loginbase.service;

import com.example.oauth2loginbase.certification.SelfCertification;
import com.example.oauth2loginbase.common.converters.ProviderUserConverter;
import com.example.oauth2loginbase.common.converters.ProviderUserRequest;
import com.example.oauth2loginbase.model.users.PrincipalUser;
import com.example.oauth2loginbase.model.users.ProviderUser;
import com.example.oauth2loginbase.model.users.User;
import com.example.oauth2loginbase.repository.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService extends AbstractOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository, UserService userService, SelfCertification certification, ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter, UserRepository userRepository1) {
        super(userRepository, userService, certification, providerUserConverter);
        this.userRepository = userRepository1;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            user = User.builder()
                    .id("1")
                    .username("user1")
                    .password("{noop}1234")
                    .authorities(AuthorityUtils.createAuthorityList("ROLE_USER"))
                    .email("user@a.com")
                    .build();
        }

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(user);
        ProviderUser providerUser = providerUser(providerUserRequest);

        selfCertificate(providerUser);

        return new PrincipalUser(providerUser);
    }
}
