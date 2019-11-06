package io.omlett.coffeehaus.player.service.security;

import java.security.PublicKey;
import lombok.AllArgsConstructor;
import org.keycloak.TokenVerifier;
import org.keycloak.TokenVerifier.Predicate;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;

@AllArgsConstructor
public class KeycloakTokenVerifier {

  private final Predicate[] checks;
  private final PublicKey publicKey;

  @SuppressWarnings("unchecked")
  TokenVerifier<AccessToken> verifyToken(String token) throws VerificationException {
    TokenVerifier<AccessToken> verifier = TokenVerifier
        .create(token, AccessToken.class)
        .publicKey(publicKey);

    return verifier.withChecks(checks).verify();
  }
}
