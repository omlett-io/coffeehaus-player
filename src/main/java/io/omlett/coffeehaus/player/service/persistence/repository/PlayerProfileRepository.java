package io.omlett.coffeehaus.player.service.persistence.repository;

import io.omlett.coffeehaus.player.service.persistence.entity.PlayerKey;
import io.omlett.coffeehaus.player.service.persistence.proto.PlayerProfileEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface PlayerProfileRepository extends
    CassandraRepository<PlayerProfileEntity, PlayerKey> {

}
