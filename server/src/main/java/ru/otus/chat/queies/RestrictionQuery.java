package ru.otus.chat.queies;

import ru.otus.chat.entities.Restriction;
import ru.otus.chat.entities.RestrictionType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RestrictionQuery {
    private final Connection connection;
    private final String selectByUserId;

    public RestrictionQuery(Connection connection) throws IOException {
        Objects.requireNonNull(connection);
        this.connection = connection;
        selectByUserId = Utils.readQuery("entities/restriction/select_restriction_query_by_user_id.sql");
    }

    public Map<Long, Restriction> getByUserId(Long userId) throws SQLException {
        final var result = new HashMap<Long, Restriction>();
        try (PreparedStatement ps = connection.prepareStatement(selectByUserId)) {
            ps.setLong(1, userId);
            final var queryResult = ps.executeQuery();
            while (queryResult.next()) {
                final var id = queryResult.getLong(1);
                final var typeId = queryResult.getLong(2);
                final var restrictionType = RestrictionType.getById(typeId);
                result.put(id, new Restriction(id, null, restrictionType == RestrictionType.IS_KICKED));
            }
        }
        return result;
    }
}
