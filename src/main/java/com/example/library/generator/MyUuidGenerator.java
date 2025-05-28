package com.example.library.generator;

import java.util.UUID;

public class MyUuidGenerator {

    public static String generateUuid() {
        UUID uuid = UUID.randomUUID();

        return uuid.toString();
    }
}
