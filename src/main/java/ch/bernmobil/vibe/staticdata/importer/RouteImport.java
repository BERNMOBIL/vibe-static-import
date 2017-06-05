package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.RouteContract;
import ch.bernmobil.vibe.shared.entitiy.Route;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.RouteFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsRoute;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class RouteImport extends Import<GtfsRoute, Route> {
    private static final String[] FIELD_NAMES = {"route_id", "agency_id", "route_short_name", "route_long_name", "route_desc", "route_type", "route_url", "route_color", "route_text_color"};
    private static final String PATH = "routes.txt";
    private static final String INSERT_QUERY = new QueryBuilder.PreparedStatement()
            .Insert(RouteContract.TABLE_NAME, RouteContract.COLUMNS).getQuery();

    public RouteImport(DataSource dataSource, String folder) {
        super(dataSource, FIELD_NAMES, folder + PATH,
                new RouteFieldSetMapper(), INSERT_QUERY, new RoutePreparedStatementSetter());
    }

    public static class RoutePreparedStatementSetter implements ItemPreparedStatementSetter<Route> {
        @Override
        public void setValues(Route item, PreparedStatement ps) throws SQLException {
            ps.setObject(1, item.getId());
            ps.setInt(2, item.getType());
            ps.setString(3, item.getLine());
            ps.setTimestamp(4, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
