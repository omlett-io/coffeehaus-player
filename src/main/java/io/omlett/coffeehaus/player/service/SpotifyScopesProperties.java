package io.omlett.coffeehaus.player.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spotify.scopes")
@Data
public class SpotifyScopesProperties {

  private String fullAccess;
}
