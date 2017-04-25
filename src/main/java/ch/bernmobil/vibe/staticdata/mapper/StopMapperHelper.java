package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapping;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class StopMapperHelper extends Mapper<StopMapping>{
    private final static String QUERY =  "INSERT INTO stop_mapper(gtfs_id, id) VALUES(?, ?)";

    public StopMapperHelper(DataSource dataSource, MapperStore<String, StopMapping> mapperStore) {
        super(dataSource, QUERY, new StopMapperPreparedStatementSetter(), mapperStore);
    }

    public static class StopMapperPreparedStatementSetter implements ItemPreparedStatementSetter<StopMapping> {

        @Override
        public void setValues(StopMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
        }
    }
}
