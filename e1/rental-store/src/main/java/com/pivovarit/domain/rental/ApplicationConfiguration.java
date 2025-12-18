package com.pivovarit.domain.rental;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApplicationConfiguration {

    @Bean
    public MoviePriceCalculator moviePriceCalculator(CalculatorConfiguration calculatorConfiguration) {
        return new MoviePriceCalculator(calculatorConfiguration.getPricing());
    }

    @Bean
    public RentalFacade rentalService(
      InMemoryMovieRepository movieRepository,
      InMemoryMovieDescriptionsRepository movieDescriptionsRepository,
      MovieCatalogueFacade movieCatalogueFacade) {
        return new RentalFacade(movieRepository, movieDescriptionsRepository, movieCatalogueFacade);
    }

    @Bean
    public MovieCatalogueFacade movieRentalService(InMemoryRentalHistory rentalHistoryRepository, InMemoryMovieRepository movieRepository) {
        return new MovieCatalogueFacade(rentalHistoryRepository, movieRepository);
    }

    @Bean
    public InMemoryRentalHistory rentalHistoryRepository() {
        return new InMemoryRentalHistory();
    }

    @Bean
    public InMemoryMovieRepository movieRepository() {
        return new InMemoryMovieRepository();
    }

    @Bean
    public InMemoryMovieDescriptionsRepository movieDescriptionsRepository() {
        return new InMemoryMovieDescriptionsRepository();
    }
}
