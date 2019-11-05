package io.omlett.coffeehaus.player.service.account.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spotify.scopes")
public class SpotifyScopesProperties {

  private String fullAccess;
}
