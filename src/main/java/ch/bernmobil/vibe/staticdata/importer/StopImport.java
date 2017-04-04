package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.entity.Stop;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.StopFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Autowired;


public class StopImport extends Import<GtfsStop, Stop> {
    private static String[] fieldNames = {"stop_id", "stop_code", "stop_name", "stop_desc", "stop_lat", "stop_lon", "zone_id", "stop_url", "location_type", "parent_station"};
    private static String path = "gtfs/stops.txt";
    private static final String insertQuery = "INSERT INTO stop (id, name, area) VALUES(?, ?, ?)";

    private static ItemPreparedStatementSetter<Stop> getItemPreparedStatementSetter() {
        return (item, ps) -> {
            ps.setLong(1, item.getId());
            ps.setString(2, item.getName());
            ps.setLong(3, item.getArea());
        };
    }

    @Autowired
    public StopImport(DataSource dataSource) {
        super(dataSource, fieldNames, path, new StopFieldSetMapper(), insertQuery, getItemPreparedStatementSetter());
    }
}
