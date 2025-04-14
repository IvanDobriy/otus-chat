package ru.otus.chat.queies;

import ru.otus.chat.entities.Role;
import ru.otus.chat.entities.RoleType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RoleQuery {
    private final Connection connection;
    private final String query;

    public RoleQuery(Connection connection) throws IOException {
        Objects.requireNonNull(connection);
        this.connection = connection;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream("entities/role_query.sql")) {
            Objects.requireNonNull(is);
            query = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public final Map<Long, Role> getByUserId(Long userId) throws SQLException {
        final var result = new HashMap<Long, Role>();
        try (final var ps = connection.prepareStatement(query)) {
            ps.setLong(1, userId);
            final var queryResult = ps.executeQuery();
            while (queryResult.next()) {
                final var usersRolesId = queryResult.getLong(1);
                final var roleTypeId = queryResult.getLong(2);
                result.put(usersRolesId, new Role(usersRolesId, null, RoleType.getById(roleTypeId)));
            }
        }
        return result;
    }
}
