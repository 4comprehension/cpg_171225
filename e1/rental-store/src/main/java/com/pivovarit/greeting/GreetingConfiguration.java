package com.pivovarit.greeting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class GreetingConfiguration {

    @Bean
    @Profile("dev")
    public StaticGreetingRepository staticGreetingRepository() {
        return new StaticGreetingRepository();
    }

    @Bean
    @Profile("prod")
    public RandomizedGreetingRepository randomizedGreetingRepository() {
        return new RandomizedGreetingRepository();
    }
}
