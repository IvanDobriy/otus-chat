package ru.otus.chat.queies;

import java.sql.Connection;
import java.util.Objects;

public class RoleQuery {
    private final Connection connection;
    public RoleQuery(Connection connection){
        Objects.requireNonNull(connection);
        this.connection = connection;
    }
}
