package ru.otus.chat.jdbc;

import ru.otus.chat.AuthenticatedProvider;
import ru.otus.chat.ClientHandler;
import ru.otus.chat.InMemoryAuthenticatedProvider;
import ru.otus.chat.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class JDBCAuthenticatedProvider implements AuthenticatedProvider {
    private class User {
        private String login;
        private String password;
        private String username;

        public User(String login, String password, String username) {
            this.login = login;
            this.password = password;
            this.username = username;
        }
    }

    private enum RoleType {
        USER,
        ADMIN
    }

    private class Role {
        private String login;
        private RoleType roleType;

        public Role(String login, RoleType role) {
            this.login = login;
            this.roleType = role;
        }
    }

    private class Restriction {
        private String login;
        private boolean isKicked;

        public Restriction(String login, boolean isKicked) {
            this.isKicked = isKicked;
            this.login = login;
        }
    }

    private Server server;
    private List<User> users;
    private List<Role> roles;
    private List<Restriction> restrictions;


    private Connection connection;

    JDBCAuthenticatedProvider()  {
        try{
            connection = DriverManager.getConnection("jdbc:/postgresql:/localhost:54321/chat");
        }catch (SQLException e){
            e.printStackTrace();
        }
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
