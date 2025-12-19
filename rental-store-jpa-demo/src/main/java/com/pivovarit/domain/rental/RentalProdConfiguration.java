package com.pivovarit.domain.rental;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
class RentalProdConfiguration {

    @Bean
    public InMemoryMovieDescriptionsRepository movieDescriptionsRepository() {
        return new InMemoryMovieDescriptionsRepository();
    }
}
