package io.omlett.coffeehaus.player.service.persistence.entity;

import com.datastax.driver.core.DataType.Name;
import io.omlett.coffeehaus.player.service.persistence.proto.PlayerProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@AllArgsConstructor(staticName = "of")
@Table("player")
public class PlayerEntity {

  @PrimaryKey
  private PlayerKey key;

  @Column
  @CassandraType(type = Name.BLOB)
  private PlayerProfileEntity player;
}
