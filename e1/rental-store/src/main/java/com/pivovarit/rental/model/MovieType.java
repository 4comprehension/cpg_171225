package com.pivovarit.rental.model;

import java.util.Arrays;

public enum MovieType {
    NEW, REGULAR, OLD;

    public static boolean exists(String type) {
        return Arrays.stream(MovieType.values())
          .map(Enum::name)
          .toList().contains(type);
    }
}
