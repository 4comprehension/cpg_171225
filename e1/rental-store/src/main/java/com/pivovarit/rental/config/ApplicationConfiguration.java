package com.pivovarit.rental.config;

import com.pivovarit.rental.MoviePriceCalculator;
import com.pivovarit.rental.persistence.InMemoryMovieDescriptionsRepository;
import com.pivovarit.rental.persistence.InMemoryMovieRepository;
import com.pivovarit.rental.persistence.InMemoryRentalHistory;
import com.pivovarit.rental.service.MovieRentalService;
import com.pivovarit.rental.service.RentalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApplicationConfiguration {

    @Bean
    public MoviePriceCalculator moviePriceCalculator(CalculatorConfiguration calculatorConfiguration) {
        return new MoviePriceCalculator(calculatorConfiguration.getPricing());
    }

    @Bean
    public RentalService rentalService(
      InMemoryMovieRepository movieRepository,
      InMemoryMovieDescriptionsRepository movieDescriptionsRepository,
      MovieRentalService movieRentalService) {
        return new RentalService(movieRepository, movieDescriptionsRepository, movieRentalService);
    }

    @Bean
    public MovieRentalService movieRentalService(InMemoryRentalHistory rentalHistoryRepository, InMemoryMovieRepository movieRepository) {
        return new MovieRentalService(rentalHistoryRepository, movieRepository);
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
