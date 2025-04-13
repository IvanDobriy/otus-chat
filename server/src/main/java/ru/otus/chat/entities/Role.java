package ru.otus.chat.entities;

import ru.otus.chat.InMemoryAuthenticatedProvider;

public class Role {
    private String login;
    private RoleType roleType;

    public Role(String login, RoleType role) {
        this.login = login;
        this.roleType = role;
    }

    public String getLogin() {
        return login;
    }

    public RoleType getRoleType() {
        return roleType;
    }
}