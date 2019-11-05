package io.omlett.coffeehaus.player.service.account.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spotify")
public class SpotifyClientProperties {

  private String clientId;
  private String clientSecret;
  private String redirectUri;
}
