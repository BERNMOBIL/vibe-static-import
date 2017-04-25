package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.UpdateManager;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapping;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class AreaMapperHelper extends Mapper<AreaMapping>{
    private final static String TABLE_NAME = "area_mapper";
    private final static String FIELDS[] = {"gtfs_id", "id", "update"};
    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement().Insert(TABLE_NAME, FIELDS).getQuery();

    public AreaMapperHelper(DataSource dataSource, MapperStore<String, AreaMapping> mapperStore) {
        super(dataSource, INSERT_QUERY, new AreaMapperPreparedStatementSetter(), mapperStore);
    }

    public static class AreaMapperPreparedStatementSetter implements ItemPreparedStatementSetter<AreaMapping> {

        @Override
        public void setValues(AreaMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getLatestUpdateTimestamp());
        }
    }
}
