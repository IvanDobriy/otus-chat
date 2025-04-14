package ru.otus.chat;

import java.security.SecureRandom;
import java.util.Objects;

public class SimpleIdGenerator implements IdGenerator {
    private final SecureRandom secureRandom;

    public SimpleIdGenerator() {
        secureRandom = new SecureRandom();
    }

    public SimpleIdGenerator(SecureRandom secureRandom) {
        Objects.requireNonNull(secureRandom);
        this.secureRandom = secureRandom;
    }

    public Long getNextId() {
        return secureRandom.nextLong();
    }
}
