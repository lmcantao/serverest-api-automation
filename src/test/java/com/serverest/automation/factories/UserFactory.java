package com.serverest.automation.factories;

import com.serverest.automation.models.User;
import net.datafaker.Faker;

public final class UserFactory {

    private static final Faker FAKER = new Faker();

    private UserFactory() {
    }

    public static User validUser() {
        return User.builder()
                .nome(FAKER.name().fullName())
                .email(FAKER.internet().emailAddress())
                .password(FAKER.internet().password(8, 16))
                .administrador("true")
                .build();
    }

    public static User emptyUser() {
        return User.builder()
                .build();
    }
}