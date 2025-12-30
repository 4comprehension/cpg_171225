package com.pivovarit.moviedescriptions.config;

import com.pivovarit.moviedescriptions.repository.InMemoryMovieDescriptionsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
class MovieDescriptionsDevConfiguration {

    @Bean
    public InMemoryMovieDescriptionsRepository movieDescriptionsRepository() {
        return new InMemoryMovieDescriptionsRepository();
    }
}
