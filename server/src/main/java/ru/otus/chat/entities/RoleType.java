package ru.otus.chat.entities;

public enum RoleType {
    USER(1),
    ADMIN(2);

    private long id;

    RoleType(long id) {
        this.id = id;
    }

    public static RoleType getById(long id) {
        if (id == 1L) {
            return USER;
        }
        if (id == 2L) {
            return ADMIN;
        }
        throw new RuntimeException(String.format("Unsupported role type id: %d", id));
    }

    public long getId() {
        return id;
    }
}

