package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.entity.Journey;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.TripFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsTrip;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class TripImport extends Import<GtfsTrip, Journey> {
    private static final String[] fieldNames = {"route_id", "service_id", "trip_id", "trip_headsign", "trip_short_name", "direction_id", "block_id", "shape_id"};
    private static final String path = "trips.txt";
    private static final String insertQuery = "INSERT INTO journey (id, headsign, route) VALUES(?, ?, ?)";

    public TripImport(DataSource dataSource, String folder) {
        super(dataSource, fieldNames, folder + path, new TripFieldSetMapper(), insertQuery, new JourneyPreparedStatementSetter());
    }

    public static class JourneyPreparedStatementSetter implements ItemPreparedStatementSetter<Journey> {

        @Override
        public void setValues(Journey item, PreparedStatement ps) throws SQLException {
            ps.setLong(1, item.getId());
            ps.setString(2, item.getHeadsign());
            ps.setLong(3, item.getRoute());
        }
    }
}
