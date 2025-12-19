package com.pivovarit.domain.placeholder;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
class PlaceHolderConfiguration {

    @Bean
    @Profile("prod")
    ProductService productService(JdbcClient jdbcClient) {
        return new ProductService(jdbcClient);
    }

//    @Bean
    ApplicationRunner runner(ProductService productService) {
        return _ -> {

            System.out.println("productService.save(\"Foo\", 1321, false) = " + productService.save("Foo", 1321, false));

            System.out.println("productService.findAll()");
            productService.findAll().forEach(System.out::println);

            System.out.println("productService.findAllActive()");
            productService.findAllActive().forEach(System.out::println);
        };
    }
}
