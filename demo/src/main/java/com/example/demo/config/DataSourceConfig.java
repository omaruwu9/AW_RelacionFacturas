package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfig{

    @Primary
    @Bean("postgresqlProperties")
    @ConfigurationProperties(prefix = "psql.datasource")
    public DataSourceProperties getPostgresqlProperties(){
        return new DataSourceProperties();
    }

    @Primary
    @Bean("postgresqlDatasource")
    public DataSource getPostgesqlDatasource(){
        DataSourceProperties postgresqlProps = getPostgresqlProperties();
        return postgresqlProps.initializeDataSourceBuilder()
                .build();
    }

    @Bean("sqlserverProperties")
    @ConfigurationProperties(prefix = "sqlserver.datasource")
    public DataSourceProperties getasqlserverProperties(){
        return new DataSourceProperties();
    }

    @Bean("sqlserverDatasource")
    public DataSource getsqlserverDatasource(){
        DataSourceProperties sqlserverProps = getasqlserverProperties();
        return sqlserverProps.initializeDataSourceBuilder()
                .build();
    }

    @Bean("postgresJdbcTemplate")
    public JdbcTemplate postgresJdbcTemplate(@Qualifier("postgresqlDatasource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean("sqlServerJdbcTemplate")
    public JdbcTemplate sqlServerJdbcTemplate(@Qualifier("sqlserverDatasource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}