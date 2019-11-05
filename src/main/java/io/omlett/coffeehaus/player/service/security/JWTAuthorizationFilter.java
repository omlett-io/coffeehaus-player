package io.omlett.coffeehaus.player.service.security;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private static final String TOKEN_PREFIX = "Bearer ";
  private static final String AUTHZ_HEADER_STRING = "Authorization";

  private KeycloakTokenVerifier tokenVerifier;

  public JWTAuthorizationFilter(AuthenticationManager authManager,
      KeycloakTokenVerifier tokenVerifier) {
    super(authManager);
    this.tokenVerifier = tokenVerifier;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    String header = request.getHeader(AUTHZ_HEADER_STRING);

    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    getAuthentication(header)
        .ifPresent(token -> SecurityContextHolder.getContext().setAuthentication(token));

    chain.doFilter(request, response);
  }

  private Optional<Authentication> getAuthentication(String header) {

    Authentication authentication = null;
    try {
      AccessToken token = tokenVerifier.verifyToken(header).getToken();
      UserDetails userDetails = extractUserFromToken(token);
      authentication = new PreAuthenticatedAuthenticationToken(userDetails, null);
    } catch (VerificationException e) {
      log.warn("Caught an exception trying to verify the access token: {}", e.getMessage());
    }

    return Optional.ofNullable(authentication);
  }

  private UserDetails extractUserFromToken(AccessToken token) {
    return UserDetails.builder()
        .subject(Objects.requireNonNull(token.getSubject(),
            "JWT Subject cannot be null."))
        .username(Objects.requireNonNull(token.getPreferredUsername(),
            "JWT username cannot be null."))
        .email(token.getEmail())
        .givenName(token.getGivenName())
        .familyName(token.getFamilyName())
        .build();
  }
}
