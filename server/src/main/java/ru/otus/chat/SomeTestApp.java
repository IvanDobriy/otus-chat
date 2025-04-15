package ru.otus.chat;

import ru.otus.chat.entities.RestrictionType;
import ru.otus.chat.jdbc.Model;

import java.io.IOException;
import java.sql.SQLException;

public class SomeTestApp {

    public static void main(String[] args) throws IOException, SQLException {
        final var model = new Model();
        final var userQuery = model.getUser();
        final var restrictionQuery = model.getRestriction();
        final var result = userQuery.getUserByLogin("admin");
        final var restriction = restrictionQuery.getByUserId(1L);
        final var a = restriction;
        restrictionQuery.create(10L, 2L, RestrictionType.IS_KICKED);
        model.save();
//        final var roleQuery = model.getRole();
//        roleQuery.create(10L, 1L, RoleType.USER);
//        userQuery.create(10L, "fff", "fff", "fff1");
//        model.save();
    }
}
