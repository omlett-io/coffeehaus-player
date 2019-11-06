package io.omlett.coffeehaus.player.service.persistence.repository;

import io.omlett.coffeehaus.player.service.persistence.entity.AccountEntity;
import io.omlett.coffeehaus.player.service.persistence.entity.AccountKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface AccountProfileRepository extends
    CassandraRepository<AccountEntity, AccountKey> {

}
