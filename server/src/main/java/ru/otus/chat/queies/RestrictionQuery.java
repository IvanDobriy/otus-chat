package ru.otus.chat.queies;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Objects;

public class RestrictionQuery {
    private final Connection connection;
    private final String query;

    public RestrictionQuery(Connection connection) throws IOException {
        Objects.requireNonNull(connection);
        this.connection = connection;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream("entities/restriction_query.sql")) {
            Objects.requireNonNull(is);
            query = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
