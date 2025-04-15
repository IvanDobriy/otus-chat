package ru.otus.chat;

import ru.otus.chat.entities.RoleType;
import ru.otus.chat.jdbc.Model;

import java.io.IOException;
import java.sql.SQLException;

public class SomeTestApp {

    public static void main(String[] args) throws IOException, SQLException {
        final var model = new Model();
        final var userQuery = model.getUser();
        final var result = userQuery.getUserByLogin("admin");
        final var roleQuery = model.getRole();
        roleQuery.create(10L, 1L, RoleType.USER);
        userQuery.create(10L, "fff", "fff", "fff1");
        model.save();
    }
}
