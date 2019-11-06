package io.omlett.coffeehaus.player.service.web;

import io.omlett.coffeehaus.player.service.account.SpotifyAccountService;
import io.omlett.coffeehaus.player.service.security.UserDetails;
import io.omlett.coffeehaus.player.service.web.exception.UnexpectedPrincipalException;
import io.omlett.coffeehaus.player.service.web.proto.AddPlayerRequest;
import io.omlett.coffeehaus.player.service.web.proto.AddPlayerResponse;
import io.omlett.coffeehaus.player.service.web.proto.AuthorizationUriRequest;
import io.omlett.coffeehaus.player.service.web.proto.AuthorizationUriResponse;
import io.omlett.coffeehaus.player.service.web.proto.AuthorizationUriResponse.ServiceUri;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/account")
@AllArgsConstructor
public class AccountController {

  private final SpotifyAccountService spotifyAccountService;

  @PostMapping(value = "/player/authorize-uri",
      consumes = "application/x-protobuf", produces = "application/x-protobuf")
  @CrossOrigin(origins = "*")
  public ResponseEntity<byte[]> getAuthorizationCodeUri(Authentication authentication,
      @RequestBody AuthorizationUriRequest request) {

    if (!(authentication.getPrincipal() instanceof UserDetails)) {
      throw new UnexpectedPrincipalException("Couldn't find user details. Are you logged in?");
    }

    UserDetails user = (UserDetails) authentication.getPrincipal();

    AuthorizationUriResponse.Builder response = AuthorizationUriResponse.newBuilder();
    request.getServicesList()
        .forEach(musicStreamingService -> {
          switch (musicStreamingService) {
            case SPOTIFY:
              response.addServiceUri(ServiceUri.newBuilder().setService(musicStreamingService)
                  .setUri(spotifyAccountService.getAuthorizationCodeUri(user.getUsername())))
                  .build();
              break;
            case SOUNDCLOUD:
              log.debug("Where's my JIRA ticket for SoundCloud?");
              break;
            case YOUTUBE:
              log.debug("Where's my JIRA ticket for YouTube?");
              break;
            case UNRECOGNIZED:
              log.debug("Where's my JIRA ticket for Unrecognized?");
              break;
          }
        });

    return ResponseEntity.accepted()
        .body(response.build().toByteArray());
  }

  @PostMapping(value = "/player/authorize",
      consumes = "application/x-protobuf", produces = "application/x-protobuf")
  @CrossOrigin(origins = "http://localhost:3000")
  public ResponseEntity<AddPlayerResponse> authorizePlayer(Authentication authentication,
      @RequestBody AddPlayerRequest request) {

    if (!(authentication.getPrincipal() instanceof UserDetails)) {
      throw new UnexpectedPrincipalException("Couldn't find user details. Are you logged in?");
    }

    UserDetails user = (UserDetails) authentication.getPrincipal();

    AddPlayerResponse.Builder response = AddPlayerResponse.newBuilder();
    switch (request.getPlayerType()) {
      case SPOTIFY:
        response
            .setPlayer(spotifyAccountService.addPlayer(user, request.getPlayerType(),
                request.getPlayerAuthorizationCode()))
            .setStatus(true);
        break;
      case SOUNDCLOUD:
        response.setStatus(false);
        log.debug("Where's my JIRA ticket for SoundCloud?");
        break;
      case YOUTUBE:
        response.setStatus(false);
        log.debug("Where's my JIRA ticket for YouTube?");
        break;
      case UNRECOGNIZED:
        response.setStatus(false);
        log.debug("Where's my JIRA ticket for Unrecognized?");
        break;
    }

    return ResponseEntity.accepted().body(response.build());
  }
}
