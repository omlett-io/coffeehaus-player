package io.omlett.coffeehaus.player.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spotify")
@Data
public class SpotifyClientProperties {

  private String clientId;
  private String clientSecret;
  private String redirectUri;
}
