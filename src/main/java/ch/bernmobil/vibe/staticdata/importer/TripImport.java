package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.entity.Journey;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.TripFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsTrip;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Autowired;

public class TripImport extends Import<GtfsTrip, Journey> {
    private static String[] fieldNames = {"route_id", "service_id", "trip_id", "trip_headsign", "trip_short_name", "direction_id", "block_id", "shape_id"};
    private static String path = "gtfs/trips.txt";
    private static final String insertQuery = "INSERT INTO journey (id, headsign, route) VALUES(?, ?, ?)";

    private static ItemPreparedStatementSetter<Journey> getItemPreparedStatementSetter() {
        return (item, ps) -> {
            ps.setLong(1, item.getId());
            ps.setString(2, item.getHeadsign());
            ps.setLong(3, item.getRoute());
        };
    }
    @Autowired
    public TripImport(DataSource dataSource) {
        super(dataSource, fieldNames, path, new TripFieldSetMapper(), insertQuery, getItemPreparedStatementSetter());
    }
}
