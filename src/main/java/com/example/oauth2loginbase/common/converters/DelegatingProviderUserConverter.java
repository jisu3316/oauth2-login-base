package com.example.oauth2loginbase.common.converters;

import com.example.oauth2loginbase.model.ProviderUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// 구글, 네이버, 카카오등의 컨버터 클래스를 가지고 있으면서 위임하는 방식이다. 이 클래스는 아래의 클래스들에서 값만 받아와 리턴해주는 클래스이다.
@Configuration
public class DelegatingProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    private final List<ProviderUserConverter<ProviderUserRequest, ProviderUser>> converters;

    // 초기화시 값을 전달 받을 클래스 목록을 저장하기 위함이다.
    //LinkedList 이기 때문에 일반 로그인시 구글이나 다른 OAuth2 컨버터 클래스의 값들이 없기 때문에 에러가난ㄷ. 그러므로 UserDetailsProviderUserConverter 를 링크드이스트 맨 앞으서 추가한다.
    public DelegatingProviderUserConverter() {
        List<ProviderUserConverter<ProviderUserRequest, ProviderUser>> providerUserConverters =
                Arrays.asList(new UserDetailsProviderUserConverter(),
                        new OAuth2GoogleProviderUserConverter(),
                        new OAuth2NaverProviderUserConverter(),
                        new OAuth2KakaoProviderUserConverter(),
                        new OAuth2KakaoOidcProviderUserConverter()
                        );
        this.converters = Collections.unmodifiableList(new LinkedList<>(providerUserConverters));
    }

    //ProviderUserRequest 에는 clientRegister 와 OAuth2USer 정보가 있다. 들어온 정보가 카카오인지, 구글인지 등등 판단을 할 수 있다.
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        Assert.notNull(providerUserRequest, "providerUserRequest cannot be null");

        //위의 초기화해둔 클래스들은 ProviderUserConverter 을 상속받아 만든 클래스들이기 때문에 반복문을 돌며 정보를 가져와 oauth2 유저 정보를 리턴해준다.
        //각 클래스의  converter 메서드를 타면서 enum과 비교해 검증한다.
        //다른 클래스들과 다르게 폼 유저는 유저가 널일수없기 때문에 user로 비교힌ㄷ.
        for (ProviderUserConverter<ProviderUserRequest, ProviderUser> converter : converters) {
            ProviderUser providerUser = converter.converter(providerUserRequest);
            if (providerUser != null) return providerUser;
        }

        return null;
    }
}
