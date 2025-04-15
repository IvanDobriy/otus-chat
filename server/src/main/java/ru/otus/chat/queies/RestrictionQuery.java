package ru.otus.chat.queies;

import ru.otus.chat.entities.Restriction;
import ru.otus.chat.entities.RestrictionType;
import ru.otus.chat.jdbc.ModelChangeList;
import ru.otus.chat.jdbc.QueryType;
import ru.otus.chat.jdbc.SQLQuery;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RestrictionQuery {
    private final Connection connection;
    private final String selectByUserId;
    private final String insertQuery;
    private final String deleteByIdQuery;
    private final ModelChangeList changeList;


    public RestrictionQuery(Connection connection, ModelChangeList changeList) throws IOException {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(changeList);
        this.changeList = changeList;
        this.connection = connection;
        selectByUserId = Utils.readQuery("entities/restriction/select_restriction_query_by_user_id.sql");
        insertQuery = Utils.readQuery("entities/restriction/insert_restriction_query.sql");
        deleteByIdQuery = Utils.readQuery("entities/restriction/delete_restriction_by_id_query.sql");
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

    public Restriction create(Long id, Long userId, RestrictionType restrictionType){
        final var restriction = new Restriction(id, null, restrictionType == RestrictionType.IS_KICKED);
        changeList.add(new SQLQuery(insertQuery, QueryType.INSERT, List.of(id, userId, restrictionType.getId())));
        return restriction;
    }
    public void deleteById(Long id){
        changeList.add(new SQLQuery(deleteByIdQuery, QueryType.DELETE, List.of(id)));
    }
}
