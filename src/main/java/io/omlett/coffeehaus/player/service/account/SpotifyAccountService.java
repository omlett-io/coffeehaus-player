package io.omlett.coffeehaus.player.service.account;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import io.omlett.coffeehaus.player.service.web.proto.AddPlayerRequest;
import io.omlett.coffeehaus.player.service.web.proto.AddPlayerResponse;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SpotifyAccountService {

  private final String fullAccessScopes;
  private final SpotifyApi spotifyApi;

  public String getAuthorizationCodeUri(String username) {

    AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
        .state(username.substring(0, 5))
        .scope(fullAccessScopes)
        .build();

    URI uri = authorizationCodeUriRequest.execute();
    return uri.toString();
  }

  public AddPlayerResponse addPlayer(AddPlayerRequest request) {
    return AddPlayerResponse.newBuilder()
        .setStatus(false)
        .build();
  }

}
