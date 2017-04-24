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
    private final String[] TABLES_TO_DELETE = {"schedule", "calendar_date", "calendar_exception", "journey", "route", "stop", "area"};


    @Autowired
    public UpdateManager(
        @Qualifier("MapperDataSource") DataSource mapperDataSource,
        @Qualifier("PostgresDataSource") DataSource postgresDataSource
    ) {
        jdbcMapperTemplate = new JdbcTemplate(mapperDataSource);
        jdbcVibeTemplate = new JdbcTemplate(postgresDataSource);
        if(updateHistory == null) {
            loadUpdateHistory();
        }
    }

    public void createUpdateTimestamp() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String query = new PreparedStatement().Insert("update_history", "time").getQuery();
        jdbcMapperTemplate.update(query, new Object[]{now}, new int[]{Types.TIMESTAMP});
        updateHistory.add(now);
    }

    public static Timestamp getLatestUpdateTimestamp() {
        if (updateHistory.isEmpty()) {
            return new Timestamp(0);
        }

        return updateHistory
            .stream()
            .max((t1, t2) -> t1.after(t2) ? 1 : -1)
            .get();
    }

     public void loadUpdateHistory() {
        String query = new QueryBuilder().Select("update_history").getQuery();
        List<Map<String, Object>> rows = jdbcMapperTemplate.queryForList(query);
        updateHistory = new ArrayList<>();
        for (Map row : rows) {
            Timestamp timestamp = (Timestamp) row.get("time");
            updateHistory.add(timestamp);
        }
    }

    public void cleanOldData() {
        if(updateHistory == null) {
            loadUpdateHistory();
        }

        Timestamp[] lastUpdates = updateHistory
            .stream()
            .sorted((t1, t2) -> t1.after(t2) ? -1 : 1)
            .limit(2)
            .toArray(size -> new Timestamp[size]);

        ArrayList<Predicate> predicates = new ArrayList<>();

        for(Timestamp timestamp : lastUpdates) {
            predicates.add(Predicate.notEquals("update", "'" + timestamp + "'"));
        }
        Predicate predicate = Predicate.joinAnd(predicates);

        jdbcVibeTemplate.update("TRUNCATE schedule_update");

        for(String tableName : TABLES_TO_DELETE) {
            String query = new QueryBuilder()
                .Delete(tableName)
                .Where(predicate)
                .getQuery();

            jdbcVibeTemplate.update(query);
        }
    }
}
