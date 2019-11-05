package io.omlett.coffeehaus.player.service.security;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class UserDetails {

  private String subject;
  private String username;
  private String email;
  private String givenName;
  private String familyName;
}
