package com.pivovarit.domain.rental;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
class RentalProdConfiguration {

    @Bean
    public PostgresMovieRepository movieRepository(JdbcClient jdbcClient) {
        return new PostgresMovieRepository(jdbcClient);
    }

    @Bean
    @Profile("jdbi")
    public JdbiPostgresMovieRepository jdbiMovieRepository(DataSource ds) {
        return new JdbiPostgresMovieRepository(ds);
    }

    @Bean
    public MovieDescriptionsRepository movieDescriptionsRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcMovieDescriptionsRepository(jdbcTemplate);
    }
}
