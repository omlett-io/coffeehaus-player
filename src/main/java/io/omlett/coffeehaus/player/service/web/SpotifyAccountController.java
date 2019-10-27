package io.omlett.coffeehaus.player.service.web;

import io.omlett.coffeehaus.player.service.spotify.AccountService;
import java.net.URI;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/account")
public class SpotifyAccountController {

  private final AccountService service;

  public SpotifyAccountController(AccountService accountService) {
    this.service = accountService;
  }

  @RequestMapping(value = "/authorize", method = RequestMethod.GET)
  @CrossOrigin(origins = "http://localhost:3000")
  public URI getAuthorizationCodeUri() {
    return service.getAuthorizationCodeUri();
  }

  @RequestMapping(value = "/authorize", method = RequestMethod.POST)
  @CrossOrigin(origins = "http://localhost:3000")
  public boolean testPauseSong(@RequestParam("code") String authorizationCode) {

    return service.testPausePlayer(authorizationCode);
  }
}
