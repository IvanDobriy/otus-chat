package ru.otus.chat.entities;

public class Restriction {
    private String login;
    private boolean isKicked;

    public Restriction(String login, boolean isKicked) {
        this.isKicked = isKicked;
        this.login = login;
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