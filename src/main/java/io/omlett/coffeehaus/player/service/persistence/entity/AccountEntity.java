package io.omlett.coffeehaus.player.service.persistence.entity;

import com.datastax.driver.core.DataType.Name;
import io.omlett.coffeehaus.player.service.persistence.proto.AccountProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Table("account")
public class AccountEntity {

  @PrimaryKey
  private AccountKey key;

  @Column
  @CassandraType(type = Name.BLOB)
  private AccountProfileEntity profile;
}
