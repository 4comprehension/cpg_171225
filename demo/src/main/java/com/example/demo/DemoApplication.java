package com.example.demo;

import com.example.demo.bar.BarService;
import com.example.demo.foo.FooService;
import com.example.demo.hello.HelloWorldService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Component
    public static class DemoRunner implements ApplicationRunner {

        private final FooService fooService;
        private final BarService barService;
        private final HelloWorldService helloWorldService;

        DemoRunner(FooService fooService, BarService barService, HelloWorldService helloWorldService) {
            this.fooService = fooService;
            this.barService = barService;
            this.helloWorldService = helloWorldService;
        }

        @Override
        public void run(ApplicationArguments args) {
            System.out.println("hello world from ApplicationRunner");

            fooService.hello();
            barService.hello();

            helloWorldService.hello();
        }
    }
}
