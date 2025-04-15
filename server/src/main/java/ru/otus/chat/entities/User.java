package ru.otus.chat.entities;

public class User {
    private Long id;
    private String login;
    private String password;
    private String username;

    public User(Long id, String login, String password, String username) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}