package ru.otus.chat;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryAuthenticatedProvider implements AuthenticatedProvider {
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

    private Server server;
    private List<User> users;
    private List<Role> roles;

    public InMemoryAuthenticatedProvider(Server server) {
        this.server = server;
        this.users = new CopyOnWriteArrayList<>();
        this.users.add(new User("admin", "123", "admin"));
        this.users.add(new User("qwe", "qwe", "qwe1"));
        this.users.add(new User("asd", "asd", "asd1"));
        this.users.add(new User("zxc", "zxc", "zxc1"));

        this.roles = new CopyOnWriteArrayList<>();
        roles.add(new Role("admin", RoleType.ADMIN));
        roles.add(new Role("qwe", RoleType.USER));
        roles.add(new Role("asd", RoleType.USER));
        roles.add(new Role("zxc", RoleType.USER));
    }

    @Override
    public void initialize() {
        System.out.println("initialize InMemoryAuthenticatedProvider");
    }

    private String getUsernameByLoginAndPassword(String login, String password) {
        for (User user : users) {
            if (user.login.equals(login) && user.password.equals(password)) {
                return user.username;
            }
        }
        return null;
    }


    private User getUserByUserName(String userName) {
        for (User user : users) {
            if (user.username.equals(userName))
                return user;
        }
        return null;
    }

    private boolean isLoginAlreadyExist(String login) {
        for (User user : users) {
            if (user.login.equals(login)) {
                return true;
            }
        }
        return false;
    }

    private boolean isUsernameAlreadyExist(String username) {
        for (User user : users) {
            if (user.username.equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean authenticate(ClientHandler clientHandler, String login, String password) {
        String authUsername = getUsernameByLoginAndPassword(login, password);
        if (authUsername == null) {
            clientHandler.sendMsg("Некорректный логин/пароль");
            return false;
        }
        if (server.isUsernameBusy(authUsername)) {
            clientHandler.sendMsg("Данная учетная запись уже занята");
            return false;
        }

        clientHandler.setUsername(authUsername);
        server.subscribe(clientHandler);
        clientHandler.sendMsg("/authok " + authUsername);
        return true;
    }

    @Override
    public boolean registration(ClientHandler clientHandler, String login, String password, String username) {
        if (login.trim().length() < 3 || password.trim().length() < 3 || username.trim().length() < 3) {
            clientHandler.sendMsg("Логин 3+ символа, пароль 3+ символа, имя пользователя 3+ символа");
            return false;
        }
        if (isLoginAlreadyExist(login)) {
            clientHandler.sendMsg("Указанный логин уже занят");
            return false;
        }
        if (isUsernameAlreadyExist(username)) {
            clientHandler.sendMsg("Указанное имя пользователя уже занято");
            return false;
        }
        users.add(new User(login, password, username));
        roles.add(new Role(login, RoleType.USER));
        clientHandler.setUsername(username);
        server.subscribe(clientHandler);
        clientHandler.sendMsg("/regok " + username);
        return true;
    }

    @Override
    public boolean isAdmin(ClientHandler clientHandler) {
        User user = getUserByUserName(clientHandler.getUsername());
        if (user == null) {
            return false;
        }
        for (Role role : roles) {
            if (role.login.equals(user.login) && role.roleType == RoleType.ADMIN) {
                return true;
            }
        }
        return false;
    }
}
