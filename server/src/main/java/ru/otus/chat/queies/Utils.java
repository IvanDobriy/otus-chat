package ru.otus.chat.queies;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Utils {
    public static String readQuery(String resourcePath) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(resourcePath)) {
            Objects.requireNonNull(is);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
