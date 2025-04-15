package ru.otus.chat.jdbc;

import ru.otus.chat.*;
import ru.otus.chat.entities.Restriction;
import ru.otus.chat.entities.RestrictionType;
import ru.otus.chat.entities.Role;
import ru.otus.chat.entities.RoleType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class JDBCAuthenticatedProvider implements AuthenticatedProvider {
    private final Model model;
    private Server server;
    private IdGenerator idGenerator;


    public JDBCAuthenticatedProvider(Server server) throws SQLException, IOException {
        Objects.requireNonNull(server);
        idGenerator = new SimpleIdGenerator();
        model = new Model();
        this.server = server;
    }

    @Override
    public void initialize() {

    }

    @Override
    public boolean authenticate(ClientHandler clientHandler, String login, String password) {
        try {
            final var userQuery = model.getUser();
            final var users = userQuery.getUserByLogin(login);
            if (users.isEmpty()) {
                clientHandler.sendMsg("Некорректный логин/пароль");
                return false;
            }
            final var user = users.values().stream().findFirst().get();
            if (server.isUsernameBusy(user.getUsername())) {
                clientHandler.sendMsg("Данная учетная запись уже занята");
                return false;
            }
            clientHandler.setUsername(user.getUsername());
            server.subscribe(clientHandler);
            clientHandler.sendMsg("/authok " + user.getUsername());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean registration(ClientHandler clientHandler, String login, String password, String username) {
        try {
            if (login.trim().length() < 3 || password.trim().length() < 3 || username.trim().length() < 3) {
                clientHandler.sendMsg("Логин 3+ символа, пароль 3+ символа, имя пользователя 3+ символа");
                return false;
            }
            final var userQuery = model.getUser();
            final var usersByLogin = userQuery.getUserByLogin(login);
            if (!usersByLogin.isEmpty()) {
                clientHandler.sendMsg("Указанный логин уже занят");
                return false;
            }
            final var usersByName = userQuery.getUserByName(username);
            if (!usersByName.isEmpty()) {
                clientHandler.sendMsg("Указанное имя пользователя уже занято");
                return false;
            }
            final var user = userQuery.create(idGenerator.getNextId(), login, password, username);
            final var rolesQuery = model.getRole();
            rolesQuery.create(idGenerator.getNextId(), user.getId(), RoleType.USER);
            model.save();
            clientHandler.setUsername(user.getUsername());
            server.subscribe(clientHandler);
            clientHandler.sendMsg("/regok " + username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean isAdmin(ClientHandler clientHandler) {
        try {
            final var userQuery = model.getUser();
            final var rolesQuery = model.getRole();
            final var users = userQuery.getUserByName(clientHandler.getUsername());
            if (users.isEmpty()) {
                return false;
            }
            final var currentUser = users.values().stream().findFirst().get();
            final var roles = rolesQuery.getByUserId(currentUser.getId());
            for (Role role : roles.values()) {
                if (role.getRoleType() == RoleType.ADMIN) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean kick(String userName) {
        try {
            final var usersQuery = model.getUser();
            final var users = usersQuery.getUserByName(userName);
            if (users.isEmpty()) {
                return false;
            }
            final var currentUser = users.values().stream().findFirst().get();
            final var rolesQuery = model.getRole();
            final var roles = rolesQuery.getByUserId(currentUser.getId());
            if (roles.isEmpty()) {
                return false;
            }
            final var isAdmin = roles.values().stream().anyMatch(role -> role.getRoleType() == RoleType.ADMIN);
            if (!isAdmin) {
                return false;
            }
            final var restrictionQuery = model.getRestriction();
            final var restrictions = restrictionQuery.getByUserId(currentUser.getId());
            final var isKicked = restrictions.values().stream().anyMatch(Restriction::isKicked);
            if (isKicked) {
                return true;
            }
            restrictionQuery.create(idGenerator.getNextId(), currentUser.getId(), RestrictionType.IS_KICKED);
            model.save();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean isKicked(String userName) {
        try {
            final var usersQuery = model.getUser();
            final var users = usersQuery.getUserByName(userName);
            if (users.isEmpty()) {
                return false;
            }
            final var currentUser = users.values().stream().findFirst().get();
            final var restrictionsQuery = model.getRestriction();
            final var restrictions = restrictionsQuery.getByUserId(currentUser.getId());
            if (restrictions.isEmpty()) {
                return false;
            }
            return restrictions.values().stream().anyMatch(Restriction::isKicked);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
