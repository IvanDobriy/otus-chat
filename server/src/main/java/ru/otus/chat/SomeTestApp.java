package ru.otus.chat;

import ru.otus.chat.jdbc.Model;

import java.io.IOException;
import java.sql.SQLException;

public class SomeTestApp {

    public static void main(String[] args) throws IOException, SQLException {
        final var model = new Model();
        final var result = model.getUser().getUserByLogin("admin");
        final var a = result;
    }
}
