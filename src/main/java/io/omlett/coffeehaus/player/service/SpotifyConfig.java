package io.omlett.coffeehaus.player.service;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
@EnableConfigurationProperties({SpotifyClientProperties.class, SpotifyScopesProperties.class})
public class SpotifyConfig {

  @Bean
  SpotifyApi getSpotifyApi(SpotifyClientProperties clientProperties) {
    return new SpotifyApi.Builder()
        .setClientId(clientProperties.getClientId())
        .setClientSecret(clientProperties.getClientSecret())
        .setRedirectUri(SpotifyHttpManager.makeUri(clientProperties.getRedirectUri()))
        .build();
  }

  @Bean
  String getFullAccessScopes(SpotifyScopesProperties scopesProperties) {
    return scopesProperties.getFullAccess();
  }
}
