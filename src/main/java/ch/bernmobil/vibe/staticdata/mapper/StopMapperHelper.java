package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.UpdateManager;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapping;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class StopMapperHelper extends Mapper<StopMapping>{
    private final static String TABLE_NAME = "stop_mapper";
    private final static String FIELDS[] = {"gtfs_id", "id", "update"};
    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement().Insert(TABLE_NAME, FIELDS).getQuery();

    public StopMapperHelper(DataSource dataSource, MapperStore<String, StopMapping> mapperStore) {
        super(dataSource, INSERT_QUERY, new StopMapperPreparedStatementSetter(), mapperStore);
    }

    public static class StopMapperPreparedStatementSetter implements ItemPreparedStatementSetter<StopMapping> {

        @Override
        public void setValues(StopMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getLatestUpdateTimestamp());
        }
    }
}
