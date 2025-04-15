package ru.otus.chat.queies;

import ru.otus.chat.entities.Role;
import ru.otus.chat.entities.User;
import ru.otus.chat.jdbc.ModelChangeList;
import ru.otus.chat.jdbc.QueryType;
import ru.otus.chat.jdbc.SQLQuery;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserQuery {
    private final Connection connection;
    private final String selectByLoginQuery;
    private final String selectByUserNameQuery;
    private final String insertQuery;
    private final ModelChangeList changeList;


    public UserQuery(Connection connection, ModelChangeList changeList) throws IOException {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(changeList);
        this.connection = connection;
        this.changeList = changeList;
        selectByLoginQuery = Utils.readQuery("entities/user/select_user_by_login_query.sql");
        insertQuery = Utils.readQuery("entities/user/insert_user_query.sql");
        selectByUserNameQuery = Utils.readQuery("entities/user/select_user_by_user_name_query.sql");
    }

    public Map<Long, User> getUserByLogin(String login) throws SQLException {
        final var result = new HashMap<Long, User>();
        try (PreparedStatement ps = connection.prepareStatement(selectByLoginQuery)) {
            ps.setString(1, login);
            final var rs = ps.executeQuery();
            while (rs.next()) {
                final var userId = rs.getLong(1);
                final var userLogin = rs.getString(2);
                final var password = rs.getString(3);
                final var userName = rs.getString(4);
                result.put(userId, new User(userId, userLogin, password, userName));
            }
        }
        return result;
    }

    public Map<Long, User> getUserByName(String name) throws SQLException {
        final var result = new HashMap<Long, User>();
        try (PreparedStatement ps = connection.prepareStatement(selectByUserNameQuery)) {
            ps.setString(1, name);
            final var rs = ps.executeQuery();
            while (rs.next()) {
                final var userId = rs.getLong(1);
                final var userLogin = rs.getString(2);
                final var password = rs.getString(3);
                final var userName = rs.getString(4);
                result.put(userId, new User(userId, userLogin, password, userName));
            }
        }
        return result;
    }

    public User create(Long id, String login, String password, String userName) {
        final var user = new User(id, login, password, userName);
        changeList.add(new SQLQuery(insertQuery, QueryType.INSERT, List.of(id, login, password, userName)));
        return user;
    }
}
