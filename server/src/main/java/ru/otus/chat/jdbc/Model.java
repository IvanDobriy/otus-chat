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

    private final RestrictionQuery restrictionQuery;
    private final RoleQuery roleQuery;
    private final UserQuery userQuery;
    private ModelChangeList changeList;

    public Model() throws SQLException, IOException {
        String url = "jdbc:postgresql://localhost:54321/chat";
        String userName = "user";
        String password = "123";
        connection = DriverManager.getConnection(url, userName, password);
        changeList = new ModelChangeList();
        restrictionQuery = new RestrictionQuery(connection, changeList);
        roleQuery = new RoleQuery(connection, changeList);
        userQuery = new UserQuery(connection, changeList);
    }

    public RestrictionQuery getRestrictionQuery() {
        return restrictionQuery;
    }

    public RoleQuery getRoleQuery() {
        return roleQuery;
    }

    public UserQuery getUserQuery() {
        return userQuery;
    }

    public void save() throws SQLException {
        try {
            connection.setAutoCommit(false);
            final var handledChangeList = changeList;
            changeList = new ModelChangeList();
            handledChangeList.forEach((sql) -> {
                try (PreparedStatement ps = connection.prepareStatement(sql.getQuery())) {
                    final var parameters = sql.getParameters();
                    for (int i = 0; i < parameters.size(); i++) {
                        final var parameter = parameters.get(i);
                        if (parameter instanceof Long) {
                            ps.setLong(i + 1, (Long) parameter);
                        } else if (parameter instanceof String) {
                            ps.setString(i + 1, (String) parameter);
                        } else if (parameter instanceof Boolean) {
                            ps.setBoolean(i + 1, (Boolean) parameter);
                        }else {
                            throw new RuntimeException("Unsupported parameter type");
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
