package com.dnd.ahaive.global.oauth2;

import com.dnd.ahaive.domain.user.entity.Provider;
import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import com.dnd.ahaive.global.security.jwt.JwtTokenProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

   private final JwtTokenProvider jwtTokenProvider;
   private final UserRepository userRepository;


    @Value("${jwt.access-token-expiry}")
    private Long jwtAccessTokenExpirationTime;
    @Value("${jwt.refresh-token-expiry}")
    private Long jwtRefreshTokenExpirationTime;




    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String socialEmail, nickname, providerId;

        Map<String, Object> customAttributes = new HashMap<>(attributes);



      if ("google".equals(provider)) {
            providerId = (String) attributes.get("sub");

            socialEmail = (String) attributes.get("email");
            nickname = (String) attributes.get("name");

            log.info("Google Provider ID: " + providerId);
            log.info("Google Email: " + socialEmail);
            log.info("Google Name: " + nickname);

        } else {
            throw new OAuth2AuthenticationException("지원되지 않는 소셜 서비스 입니다. : " + provider);
        }

        // 회원가입 또는 로그인 처리
      User user = userRepository.findByProviderId(providerId)
          .orElseGet(() -> {
            // 신규 유저라면 회원가입 처리
            return userRepository.save(
                User.createMember(
                    nickname,
                    100,
                    socialEmail,
                    Provider.GOOGLE,
                    providerId
                )
            );
          });

        // JWT 액세스 & 리프레시 토큰 발급

        String accessToken = jwtTokenProvider.createAccessToken(user.getUserUuid(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserUuid(), user.getRole());

        customAttributes.put("accessToken", accessToken);
        customAttributes.put("refreshToken", refreshToken);
        customAttributes.put("email", socialEmail);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                customAttributes,
                "email"
        );
    }
}