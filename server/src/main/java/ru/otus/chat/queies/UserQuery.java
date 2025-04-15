package ru.otus.chat.queies;

import ru.otus.chat.entities.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserQuery {
    private final Connection connection;
    private final String selectByLoginQuery;
    private final String insertQuery;

    public UserQuery(Connection connection) throws IOException {
        Objects.requireNonNull(connection);
        this.connection = connection;
        selectByLoginQuery = Utils.readQuery("entities/user/select_user_by_login_query.sql");
        insertQuery = Utils.readQuery("entities/user/insert_user_query.sql");
    }

    public Map<Long, User> getUserByLogin(String login) throws SQLException {
        final var result = new HashMap<Long, User>();
        try(PreparedStatement ps = connection.prepareStatement(selectByLoginQuery)){
            ps.setString(1, login);
            final var rs = ps.executeQuery();
            while (rs.next()){
                final var userId = rs.getLong(1);
                final var userLogin = rs.getString(2);
                final var password = rs.getString(3);
                final var userName = rs.getString(4);
                result.put(userId, new User(userId, userLogin, password, userName));
            }
        }
        return result;
    }
}
