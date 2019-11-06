package io.omlett.coffeehaus.player.service.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

  private String realmUrl;

  private String publicKeyCertificate;
}
