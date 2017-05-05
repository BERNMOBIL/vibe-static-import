package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.UpdateManager;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapping;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Qualifier;

public class RouteMapperHelper extends Mapper<RouteMapping> {

    private final static String TABLE_NAME = "route_mapper";
    private final static String FIELDS[] = {"gtfs_id", "id", "update"};
    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement()
            .Insert(TABLE_NAME, FIELDS).getQuery();

    public RouteMapperHelper(DataSource dataSource,
            @Qualifier("routeMapperStore") MapperStore<String, RouteMapping> mapperStore) {
        super(dataSource, INSERT_QUERY, new RouteMapperPreparedStatementSetter(), mapperStore);
    }

    public static class RouteMapperPreparedStatementSetter implements
            ItemPreparedStatementSetter<RouteMapping> {

        @Override
        public void setValues(RouteMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setObject(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getLatestUpdateTimestamp());
        }
    }
}
