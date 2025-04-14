package ru.otus.chat.jdbc;

import ru.otus.chat.AuthenticatedProvider;
import ru.otus.chat.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;

public class JDBCAuthenticatedProvider implements AuthenticatedProvider {
    private final Model model;

    JDBCAuthenticatedProvider() throws SQLException, IOException {
        model = new Model();
    }

    @Override
    public void initialize() {

    }

    @Override
    public boolean authenticate(ClientHandler clientHandler, String login, String password) {
        return false;
    }

    @Override
    public boolean registration(ClientHandler clientHandler, String login, String password, String username) {
        return false;
    }

    @Override
    public boolean isAdmin(ClientHandler clientHandler) {
        return false;
    }

    @Override
    public boolean kick(String userName) {
        return false;
    }

    @Override
    public boolean isKicked(String userName) {
        return false;
    }
}
