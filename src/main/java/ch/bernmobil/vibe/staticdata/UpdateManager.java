package ch.bernmobil.vibe.staticdata;

import ch.bernmobil.vibe.staticdata.QueryBuilder.Predicate;
import ch.bernmobil.vibe.staticdata.QueryBuilder.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UpdateManager {

    private final JdbcTemplate jdbcMapperTemplate;
    private final JdbcTemplate jdbcVibeTemplate;
    private static ArrayList<Timestamp> updateHistory;
    private static final String[] TABLES_TO_DELETE = {"schedule", "calendar_date", "calendar_exception", "journey", "route", "stop", "area"};
    private static final String[] MAPPING_TABLES_TO_DELETE = {"area_mapper", "calendar_date_mapper", "journey_mapper", "route_mapper", "stop_mapper"};
    private static final int UPDATE_HISTORY_LENGTH = 2;

    @Autowired
    public UpdateManager(
            @Qualifier("MapperDataSource") DataSource mapperDataSource,
            @Qualifier("PostgresDataSource") DataSource postgresDataSource) {
        jdbcMapperTemplate = new JdbcTemplate(mapperDataSource);
        jdbcVibeTemplate = new JdbcTemplate(postgresDataSource);
        if (updateHistory == null) {
            loadUpdateHistory();
        }
    }

    public void createUpdateTimestamp() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String query = new PreparedStatement().Insert("update_history", "time").getQuery();
        jdbcMapperTemplate.update(query, new Object[]{now}, new int[]{Types.TIMESTAMP});
        updateHistory.add(now);
    }

    //TODO Split up update manager and inject a proxy object containing a history object which lazy loads the updates if needed
    public static Timestamp getLatestUpdateTimestamp() {
        if (updateHistory.isEmpty()) {
            return new Timestamp(0);
        }
        return getLatestUpdateTimestamp(1)[0];
    }

    public static Timestamp[] getLatestUpdateTimestamp(int number) {
        return updateHistory
                .stream()
                //TODO: could be converted to localtime to use comparator instead of ternay function
                .sorted((t1, t2) -> t1.after(t2) ? -1 : 1)
                .limit(number)
                .toArray(Timestamp[]::new);
    }

    public void cleanOldData() {
        if (updateHistory == null) {
            loadUpdateHistory();
        }
        Timestamp[] lastUpdates = getLatestUpdateTimestamp(UPDATE_HISTORY_LENGTH);

        jdbcVibeTemplate.update(new QueryBuilder().truncate("schedule_update").getQuery());

        deleteByUpdateTimestamp(TABLES_TO_DELETE, lastUpdates, jdbcVibeTemplate);
        deleteByUpdateTimestamp(MAPPING_TABLES_TO_DELETE, lastUpdates, jdbcMapperTemplate);
    }

    public void repairFailedUpdate() {
        Timestamp failedUpdateTimestamp = getLatestUpdateTimestamp();
        deleteByUpdateTimestamp("update_history", failedUpdateTimestamp, jdbcMapperTemplate,
                "time");
        deleteByUpdateTimestamp(TABLES_TO_DELETE, failedUpdateTimestamp, jdbcVibeTemplate);
        deleteByUpdateTimestamp(MAPPING_TABLES_TO_DELETE, failedUpdateTimestamp,
                jdbcMapperTemplate);
    }

    private void deleteByUpdateTimestamp(String table, Timestamp[] lastUpdates,
            JdbcTemplate jdbcTemplate) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        for (Timestamp timestamp : lastUpdates) {
            predicates.add(Predicate.notEquals("update", "'" + timestamp + "'"));
        }
        Predicate predicate = Predicate.joinAnd(predicates);
        jdbcTemplate.update(
                new QueryBuilder()
                        .delete(table)
                        .where(predicate)
                        .getQuery()
        );
    }

    private void deleteByUpdateTimestamp(String table, Timestamp updateTimestamp,
            JdbcTemplate jdbcTemplate, String timestampColumn) {
        jdbcTemplate.update(
                new QueryBuilder()
                        .delete(table)
                        .where(Predicate.equals(timestampColumn, "'" + updateTimestamp + "'"))
                        .getQuery()
        );
    }

    private void deleteByUpdateTimestamp(String[] tables, Timestamp updateTimestamp,
            JdbcTemplate jdbcTemplate) {
        for (String table : tables) {
            deleteByUpdateTimestamp(table, updateTimestamp, jdbcTemplate, "update");
        }
    }

    private void deleteByUpdateTimestamp(String[] tables, Timestamp[] lastUpdates,
            JdbcTemplate jdbcTemplate) {
        for (String table : tables) {
            deleteByUpdateTimestamp(table, lastUpdates, jdbcTemplate);
        }
    }

    public void loadUpdateHistory() {
        updateHistory = new ArrayList<>();
        String query = new QueryBuilder().select("update_history").getQuery();
        List<Map<String, Object>> rows = jdbcMapperTemplate.queryForList(query);
        for (Map row : rows) {
            Timestamp timestamp = (Timestamp) row.get("time");
            updateHistory.add(timestamp);
        }
    }
}
