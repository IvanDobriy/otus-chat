package ru.otus.chat.queies;

import java.sql.Connection;
import java.util.Objects;

public class UserQuery {
    private final Connection connection;
    public UserQuery(Connection connection){
        Objects.requireNonNull(connection);
        this.connection = connection;
    }
}
