package com.pivovarit.moviedescriptions.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RentalStoreClientConfiguration {
    @Bean
    public RentalStoreClient rentalStoreClient(@Value("${service.rental-store.url}") String url, RestClient.Builder builder) {
        return new RentalStoreClient(url, builder);
    }
}
