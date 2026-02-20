package com.dnd.ahaive.global.security.config;

import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.exception.ErrorCode;
import com.dnd.ahaive.global.oauth2.CustomOAuth2AuthorizationRequestResolver;
import com.dnd.ahaive.global.oauth2.OAuth2LoginSuccessHandler;
import com.dnd.ahaive.global.oauth2.OAuth2UserService;
import com.dnd.ahaive.global.security.jwt.JwtTokenFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  /**
   * 예외처리 응답을 위해 사용되는 객체입니다.
   */
  private final ObjectMapper objectMapper;
  private final OAuth2UserService OAuth2UserService;
  private final CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter,
      OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) throws Exception {

    // 인증 실패 시 반환할 JSON 응답
    String invalidAuthenticationResponse = objectMapper
        .writeValueAsString(ResponseDTO.of(ErrorCode.UNAUTHORIZED));

    // 인가 실패 시 반환할 JSON 응답
    String invalidAuthorizationResponse = objectMapper
        .writeValueAsString(ResponseDTO.of(ErrorCode.ACCESS_DENIED));

    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/oauth2/**",
                "/login/oauth2/code/**",
                "/api/auth/**",
                "/api/v1/auth/refresh",
                "/api/health"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .exceptionHandling(e -> e
            //인증 실패 시 응답 핸들링
            .authenticationEntryPoint(((request, response, authException) -> {
              response.setContentType("application/json;charset=UTF-8");
              response.setStatus(HttpStatus.UNAUTHORIZED.value());
              response.setContentType("application/json");
              response.getWriter().write(invalidAuthenticationResponse);
            }
            ))
            //인가 실패 시 응답 핸들링
            .accessDeniedHandler((request, response, authException) -> {
              response.setContentType("application/json;charset=UTF-8");
              response.setStatus(HttpStatus.FORBIDDEN.value());
              response.setContentType("application/json");
              response.getWriter().write(invalidAuthorizationResponse);
            }))
        .oauth2Login(oauth2 -> oauth2
            .authorizationEndpoint(authorization -> authorization
                .baseUri("/oauth2/authorization")
                .authorizationRequestResolver(customOAuth2AuthorizationRequestResolver)
            )
            .redirectionEndpoint(redirection -> redirection
                .baseUri("/login/oauth2/code/*"))
            .userInfoEndpoint(userInfo -> userInfo
                .userService(OAuth2UserService))
            .successHandler(oAuth2LoginSuccessHandler)
        )
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
  }

}
