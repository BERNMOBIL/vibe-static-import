package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.entity.Route;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.RouteFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsRoute;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class RouteImport extends Import<GtfsRoute, Route> {
    private static final String[] fieldNames = {"route_id", "agency_id", "route_short_name", "route_long_name", "route_desc", "route_type", "route_url", "route_color", "route_text_color"};
    private static final String path = "routes.txt";
    private static final String insertQuery = "INSERT INTO route (id, type) VALUES(?, ?)";


    public RouteImport(DataSource dataSource, String folder) {
        super(dataSource, fieldNames, folder + path, new RouteFieldSetMapper(), insertQuery, new RoutePreparedStatementSetter());
    }

    public static class RoutePreparedStatementSetter implements ItemPreparedStatementSetter<Route> {

        @Override
        public void setValues(Route item, PreparedStatement ps) throws SQLException {
            ps.setLong(1, item.getId());
            ps.setInt(2, item.getType());
        }
    }
}
