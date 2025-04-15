package ru.otus.chat.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    private Connection connection;

    public ConnectionProvider() {
        try {
            String url = "jdbc:postgresql://localhost:54321/chat";
            String userName = "user";
            String password = "123";
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
