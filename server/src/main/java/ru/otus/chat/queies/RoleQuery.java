package ru.otus.chat.queies;

import ru.otus.chat.entities.Role;
import ru.otus.chat.entities.RoleType;
import ru.otus.chat.jdbc.ModelChangeList;
import ru.otus.chat.jdbc.QueryType;
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
    private final String selectQuery;
    private final String insertQuery;
    private final ModelChangeList changeList;



    public RoleQuery(Connection connection, ModelChangeList changeList) throws IOException {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(connection);
        this.changeList = changeList;
        this.connection = connection;
        selectQuery = Utils.readQuery("entities/role/select_role_query.sql");
        insertQuery = Utils.readQuery("entities/role/insert_role_query.sql");
    }

    public final Map<Long, Role> getByUserId(Long userId) throws SQLException {
        final var result = new HashMap<Long, Role>();
        try (final var ps = connection.prepareStatement(selectQuery)) {
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

    public Role create(Long id, Long userId, RoleType roleType){
        final var role = new Role(id, null, roleType);
        changeList.add(new SQLQuery(insertQuery, QueryType.INSERT, List.of(id, userId, roleType.getId())));
        return role;
    }
}
