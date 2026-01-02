package com.pivovarit.moviedescriptions.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.pivovarit.moviedescriptions.repository.JdbiH2MovieDescriptionsRepository;

@Configuration
@Profile("dev")
class MovieDescriptionsDevConfiguration {

    @Bean
    public JdbiH2MovieDescriptionsRepository jdbiMovieRepository(DataSource ds) {
        return new JdbiH2MovieDescriptionsRepository(ds);
    }
}
