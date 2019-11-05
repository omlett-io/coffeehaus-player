package io.omlett.coffeehaus.player.service.persistence.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.data.cassandra")
public class CassandraProperties {

  private String contactPoints;

  private int port;

  private String username;

  private String password;

  private String keyspaceName;
}
