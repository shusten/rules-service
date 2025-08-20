package com.rules.service.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcRepositories(
    basePackages = "com.rules.service.repository.foracesso", // Pacote dos repositórios do SQL Server
    entityOperationsRef = "sqlServerEntityTemplate" // Aponta para o template específico abaixo
)
public class SqlServerConfig {

    @Bean("sqlServerConnectionFactory")
    public ConnectionFactory sqlServerConnectionFactory(Environment env) {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(env.getProperty("spring.r2dbc.sqlserver.url"))
                .mutate()
                .option(USER, env.getProperty("spring.r2dbc.sqlserver.username"))
                .option(PASSWORD, env.getProperty("spring.r2dbc.sqlserver.password"))
                .build();

        return ConnectionFactories.get(options);
    }

    @Bean("sqlServerEntityTemplate")
    public R2dbcEntityTemplate sqlServerEntityTemplate(@Qualifier("sqlServerConnectionFactory") ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }
}