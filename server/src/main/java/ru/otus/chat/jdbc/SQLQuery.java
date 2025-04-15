package ru.otus.chat.jdbc;

import java.util.List;
import java.util.Objects;

public class SQLQuery {
    private String query;
    private List<Object> parameters;

    public SQLQuery(String query, QueryType queryType,  List<Object> parameters){
        Objects.requireNonNull(query);
        Objects.requireNonNull(parameters);
        this.query = query;
        this.parameters = parameters;
    }

    public String getQuery() {
        return query;
    }

    public List<Object> getParameters() {
        return parameters;
    }
}
