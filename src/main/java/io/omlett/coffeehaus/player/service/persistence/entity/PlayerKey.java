package io.omlett.coffeehaus.player.service.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@Data
@AllArgsConstructor(staticName = "of")
@PrimaryKeyClass
public class PlayerKey {

  @PrimaryKeyColumn(name = "service", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
  private String service;

  @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
  private String id;
}
