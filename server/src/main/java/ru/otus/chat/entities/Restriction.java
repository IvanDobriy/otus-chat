package ru.otus.chat.entities;

public class Restriction {
    private Long id;
    private String login;
    private boolean isKicked;

    public Restriction(Long id, String login, boolean isKicked) {
        this.id = id;
        this.isKicked = isKicked;
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public boolean isKicked() {
        return isKicked;
    }

    public void setKicked(boolean kicked) {
        isKicked = kicked;
    }
}