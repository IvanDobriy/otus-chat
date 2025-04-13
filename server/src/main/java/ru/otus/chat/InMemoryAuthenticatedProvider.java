package ru.otus.chat;

import ru.otus.chat.entities.Restriction;
import ru.otus.chat.entities.Role;
import ru.otus.chat.entities.RoleType;
import ru.otus.chat.entities.User;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryAuthenticatedProvider implements AuthenticatedProvider {
    private Server server;
    private List<User> users;
    private List<Role> roles;
    private List<Restriction> restrictions;


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

        this.restrictions = new CopyOnWriteArrayList<>();
        this.restrictions.add(new Restriction("admin", false));
        this.restrictions.add(new Restriction("qwe", false));
        this.restrictions.add(new Restriction("asd", false));
        this.restrictions.add(new Restriction("zxc", false));
    }

    @Override
    public void initialize() {
        System.out.println("initialize InMemoryAuthenticatedProvider");
    }

    private String getUsernameByLoginAndPassword(String login, String password) {
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                return user.getUsername();
            }
        }
        return null;
    }

    private Restriction getRestrictionByLogin(String login) {
        for (Restriction restriction : restrictions) {
            if (login.equals(restriction.getLogin())) {
                return restriction;
            }
        }
        return null;
    }


    private User getUserByUserName(String userName) {
        for (User user : users) {
            if (user.getUsername().equals(userName))
                return user;
        }
        return null;
    }

    private boolean isLoginAlreadyExist(String login) {
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    private boolean isUsernameAlreadyExist(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
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
        restrictions.add(new Restriction(login, false));
        clientHandler.setUsername(username);
        server.subscribe(clientHandler);
        clientHandler.sendMsg("/regok " + username);
        return true;
    }

    private boolean isAdmin(String login) {
        for (Role role : roles) {
            if (role.getLogin().equals(login) && role.getRoleType() == RoleType.ADMIN) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAdmin(ClientHandler clientHandler) {
        User user = getUserByUserName(clientHandler.getUsername());
        if (user == null) {
            return false;
        }
        return isAdmin(user.getLogin());
    }


    @Override
    public boolean kick(String userName) {
        User user = getUserByUserName(userName);
        if (user == null) {
            return false;
        }
        if (isAdmin(user.getLogin())) {
            return false;
        }
        Restriction restriction = getRestrictionByLogin(user.getLogin());
        if (restriction == null) {
            throw new RuntimeException(String.format("Restriction for user: %s not found", userName));
        }
        restriction.setKicked(true);
        return true;
    }

    @Override
    public boolean isKicked(String userName) {
        User user = getUserByUserName(userName);
        if (user == null) {
            return false;
        }
        Restriction restriction = getRestrictionByLogin(user.getLogin());
        if (restriction == null) {
            throw new RuntimeException(String.format("Restriction for user: %s not found", userName));
        }
        return restriction.isKicked();
    }
}
