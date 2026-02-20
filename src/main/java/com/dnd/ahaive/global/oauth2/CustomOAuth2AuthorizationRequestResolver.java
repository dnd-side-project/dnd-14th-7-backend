package com.dnd.ahaive.global.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CustomOAuth2AuthorizationRequestResolver implements
    OAuth2AuthorizationRequestResolver {

  private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

  public CustomOAuth2AuthorizationRequestResolver(
      ClientRegistrationRepository clientRegistrationRepository) {
    this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository, "/oauth2/authorization");
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    saveUri(request);
    return defaultResolver.resolve(request);
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request,
      String clientRegistrationId) {
    saveUri(request);
    return defaultResolver.resolve(request, clientRegistrationId);
  }

  private void saveUri(HttpServletRequest request) {
    String redirectUri = request.getParameter("redirectUri");
    if (StringUtils.hasText(redirectUri)) {
      request.getSession().setAttribute("redirectUri", redirectUri);
    }
  }

}
