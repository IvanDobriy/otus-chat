package ru.otus.chat.entities;

public enum RestrictionType {
    IS_KICKED(1L);
    private Long id;

    RestrictionType(Long id) {
        this.id = id;
    }
}
