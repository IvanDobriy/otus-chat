package ru.otus.chat.entities;

public class Role {
    private Long id;
    private String login;
    private RoleType roleType;

    public Role(Long id, String login, RoleType role) {
        this.id = id;
        this.login = login;
        this.roleType = role;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public RoleType getRoleType() {
        return roleType;
    }
}