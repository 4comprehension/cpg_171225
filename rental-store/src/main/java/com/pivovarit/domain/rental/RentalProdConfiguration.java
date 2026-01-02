package com.pivovarit.domain.rental;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.client.RestClient;

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
    IntegratedMovieDescriptionsRepository movieDescriptionsRepository(@Value("${service.movie-descriptions.url}") String url, RestClient.Builder restClient) {
        return new IntegratedMovieDescriptionsRepository(url, restClient);
    }
}
