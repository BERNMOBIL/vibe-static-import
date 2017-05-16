package ch.bernmobil.vibe.staticdata.repository;

import ch.bernmobil.vibe.shared.entity.java.UpdateHistory;
import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.QueryBuilder.Predicate;
import ch.bernmobil.vibe.staticdata.QueryBuilder.PreparedStatement;
import ch.bernmobil.vibe.staticdata.UpdateManager.Status;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UpdateHistoryRepository {
    private final JdbcTemplate jdbcTemplate;
    private final String TABLE = "update_history";

    public UpdateHistoryRepository(@Qualifier("PostgresDataSource")DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<UpdateHistory> findAll() {
        List<UpdateHistory> result = new ArrayList<>();
        String query = new QueryBuilder().select(TABLE).getQuery();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        for (Map row : rows) {
            int id = (int) row.get("id");
            Timestamp timestamp = (Timestamp) row.get("time");
            String status = (String) row.get("status");
            result.add(new UpdateHistory(id, timestamp, status));
        }

        return result;
    }

    public UpdateHistory findLastSuccessUpdate() {
        Predicate successStatus = Predicate.equals("status", "'"+ Status.SUCCESS +"'");
        String query = new QueryBuilder()
            .select(TABLE)
            .where(successStatus)
            .orderby("time DESC")
            .limit("1")
            .getQuery();


        return queryForObject(query);
    }

    public UpdateHistory findLastUpdate() {
        String query = new QueryBuilder()
            .select(TABLE)
            .orderby("time DESC")
            .limit("1")
            .getQuery();


        return queryForObject(query);
    }

    public List<UpdateHistory> findLatestNUpdates(int num) {
        String query = new QueryBuilder()
            .select(TABLE)
            .orderby("time DESC")
            .limit(String.format("%d", num))
            .getQuery();

        return queryForList(query);
    }

    private List<UpdateHistory> queryForList(String query) {
        List<UpdateHistory> resultList = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        for(Map<String, Object> row : rows) {
            int id = (int) row.get("id");
            Timestamp time = (Timestamp) row.get("time");
            String status = (String) row.get("status");
            resultList.add(new UpdateHistory(id, time, status));
        }
        return resultList;
    }

    private UpdateHistory queryForObject(String query) {
        UpdateHistory updateHistory;
        try {
            updateHistory = (UpdateHistory) jdbcTemplate.queryForObject(query, new UpdateRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return updateHistory;
    }

    public void insert(Timestamp timestamp) {
        String query = new PreparedStatement().Insert("update_history", "time").getQuery();
        jdbcTemplate.update(query, new Object[]{timestamp}, new int[]{Types.TIMESTAMP});
    }

    public void insert(UpdateHistory updateHistory) {
        String query = new PreparedStatement().Insert("update_history", "time", "status").getQuery();
        jdbcTemplate.update(query, new Object[]{updateHistory.getTime(), updateHistory.getStatus()},
            new int[]{Types.TIMESTAMP, Types.VARCHAR});
    }

    public void update(UpdateHistory lastUpdate) {

    }

    private class UpdateRowMapper implements RowMapper
    {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            int id = rs.getInt("id");
            Timestamp time = rs.getTimestamp("time");
            String status = rs.getString("status");
            return new UpdateHistory(id, time, status);
        }
    }
}
