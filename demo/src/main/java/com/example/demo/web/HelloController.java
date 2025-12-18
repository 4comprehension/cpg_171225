package com.example.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
class HelloController {

    @GetMapping("/hello")
    public HelloResponse hello() {
        return new HelloResponse("Hello World!");
    }

    @GetMapping("/hello-query-param")
    public HelloResponse helloQueryParam(@RequestParam String name) {
        return new HelloResponse("Hello %s!".formatted(name));
    }

    @GetMapping("/hello-query-param-optional")
    public HelloResponse helloQueryOptionalParam(@RequestParam(required = true) Optional<String> name) {
        return new HelloResponse("Hello %s!".formatted(name.orElseThrow()));
    }

    @GetMapping("/hello-path-variable/{name}")
    public HelloResponse helloPathVariable(@PathVariable String name) {
        return new HelloResponse("Hello %s!".formatted(name));
    }

    @PostMapping("hello")
    public void addMessage(@RequestBody NewMessage message) {
        System.out.println("message = " + message);
    }

    @GetMapping("/hello-header")
    public ResponseEntity<HelloResponse> helloHeader(@RequestHeader("X-Name") String name) {
        return ResponseEntity.ok().body(new HelloResponse("Hello %s!".formatted(name)));
    }

    @GetMapping("/optional")
    public ResponseEntity<HelloResponse> optional() {
        if (ThreadLocalRandom.current().nextBoolean()) {
            return ResponseEntity.of(Optional.of(new HelloResponse("Hello World!")));
        } else {
            return ResponseEntity.of(Optional.empty());
        }
    }

    public record NewMessage(String message) {
    }

    public record HelloResponse(String message) {
    }
}
