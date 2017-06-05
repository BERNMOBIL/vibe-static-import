package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.RouteMapperContract;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Qualifier;

public class RouteMapperImport extends MapperImport<RouteMapping> {
    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement()
            .Insert(RouteMapperContract.TABLE_NAME, RouteMapperContract.COLUMNS).getQuery();

    public RouteMapperImport(DataSource dataSource,
            @Qualifier("routeMapperStore") MapperStore<String, RouteMapping> mapperStore) {
        super(dataSource, INSERT_QUERY, new RouteMapperPreparedStatementSetter(), mapperStore);
    }

    public static class RouteMapperPreparedStatementSetter implements ItemPreparedStatementSetter<RouteMapping> {
        @Override
        public void setValues(RouteMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setObject(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
