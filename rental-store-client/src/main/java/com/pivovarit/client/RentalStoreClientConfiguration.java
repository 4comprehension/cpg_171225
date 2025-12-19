package com.pivovarit.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class RentalStoreClientConfiguration {

    @Bean
    RentalStoreClient client(@Value("${service.rental-store.url}") String url, RestClient.Builder restClient) {
        return new RentalStoreClient(url, restClient);
    }
}
