package ru.otus.chat.queies;

import ru.otus.chat.entities.Role;
import ru.otus.chat.entities.RoleType;
import ru.otus.chat.jdbc.ModelChangeList;
import ru.otus.chat.jdbc.SQLQuery;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RoleQuery {
    private final Connection connection;
    private final String query;
    private final ModelChangeList changeList;

    public RoleQuery(Connection connection, ModelChangeList changeList) throws IOException {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(connection);
        this.changeList = changeList;
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

    public void create(Role role){
        changeList.add(new SQLQuery("", List.of(role.getId(), role.getRoleType())));
    }
}
