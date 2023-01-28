package com.example.oauth2loginbase.common.authority;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.HashSet;

/*
구글 같은 경우 스코프가 http://~~~이런식으로 길게 넘어오기때문에 권한을 커스텀하게 매핑하기위한 클래스이다.
 */
public class CustomAuthorityMapper implements GrantedAuthoritiesMapper {

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        HashSet<GrantedAuthority> mapped = new HashSet<>(authorities.size());
        for (GrantedAuthority authority : authorities) {
            mapped.add(mapAuthority(authority.getAuthority()));
        }
        return mapped;
    }

    private GrantedAuthority mapAuthority(String name) {

        if (name.lastIndexOf(".") > 0) {      //http://google.com/abc.email
            int index = name.lastIndexOf(".");
            name = "SCOPE_" + name.substring(index + 1); //name = SCOPE_email
        }

        // 기본 prefix는 ROLE_ 이다.
        String prefix = "ROLE_";
        if (!name.startsWith(prefix)) {   //name = ROLE_SCOPE_email
            name = prefix + name;
        }

        return new SimpleGrantedAuthority(name);
    }
}
