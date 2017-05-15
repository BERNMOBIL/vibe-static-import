package ch.bernmobil.vibe.staticdata;

import static java.util.stream.Collectors.toList;

import ch.bernmobil.vibe.staticdata.QueryBuilder.Predicate;
import ch.bernmobil.vibe.staticdata.entity.UpdateHistory;
import ch.bernmobil.vibe.staticdata.repository.UpdateHistoryRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UpdateManager {
    private final JdbcTemplate jdbcMapperTemplate;
    private final JdbcTemplate jdbcVibeTemplate;
    private static final String[] TABLES_TO_DELETE = {"schedule", "calendar_date", "calendar_exception", "journey", "route", "stop", "area"};
    private static final String[] MAPPING_TABLES_TO_DELETE = {"area_mapper", "calendar_date_mapper", "journey_mapper", "route_mapper", "stop_mapper"};
    //TODO: Extract to config file
    private static final int UPDATE_HISTORY_LENGTH = 2;
    private static final long UPDATE_TIMEOUT_MILLISECONDS = 30 * 60 * 1000;
    private final UpdateHistoryRepository updateHistoryRepository;
    public static Timestamp activeUpdateTimestamp;
    public enum Status {IN_PROGRESS, SUCCESS, FAILED}


    @Autowired
    public UpdateManager(
        @Qualifier("MapperDataSource") DataSource mapperDataSource,
        @Qualifier("PostgresDataSource") DataSource postgresDataSource,
        UpdateHistoryRepository updateHistoryRepository) {
        jdbcMapperTemplate = new JdbcTemplate(mapperDataSource);
        jdbcVibeTemplate = new JdbcTemplate(postgresDataSource);
        this.updateHistoryRepository = updateHistoryRepository;
    }

    public void startUpdate() {
        activeUpdateTimestamp = new Timestamp(System.currentTimeMillis());
        UpdateHistory newEntry = new UpdateHistory(activeUpdateTimestamp, Status.IN_PROGRESS.toString());
        updateHistoryRepository.insert(newEntry);
    }

    public void cleanOldData() {
        List<UpdateHistory> latestUpdates = updateHistoryRepository.findLatestNUpdates(UPDATE_HISTORY_LENGTH);
        List<Timestamp> latestUpdatesTimestamps = latestUpdates.stream().map(u -> u.getTime()).collect(toList());
        jdbcVibeTemplate.update(new QueryBuilder().truncate("schedule_update").getQuery());
        deleteByUpdateTimestamp(TABLES_TO_DELETE, latestUpdatesTimestamps, jdbcVibeTemplate);
        deleteByUpdateTimestamp(MAPPING_TABLES_TO_DELETE, latestUpdatesTimestamps, jdbcMapperTemplate);
    }

    public void repairFailedUpdate() {
        Timestamp failedUpdateTimestamp = UpdateManager.activeUpdateTimestamp;
        deleteByUpdateTimestamp(TABLES_TO_DELETE, failedUpdateTimestamp, jdbcVibeTemplate);
        deleteByUpdateTimestamp(MAPPING_TABLES_TO_DELETE, failedUpdateTimestamp, jdbcMapperTemplate);
    }

    private void deleteByUpdateTimestamp(String table, List<Timestamp> lastUpdates, JdbcTemplate jdbcTemplate) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        for (Timestamp timestamp : lastUpdates) {
            predicates.add(Predicate.notEquals("update", "'" + timestamp + "'"));
        }
        Predicate predicate = Predicate.joinAnd(predicates);
        jdbcTemplate.update(new QueryBuilder()
                        .delete(table)
                        .where(predicate)
                        .getQuery());
    }

    private void deleteByUpdateTimestamp(String table, Timestamp updateTimestamp,
        JdbcTemplate jdbcTemplate, String timestampColumn) {
        jdbcTemplate.update(new QueryBuilder()
                        .delete(table)
                        .where(Predicate.equals(timestampColumn, "'" + updateTimestamp + "'"))
                        .getQuery());
    }

    private void deleteByUpdateTimestamp(String[] tables, Timestamp updateTimestamp, JdbcTemplate jdbcTemplate) {
        for (String table : tables) {
            deleteByUpdateTimestamp(table, updateTimestamp, jdbcTemplate, "update");
        }
    }

    private void deleteByUpdateTimestamp(String[] tables, List<Timestamp> lastUpdates,
            JdbcTemplate jdbcTemplate) {
        for (String table : tables) {
            deleteByUpdateTimestamp(table, lastUpdates, jdbcTemplate);
        }
    }

    public boolean hasUpdateCollision() {
        UpdateHistory lastUpdate = updateHistoryRepository.findLastUpdate();
        if(lastUpdate != null && lastUpdate.getStatus().equals(Status.IN_PROGRESS.toString())) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            long timeDiff = now.getTime() - lastUpdate.getTime().getTime();
            if(timeDiff > UPDATE_TIMEOUT_MILLISECONDS) {
                setStatus(Status.FAILED, lastUpdate.getTime());
                deleteByUpdateTimestamp(TABLES_TO_DELETE, lastUpdate.getTime(), jdbcVibeTemplate);
                deleteByUpdateTimestamp(MAPPING_TABLES_TO_DELETE, lastUpdate.getTime(), jdbcMapperTemplate);
                return false;
            }
            return true;
        }
        return false;
    }

    public void setStatus(Status status, Timestamp timestamp) {
        String query = "UPDATE update_history SET status = '" + status.toString() + "' WHERE time = '" + timestamp + "'";
        jdbcVibeTemplate.update(query);
    }

    public void setStatus(Status status) {
        setStatus(status, activeUpdateTimestamp);
    }


}
