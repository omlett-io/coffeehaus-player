package io.omlett.coffeehaus.player.service.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.player.PauseUsersPlaybackRequest;
import java.io.IOException;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class AccountService {

  private final String fullAccessScopes;
  private final SpotifyApi spotifyApi;

  public URI getAuthorizationCodeUri() {

    AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
        .state("username")
        .scope(fullAccessScopes)
        .build();

    URI uri = authorizationCodeUriRequest.execute();

    return uri;
  }

  public boolean testPausePlayer(String authorizationCode) {

    AuthorizationCodeRequest authorizationCodeRequest = spotifyApi
        .authorizationCode(authorizationCode)
        .build();

    AuthorizationCodeCredentials authorizationCodeCredentials;
    try {
      authorizationCodeCredentials = authorizationCodeRequest.execute();

      spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
      spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

    } catch (IOException ioException) {
      System.out.println("IOException");    // FIXME: Fix Logger
      return false;
    } catch (SpotifyWebApiException spotifyWebApiException) {
      System.out.println("SpotifyWebApiException");  // FIXME: Fix Logger
      return false;
    }

    PauseUsersPlaybackRequest pauseUsersPlaybackRequest = spotifyApi.pauseUsersPlayback()
        .build();

    try {
      final String string = pauseUsersPlaybackRequest.execute();

      System.out.println("Null: " + string);
      return true;
    } catch (IOException | SpotifyWebApiException e) {
      System.out.println("Error: " + e.getMessage());
      return false;
    }
  }


}
