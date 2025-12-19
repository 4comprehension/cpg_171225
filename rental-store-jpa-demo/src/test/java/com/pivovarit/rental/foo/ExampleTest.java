package com.pivovarit.rental.foo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExampleTest {

    private String result;

    @BeforeEach
    void setup() {
        result = "foo";
    }

    @Test
    void shouldTestFoo() {
        Assertions.assertThat(result)
          .isNotEmpty()
          .isEqualTo("foo");
    }

    @AfterEach
    void cleanup() {
        result = null;
        System.out.println("cleaning up after test...");
    }
}
