package ru.otus.chat.queies;

import java.sql.Connection;
import java.util.Objects;

public class RestrictionQuery {
    private final Connection connection;
    public RestrictionQuery(Connection connection){
        Objects.requireNonNull(connection);
        this.connection = connection;
    }
    
}
