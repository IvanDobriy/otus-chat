package ru.otus.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Socket socket;
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;

    private String username;
    private boolean authenticated;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                System.out.println("Клиент подключился");
                //цикл аутентификации
                while (true) {
                    sendMsg("Перед работой с чатом необходимо выполнить аутентификацию " +
                            "/auth login password \n" +
                            "или регистрацию /reg login password username");
                    String message = in.readUTF();
                    if (message.startsWith("/")) {
                        if (message.equals("/exit")) {
                            sendMsg("/exitok");
                            break;
                        }
                        if (message.startsWith("/auth ")) {
                            String[] elements = message.split(" ");
                            if (elements.length != 3) {
                                sendMsg("Неверный формат команды /auth ");
                                continue;
                            }
                            if (server.getAuthenticatedProvider().authenticate(
                                    this, elements[1], elements[2])) {
                                authenticated = true;
                                break;
                            }
                        }
                        if (message.startsWith("/reg ")) {
                            String[] elements = message.split(" ");
                            if (elements.length != 4) {
                                sendMsg("Неверный формат команды /reg ");
                                continue;
                            }
                            if (server.getAuthenticatedProvider().registration(
                                    this, elements[1], elements[2], elements[3])) {
                                authenticated = true;
                                break;
                            }
                        }
                    }
                }

                //цикл работы
                while (authenticated) {
                    String message = in.readUTF();
                    if (message.startsWith("/")) {
                        if (message.equals("/exit")) {
                            sendMsg("/exitok");
                            break;
                        }
                        if (message.startsWith("/w")) {
                            String[] messageParts = message.split(" ", 3);
                            if (messageParts.length < 3) {
                                sendMsg("/w_error unsupported command, use: /w username msg");
                                continue;
                            }
                            String userName = messageParts[1];
                            String msg = messageParts[2];
                            ClientHandler clientHandler = server.findClientByUserName(userName);
                            if (clientHandler == null) {
                                sendMsg(String.format("client with user name: '%s' not fond", userName));
                                continue;
                            }
                            clientHandler.sendMsg(this.username + ": " + msg);
                            continue;
                        }
                        if (message.contains("/kick")) {
                            final String[] msgParts = message.split(" ", 2);
                            if (msgParts.length < 2 || msgParts[1].isBlank()) {
                                sendMsg("/kick_err unsupported message format");
                                continue;
                            }
                            final var userName = msgParts[1];
                            final var authProvider = server.getAuthenticatedProvider();
                            boolean isAdmin = authProvider.isAdmin(this);
                            if (isAdmin) {
                                if (authProvider.kick(userName)) {
                                    sendMsg("/kick_ok " + userName);
                                } else {
                                    sendMsg("/kick_err can`t kick current user");
                                }
                            } else {
                                sendMsg("/kick_err only admin can kick user");
                            }
                        }
                    } else {
                        if (server.getAuthenticatedProvider().isKicked(this.username)) {
                            sendMsg("Current user is kicked");
                            continue;
                        }
                        server.broadcastMessage(username + ": " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }).start();
    }

    public void sendMsg(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void disconnect() {
        server.unsubscribe(this);
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
