package com.pivovarit.domain.rental;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RentalConfiguration {

    @Bean
    public RentalHistory rentalHistoryRepository() {
        return new InMemoryRentalHistory();
    }

    @Bean
    public MoviePriceCalculator moviePriceCalculator(CalculatorConfiguration calculatorConfiguration) {
        return new MoviePriceCalculator(calculatorConfiguration.getPricing());
    }

    @Bean
    public RentalFacade rentalService(
      MovieRepository movieRepository,
      InMemoryMovieDescriptionsRepository movieDescriptionsRepository,
      MovieRentalService movieRentalService) {
        return new RentalFacade(movieRepository, movieDescriptionsRepository, movieRentalService);
    }

    @Bean
    public MovieRentalService movieRentalService(RentalHistory rentalHistoryRepository, MovieRepository movieRepository) {
        return new MovieRentalService(rentalHistoryRepository, movieRepository);
    }
}
