package ru.otus.chat;

import ru.otus.chat.queies.RestrictionQuery;

import java.sql.DriverManager;

public class ServerApplication {
    public static void main(String[] args) {
        try {
            new Server(8189).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}