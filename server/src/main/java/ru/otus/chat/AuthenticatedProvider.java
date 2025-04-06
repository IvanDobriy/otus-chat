package ru.otus.chat;

public interface AuthenticatedProvider {
    void initialize();

    boolean authenticate(ClientHandler clientHandler, String login, String password);

    boolean registration(ClientHandler clientHandler, String login, String password, String username);

    boolean isAdmin(ClientHandler clientHandler);

    boolean kick(String userName);

    boolean isKicked(String userName);
}
