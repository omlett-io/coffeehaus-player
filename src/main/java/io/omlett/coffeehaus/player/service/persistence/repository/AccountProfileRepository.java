package io.omlett.coffeehaus.player.service.persistence.repository;

import io.omlett.coffeehaus.player.service.persistence.entity.AccountKey;
import io.omlett.coffeehaus.player.service.persistence.proto.AccountProfileEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface AccountProfileRepository extends
    CassandraRepository<AccountProfileEntity, AccountKey> {

}
