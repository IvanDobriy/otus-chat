package ru.otus.chat.jdbc;

import ru.otus.chat.queies.RestrictionQuery;
import ru.otus.chat.queies.RoleQuery;
import ru.otus.chat.queies.UserQuery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class Model {
    private final Connection connection;

    private final RestrictionQuery restriction;
    private final RoleQuery role;
    private final UserQuery user;

    public Model(String url) throws SQLException {
        Objects.requireNonNull(url);
        connection = DriverManager.getConnection(url);
        restriction = new RestrictionQuery(connection);
        role = new RoleQuery(connection);
        user = new UserQuery(connection);
    }

}
