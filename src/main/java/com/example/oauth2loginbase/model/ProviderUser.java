package com.example.oauth2loginbase.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

/*
  구글 네이버, 키클록, 카카오등 여러개의 서비스 제공자 별로 사용자 정볼르 담기 위한 클래스
  아래의 정보들은 공통적으로 사용하는것들로 인터페이스로 추상화 시킨다.
 */
public interface ProviderUser {

    String getId();

    String getUsername();

    String getPassword();

    String getEmail();

    String getPicture();

    String getProvider();

    boolean isCertificated();

    List<? extends GrantedAuthority> getAuthorities();

    //서비스 제공자로부터 받는 값
    Map<String, Object> getAttributes();

    OAuth2User getOAuth2User();

    void isCertificated(boolean bool);
}
