package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapping;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class RouteMapperHelper extends Mapper<RouteMapping>{
    private final static String QUERY =  "INSERT INTO route_mapper(gtfs_id, id) VALUES(?, ?)";

    public RouteMapperHelper(DataSource dataSource,
            MapperStore<String, RouteMapping> mapperStore) {
        super(dataSource, QUERY, new RouteMapperPreparedStatementSetter(), mapperStore);
    }

    public static class RouteMapperPreparedStatementSetter implements ItemPreparedStatementSetter<RouteMapping> {

        @Override
        public void setValues(RouteMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
        }
    }
}
