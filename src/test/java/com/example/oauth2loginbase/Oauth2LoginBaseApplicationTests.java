package com.example.oauth2loginbase;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SpringBootTest
class Oauth2LoginBaseApplicationTests {

    @Test
    void contextLoads() {
    }

    private final String prefix = "ROLE_";

    @Test
    void test() {
        GrantedAuthority authority = mapAuthority("jisu.openid");
        System.out.println("authority = " + authority);
    }

    private GrantedAuthority mapAuthority(String name) {

        if (name.lastIndexOf(".") > 0) {
            int index = name.lastIndexOf(".");
            name = "SCOPE_" + name.substring(index + 1);
        }

        if (!name.startsWith(prefix)) {
            name = prefix + name;
        }

        return new SimpleGrantedAuthority(name);
    }
}
