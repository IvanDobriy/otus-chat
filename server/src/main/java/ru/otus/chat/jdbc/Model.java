package ru.otus.chat.jdbc;

import ru.otus.chat.queies.RestrictionQuery;
import ru.otus.chat.queies.RoleQuery;
import ru.otus.chat.queies.UserQuery;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Model {
    private final Connection connection;

    private final RestrictionQuery restriction;
    private final RoleQuery role;
    private final UserQuery user;

    public Model() throws SQLException, IOException {
        String url = "jdbc:postgresql://localhost:54321/chat";
        String userName = "user";
        String password = "123";
        connection = DriverManager.getConnection(url, userName, password);
        restriction = new RestrictionQuery(connection);
        role = new RoleQuery(connection);
        user = new UserQuery(connection);
    }

    public RestrictionQuery getRestriction() {
        return restriction;
    }

    public RoleQuery getRole() {
        return role;
    }

    public UserQuery getUser() {
        return user;
    }
}
