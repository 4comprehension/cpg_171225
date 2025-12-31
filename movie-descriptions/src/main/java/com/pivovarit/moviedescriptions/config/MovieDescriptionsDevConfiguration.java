package com.pivovarit.moviedescriptions.config;

import com.pivovarit.moviedescriptions.repository.InMemoryMovieDescriptionsRepository;
import com.pivovarit.moviedescriptions.repository.JdbiH2MovieDescriptionsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
class MovieDescriptionsDevConfiguration {

    @Bean
    public JdbiH2MovieDescriptionsRepository jdbiMovieRepository(DataSource ds) {
        return new JdbiH2MovieDescriptionsRepository(ds);
    }
}
