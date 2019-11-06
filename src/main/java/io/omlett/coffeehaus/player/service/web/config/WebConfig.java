package io.omlett.coffeehaus.player.service.web.config;

import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Configuration
public class WebConfig {

  @Bean
  ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter() {
    return new ProtobufJsonFormatHttpMessageConverter();
  }

  @Bean
  RestTemplate restTemplate(ProtobufJsonFormatHttpMessageConverter hmc) {
    return new RestTemplate(Collections.singletonList(hmc));
  }
}
