package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.support.ListItemReader;

public class RouteMapperHelper extends Mapper<RouteMapper>{
    private final static String QUERY =  "INSERT INTO route_mapper(gtfs_id, id) VALUES(?, ?)";

    public RouteMapperHelper(DataSource dataSource) {
        super(dataSource, QUERY, new RouteMapperPreparedStatementSetter());
    }

    @Override
    public RouteBatchReader reader() {
        return new RouteBatchReader();
    }

    public static class RouteMapperPreparedStatementSetter implements ItemPreparedStatementSetter<RouteMapper> {

        @Override
        public void setValues(RouteMapper item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
        }
    }

    public static class RouteBatchReader implements ItemReader<RouteMapper> {
        private static ListItemReader<RouteMapper> reader;

        @Override
        public RouteMapper read() throws Exception{
            if(reader == null) {
                reader = new ListItemReader<>(RouteMapper.getAll());
            }
            return reader.read();
        }
    }
}
