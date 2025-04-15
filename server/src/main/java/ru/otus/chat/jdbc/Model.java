package ru.otus.chat.jdbc;

import ru.otus.chat.queies.RestrictionQuery;
import ru.otus.chat.queies.RoleQuery;
import ru.otus.chat.queies.UserQuery;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Model {
    private final Connection connection;

    private final RestrictionQuery restriction;
    private final RoleQuery role;
    private final UserQuery user;
    private final ModelChangeList changeList;

    public Model() throws SQLException, IOException {
        String url = "jdbc:postgresql://localhost:54321/chat";
        String userName = "user";
        String password = "123";
        connection = DriverManager.getConnection(url, userName, password);
        changeList = new ModelChangeList();
        restriction = new RestrictionQuery(connection);
        role = new RoleQuery(connection, changeList);
        user = new UserQuery(connection, changeList);
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

    public void save() throws SQLException {
        try {
            connection.setAutoCommit(false);
            changeList.forEach((sql) -> {
                try (PreparedStatement ps = connection.prepareStatement(sql.getQuery())) {
                    final var parameters = sql.getParameters();
                    for (int i = 0; i < parameters.size(); i++) {
                        final var parameter = parameters.get(i);
                        if (parameter instanceof Long) {
                            ps.setLong(i + 1, (Long) parameter);
                        } else if (parameter instanceof String) {
                            ps.setString(i + 1, (String) parameter);
                        }
                    }
                    ps.execute();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            connection.commit();
        } finally {
            connection.setAutoCommit(true);
        }
    }


}
