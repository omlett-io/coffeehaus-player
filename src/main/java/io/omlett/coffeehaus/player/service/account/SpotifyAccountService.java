package io.omlett.coffeehaus.player.service.account;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import io.omlett.coffeehaus.player.service.persistence.entity.AccountEntity;
import io.omlett.coffeehaus.player.service.persistence.entity.AccountKey;
import io.omlett.coffeehaus.player.service.persistence.entity.PlayerEntity;
import io.omlett.coffeehaus.player.service.persistence.entity.PlayerKey;
import io.omlett.coffeehaus.player.service.persistence.proto.AccountProfileEntity;
import io.omlett.coffeehaus.player.service.persistence.proto.PlayerControlLink;
import io.omlett.coffeehaus.player.service.persistence.proto.PlayerProfileEntity;
import io.omlett.coffeehaus.player.service.persistence.repository.AccountProfileRepository;
import io.omlett.coffeehaus.player.service.persistence.repository.PlayerProfileRepository;
import io.omlett.coffeehaus.player.service.proto.MusicStreamingService;
import io.omlett.coffeehaus.player.service.security.UserDetails;
import io.omlett.coffeehaus.player.service.web.proto.Player;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SpotifyAccountService {

  private final String fullAccessScopes;
  private final SpotifyApi spotifyApi;
  private final AccountProfileRepository accountProfileRepository;
  private final PlayerProfileRepository playerProfileRepository;

  public String getAuthorizationCodeUri(String username) {

    AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
        .state(username)
        .scope(fullAccessScopes)
        .build();

    URI uri = authorizationCodeUriRequest.execute();
    return uri.toString();
  }

  public Player addPlayer(UserDetails user, MusicStreamingService playerType,
      String authorizationCode) {

    final AccountKey accountKey = AccountKey.of(user.getEmail(), user.getSubject());
    Optional<AccountEntity> accountEntity = accountProfileRepository.findById(accountKey);

    final AccountProfileEntity accountProfileEntity;
    if (accountEntity.isPresent()) {
      accountProfileEntity = accountEntity.get().getProfile();
    } else {
      accountProfileEntity = AccountProfileEntity.newBuilder()
          .setUsername(user.getUsername())
          .setEmailAddress(user.getEmail())
          .setFirstName(user.getGivenName())
          .setLastName(user.getFamilyName())
          .build();
    }

    String playerId = UUID.randomUUID().toString();
    PlayerKey playerKey = PlayerKey.of(playerType.toString(), playerId);

    PlayerControlLink playerControlLink = PlayerControlLink.newBuilder()
        .setPlayerId(playerId)
        .setControllerId(user.getSubject())
        .build();

    PlayerProfileEntity playerProfileEntity = PlayerProfileEntity.newBuilder()
        .setType(playerType)
        .setAuthorizationCode(authorizationCode)
        .addControllers(playerControlLink)
        .build();

    accountProfileEntity.toBuilder().addAllPlayersOwned(Collections.singletonList(playerId))
        .build();

    AccountEntity savedAccount = accountProfileRepository
        .save(AccountEntity.of(accountKey, accountProfileEntity));
    PlayerEntity savedPlayer = playerProfileRepository
        .save(PlayerEntity.of(playerKey, playerProfileEntity));

    return Player.newBuilder()
        .setId(savedPlayer.getKey().getId())
        .setOwnerId(savedAccount.getKey().getId())
        .setType(MusicStreamingService.valueOf(savedPlayer.getKey().getService()))
        .build();
  }

}
