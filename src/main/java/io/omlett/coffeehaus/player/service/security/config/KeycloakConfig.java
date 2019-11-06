package io.omlett.coffeehaus.player.service.security.config;

import io.omlett.coffeehaus.player.service.security.KeycloakTokenVerifier;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import lombok.AllArgsConstructor;
import org.keycloak.TokenVerifier.Predicate;
import org.keycloak.TokenVerifier.RealmUrlCheck;
import org.keycloak.jose.jws.AlgorithmType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakConfig {

  @Bean
  public KeycloakTokenVerifier keycloakTokenVerifier(KeycloakProperties keycloakProperties)
      throws InvalidKeySpecException, NoSuchAlgorithmException {

    byte[] publicCert = Base64.getDecoder().decode(keycloakProperties.getPublicKeyCertificate());
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicCert);
    KeyFactory keyFactory = KeyFactory.getInstance(AlgorithmType.RSA.name());
    PublicKey publicKey = keyFactory.generatePublic(keySpec);

    final Predicate[] checks = {new RealmUrlCheck(keycloakProperties.getRealmUrl())};

    return new KeycloakTokenVerifier(checks, publicKey);
  }
}
