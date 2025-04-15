package ru.otus.chat.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ModelChangeList implements Iterable<SQLQuery> {
    private final List<SQLQuery> changes;

    public ModelChangeList() {
        changes = new ArrayList<>();
    }

    public void add(SQLQuery sqlQuery) {
        changes.add(sqlQuery);
    }

    @Override
    public Iterator<SQLQuery> iterator() {
        return changes.iterator();
    }

    @Override
    public void forEach(Consumer<? super SQLQuery> action) {
        changes.forEach(action);
    }
}
