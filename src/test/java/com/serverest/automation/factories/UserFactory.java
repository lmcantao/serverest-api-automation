package com.serverest.automation.factories;

import com.serverest.automation.models.User;
import net.datafaker.Faker;

public final class UserFactory {

    private static final Faker faker = new Faker();

    private UserFactory() {
    }

    public static User createDefaultUser() {

        return User.builder()
                .nome(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password("Teste@123")
                .administrador("true")
                .build();

    }

}
