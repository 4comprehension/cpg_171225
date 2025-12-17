package com.pivovarit.rental.exception;

public class UserCannotRentException extends RuntimeException {

    public UserCannotRentException(String login) {
        super("user: " + login + " can't rent more movies");
    }
}
