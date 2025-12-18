package com.pivovarit.domain.rental;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RentalConfiguration {

    @Bean
    public MoviePriceCalculator moviePriceCalculator(CalculatorConfiguration calculatorConfiguration) {
        return new MoviePriceCalculator(calculatorConfiguration.getPricing());
    }

    @Bean
    public RentalFacade rentalService(
      InMemoryMovieRepository movieRepository,
      InMemoryMovieDescriptionsRepository movieDescriptionsRepository,
      MovieRentalService movieRentalService) {
        return new RentalFacade(movieRepository, movieDescriptionsRepository, movieRentalService);
    }

    @Bean
    public MovieRentalService movieRentalService(RentalHistory rentalHistoryRepository, InMemoryMovieRepository movieRepository) {
        return new MovieRentalService(rentalHistoryRepository, movieRepository);
    }
}
