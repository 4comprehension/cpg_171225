package com.pivovarit;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

// 1 dodajemy migrację z tabelą pod filmy
// 2 dodajemy nowe implementacje *Repository operujące na JdbcClient
// 3 porządkujemy profile tak, żeby postgres był wykorzystywany tylko na profilu 'prod' a na wszystkich innych startowały zaślepki InMemory*
@SpringBootApplication
@ConfigurationPropertiesScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Component
    static class Runner implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) {
        }
    }
}
