package ch.bernmobil.vibe.staticdata;

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
    private static ArrayList<Timestamp> updateHistory;


    @Autowired
    public UpdateManager(@Qualifier("MapperDataSource") DataSource mapperDataSource) {
        jdbcMapperTemplate = new JdbcTemplate(mapperDataSource);
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


}
