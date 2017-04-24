package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapping;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class AreaMapperHelper extends Mapper<AreaMapping>{
    private final static String QUERY =  "INSERT INTO area_mapper(gtfs_id, id) VALUES(?, ?)";

    public AreaMapperHelper(DataSource dataSource, MapperStore<String, AreaMapping> mapperStore) {
        super(dataSource, QUERY, new AreaMapperPreparedStatementSetter(), mapperStore);
    }

    public static class AreaMapperPreparedStatementSetter implements ItemPreparedStatementSetter<AreaMapping> {

        @Override
        public void setValues(AreaMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
        }
    }
}
