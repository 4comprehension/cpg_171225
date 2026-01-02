package com.pivovarit.moviedescriptions.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.pivovarit.moviedescriptions.repository.JdbiMovieDescriptionsRepository;

@Configuration
@Profile("prod")
class MovieDescriptionsProdConfiguration {

    @Bean
    public JdbiMovieDescriptionsRepository jdbiMovieRepository(DataSource ds) {
        return new JdbiMovieDescriptionsRepository(ds);
    }
}
