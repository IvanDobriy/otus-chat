package ru.otus.chat;

import ru.otus.chat.entities.RoleType;
import ru.otus.chat.jdbc.Model;

import java.io.IOException;
import java.sql.SQLException;

public class SomeTestApp {

    public static void main(String[] args) throws IOException, SQLException {
        final var model = new Model();
        final var role = model.getRole();
        role.getByUserId(1L);
        model.save();

    }
}
