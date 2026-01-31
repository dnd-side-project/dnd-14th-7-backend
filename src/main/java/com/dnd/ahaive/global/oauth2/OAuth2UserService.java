package com.dnd.ahaive.global.oauth2;

import com.dnd.ahaive.global.security.jwt.JwtTokenProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

   // jwt 토큰 발급을 위한 유틸 객체
   private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.access-token-expiry}")
    private Long jwtAccessTokenExpirationTime;
    @Value("${jwt.refresh-token-expiry}")
    private Long jwtRefreshTokenExpirationTime;




    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String socialEmail, nickname;

        Map<String, Object> customAttributes = new HashMap<>(attributes);



      if ("google".equals(provider)) {

            socialEmail = (String) attributes.get("email");
            nickname = (String) attributes.get("name");

            log.info("Google Email: " + socialEmail);
            log.info("Google Name: " + nickname);

        } else {
            throw new OAuth2AuthenticationException("지원되지 않는 소셜 서비스 입니다. : " + provider);
        }

        log.info(provider+" 로그인 확인 socialEmail = " + socialEmail);
        log.info(provider+" 로그인 확인 nickname = " + nickname);


        // JWT 액세스 & 리프레시 토큰 발급

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String accessToken = jwtTokenProvider.createToken(authentication,jwtAccessTokenExpirationTime);
        String refreshToken = jwtTokenProvider.createToken(authentication,jwtRefreshTokenExpirationTime);

        customAttributes.put("accessToken", accessToken);
        customAttributes.put("refreshToken", refreshToken);

        return new DefaultOAuth2User(
            // TODO: 추후 실제 User 테이블에서 ROLE 값을 가져와야 함
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "email"
        );
    }
}