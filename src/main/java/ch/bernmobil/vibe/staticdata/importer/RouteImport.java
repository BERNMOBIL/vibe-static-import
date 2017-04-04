package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.entity.Route;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.RouteFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsRoute;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Autowired;

public class RouteImport extends Import<GtfsRoute, Route> {
    private static String[] fieldNames = {"route_id", "agency_id", "route_short_name", "route_long_name", "route_desc", "route_type", "route_url", "route_color", "route_text_color"};
    private static String path = "gtfs/routes.txt";
    private static final String insertQuery = "INSERT INTO route (id, type) VALUES(?, ?)";

    private static ItemPreparedStatementSetter<Route> getItemPreparedStatementSetter() {
        return (item, ps) -> {
            ps.setLong(1, item.getId());
            ps.setInt(2, item.getType());
        };
    }

    @Autowired
    public RouteImport(DataSource dataSource) {
        super(dataSource, fieldNames, path, new RouteFieldSetMapper(), insertQuery, getItemPreparedStatementSetter());
    }
}
