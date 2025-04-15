package ru.otus.chat;

import ru.otus.chat.entities.Role;
import ru.otus.chat.entities.RoleType;
import ru.otus.chat.jdbc.Model;

import java.io.IOException;
import java.sql.SQLException;

public class SomeTestApp {

    public static void main(String[] args) throws IOException, SQLException {
//        String url = "jdbc:postgresql://localhost:54321/chat";
//        String userName = "user";
//        String password = "123";
//
//
//
//        final var connection = DriverManager.getConnection(url, userName, password);
//        final var role = new RoleQuery(connection);
//
//        role.getByUserId(1L);

        final var model = new Model();
        final var role = model.getRole();
        role.getByUserId(1L);
        role.create(new Role(10L, null, RoleType.USER ));

        model.save();

    }
}
