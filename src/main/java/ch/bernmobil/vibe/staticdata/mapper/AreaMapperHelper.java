package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapping;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.support.ListItemReader;

public class AreaMapperHelper extends Mapper<AreaMapping>{
    private final static String QUERY =  "INSERT INTO area_mapper(gtfs_id, id) VALUES(?, ?)";
    private MapperStore<String, AreaMapping> mapperStore;

    public AreaMapperHelper(DataSource dataSource, MapperStore<String, AreaMapping> mapperStore) {
        super(dataSource, QUERY, new AreaMapperPreparedStatementSetter());
        this.mapperStore = mapperStore;
    }

    @Override
    public ListItemReader<AreaMapping> reader() {
        return new ListItemReader<>(mapperStore.getMappings());
    }

    public static class AreaMapperPreparedStatementSetter implements ItemPreparedStatementSetter<AreaMapping> {

        @Override
        public void setValues(AreaMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
        }
    }
}
