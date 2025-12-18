package com.pivovarit.domain.greeting;

public class StaticGreetingRepository implements GreetingRepository {
    @Override
    public String getGreeting() {
        return "hello!";
    }
}
