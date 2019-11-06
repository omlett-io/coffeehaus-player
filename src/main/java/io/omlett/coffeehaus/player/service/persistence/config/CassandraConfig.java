package io.omlett.coffeehaus.player.service.persistence.config;

import io.omlett.coffeehaus.player.service.persistence.converter.ProtobufReadConverter;
import io.omlett.coffeehaus.player.service.persistence.converter.ProtobufWriteConverter;
import io.omlett.coffeehaus.player.service.persistence.proto.AccountProfileEntity;
import io.omlett.coffeehaus.player.service.persistence.proto.PlayerProfileEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraEntityClassScanner;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.convert.CustomConversions;

@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(CassandraProperties.class)
@EnableCassandraRepositories(basePackages = "io.omlett.coffeehaus.player.service.persistence.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

  private CassandraProperties cassandraProperties;

  // Classes auto-generated by Protobuf Maven Plugin
  private static final String[] ENTITY_PACKAGES =
      new String[]{"io.omlett.coffeehaus.player.service.persistence.entity"};

  @Override
  protected String getKeyspaceName() {
    return cassandraProperties.getKeyspaceName();
  }

  @Bean
  @Override
  public CassandraClusterFactoryBean cluster() {
    final CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
    cluster.setContactPoints(cassandraProperties.getContactPoints());
    cluster.setPort(cassandraProperties.getPort());
    cluster.setUsername(cassandraProperties.getUsername());
    cluster.setPassword(cassandraProperties.getPassword());
    cluster.setKeyspaceCreations(getKeyspaceCreations());
    cluster.setJmxReportingEnabled(false);

    return cluster;
  }

  @Bean
  @Override
  public CassandraSessionFactoryBean session() {
    final CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
    session.setKeyspaceName(getKeyspaceName());
    session.setCluster(Objects.requireNonNull(cluster().getObject()));
    session.setConverter(cassandraConverter());
    session.setSchemaAction(getSchemaAction());
    return session;
  }

  @Bean
  @Override
  public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
    final CassandraMappingContext context = new CassandraMappingContext();
    context.setInitialEntitySet(getInitialEntitySet());
    context.setCustomConversions(customConversions());

    return context;
  }

  @Bean
  @Override
  public CustomConversions customConversions() {
    final List<Converter<?, ?>> converters = new ArrayList<>();

    converters.add(new ProtobufWriteConverter<AccountProfileEntity>());
    converters.add(new ProtobufReadConverter<>(AccountProfileEntity.parser()));

    converters.add(new ProtobufWriteConverter<PlayerProfileEntity>());
    converters.add(new ProtobufReadConverter<>(PlayerProfileEntity.parser()));

    return new CassandraCustomConversions(converters);
  }

  @Bean
  @Override
  public CassandraAdminTemplate cassandraTemplate() {
    return new CassandraAdminTemplate(Objects.requireNonNull(session().getObject()),
        cassandraConverter());
  }

  @Override
  public String[] getEntityBasePackages() {
    return ENTITY_PACKAGES;
  }

  @Override
  public SchemaAction getSchemaAction() {
    return SchemaAction.CREATE_IF_NOT_EXISTS;
  }

  @Override
  protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
    return CassandraEntityClassScanner.scan(getEntityBasePackages());
  }

  @Override
  protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
    final CreateKeyspaceSpecification specification = CreateKeyspaceSpecification
        .createKeyspace(getKeyspaceName())
        .with(KeyspaceOption.DURABLE_WRITES, true)
        .withSimpleReplication()
        .ifNotExists();

    return Collections.singletonList(specification);
  }
}
