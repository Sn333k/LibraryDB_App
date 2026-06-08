package com.example.libraryapp.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    // Wstrzykujemy dane z węzła Primary
    @Value("${app.datasource.primary.jdbc-url}") private String primaryUrl;
    @Value("${app.datasource.primary.username}") private String primaryUsername;
    @Value("${app.datasource.primary.password}") private String primaryPassword;

    // Wstrzykujemy dane z Load Balancera (HAProxy)
    @Value("${app.datasource.replica.jdbc-url}") private String replicaUrl;
    @Value("${app.datasource.replica.username}") private String replicaUsername;
    @Value("${app.datasource.replica.password}") private String replicaPassword;

    @Value("${app.datasource.primary.maximum-pool-size}") private int primaryPoolSize;
    @Value("${app.datasource.replica.maximum-pool-size}") private int replicaPoolSize;

    @Bean
    public DataSource primaryDataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class)
                .url(primaryUrl).username(primaryUsername).password(primaryPassword)
                .build();
        dataSource.setMaximumPoolSize(primaryPoolSize);
        return dataSource;
    }

    @Bean
    public DataSource replicaDataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class)
                .url(replicaUrl).username(replicaUsername).password(replicaPassword)
                .build();
        dataSource.setMaximumPoolSize(replicaPoolSize);
        return dataSource;
    }

    @Bean
    public DataSource routingDataSource() {
        PrimaryReplicaRoutingDataSource routingDataSource = new PrimaryReplicaRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("PRIMARY", primaryDataSource());
        dataSourceMap.put("REPLICA", replicaDataSource());

        routingDataSource.setTargetDataSources(dataSourceMap);
        // Jeśli coś pójdzie nie tak z wykrywaniem transakcji, zawsze ratuj się węzłem głównym
        routingDataSource.setDefaultTargetDataSource(primaryDataSource());

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        // LazyConnectionDataSourceProxy wstrzymuje fizyczne połączenie
        // aż do momentu zbudowania zapytania i ustalenia flag transakcji.
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    // Ten menedżer transakcji pilnuje poprawności ustawiania flagi readOnly
    @Bean
    public PlatformTransactionManager transactionManager(DataSource routingDataSource) {
        return new DataSourceTransactionManager(routingDataSource);
    }
}