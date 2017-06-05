package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.JourneyContract;
import ch.bernmobil.vibe.shared.entitiy.Journey;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.TripFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsTrip;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class TripImport extends Import<GtfsTrip, Journey> {
    private static final String[] FIELD_NAMES = {"route_id", "service_id", "trip_id", "trip_headsign", "trip_short_name", "direction_id", "block_id", "shape_id"};
    private static final String PATH = "trips.txt";
    private static final String INSERT_QUERY = new QueryBuilder.PreparedStatement()
            .Insert(JourneyContract.TABLE_NAME, JourneyContract.COLUMNS).getQuery();

    public TripImport(DataSource dataSource, String folder) {
        super(dataSource, FIELD_NAMES, folder + PATH,
                new TripFieldSetMapper(), INSERT_QUERY, new JourneyPreparedStatementSetter());
    }

    public static class JourneyPreparedStatementSetter implements ItemPreparedStatementSetter<Journey> {
        @Override
        public void setValues(Journey item, PreparedStatement ps) throws SQLException {
            ps.setObject(1, item.getId());
            ps.setString(2, item.getHeadsign());
            ps.setObject(3, item.getRoute());
            ps.setObject(4, item.getTerminalStation());
            ps.setTimestamp(5, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
