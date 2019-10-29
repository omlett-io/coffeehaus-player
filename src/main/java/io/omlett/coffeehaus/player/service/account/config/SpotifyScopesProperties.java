package io.omlett.coffeehaus.player.service.account.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spotify.scopes")
@Data
public class SpotifyScopesProperties {

  private String fullAccess;
}
