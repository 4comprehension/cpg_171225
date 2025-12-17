package com.pivovarit.greeting;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomizedGreetingRepository implements GreetingRepository {

    private static final List<String> GREETINGS = List.of(
      "Hello!",
      "Hi there!",
      "Hey!",
      "Greetings!",
      "Good day!",
      "Welcome!",
      "Nice to see you!",
      "Howdy!",
      "Ahoy!",
      "Salutations!"
    );

    @Override
    public String getGreeting() {
        int index = ThreadLocalRandom.current().nextInt(GREETINGS.size());
        return GREETINGS.get(index);
    }
}

