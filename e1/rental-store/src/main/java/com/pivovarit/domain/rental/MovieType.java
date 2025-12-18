package com.pivovarit.domain.rental;

import java.util.Arrays;

enum MovieType {
    NEW, REGULAR, OLD;

    public static boolean exists(String type) {
        return Arrays.stream(MovieType.values())
          .map(Enum::name)
          .toList().contains(type);
    }
}
