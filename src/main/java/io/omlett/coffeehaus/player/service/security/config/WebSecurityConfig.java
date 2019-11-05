package io.omlett.coffeehaus.player.service.security.config;

import io.omlett.coffeehaus.player.service.security.JWTAuthorizationFilter;
import io.omlett.coffeehaus.player.service.security.KeycloakTokenVerifier;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@AllArgsConstructor
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private KeycloakTokenVerifier tokenVerifier;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .anyRequest().permitAll()
        .and()
        .addFilter(new JWTAuthorizationFilter(authenticationManager(), tokenVerifier))
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().cors()
        .and().csrf().disable();
  }
}
