package ru.otus.chat.entities;

public enum RestrictionType {
    IS_KICKED(1L);

    private Long id;

    RestrictionType(Long id) {
        this.id = id;
    }

    public static RestrictionType getById(long id){
        if (id == 1L) {
            return IS_KICKED;
        }
        throw new RuntimeException(String.format("Unsupported role type id: %d", id));
    }

    public long getId() {
        return id;
    }
}
