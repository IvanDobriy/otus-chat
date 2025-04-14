package ru.otus.chat.entities;

public enum RoleType {
    USER(1),
    ADMIN(2);

    private long id;

    RoleType(long id) {
        this.id = id;
    }
}

