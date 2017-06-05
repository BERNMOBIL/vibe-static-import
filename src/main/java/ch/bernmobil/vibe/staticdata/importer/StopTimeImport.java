package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.ScheduleContract;
import ch.bernmobil.vibe.shared.entitiy.Schedule;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.StopTimeFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStopTime;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class StopTimeImport extends Import<GtfsStopTime, Schedule> {
    private static final String[] FIELD_NAMES = {"trip_id" , "arrival_time", "departure_time", "stop_id", "stop_sequence", "stop_headsign", "pickup_type", "drop_off_type", "shape_dist_traveled"};
    private static final String PATH = "stop_times.txt";
    private static final String INSERT_QUERY = new QueryBuilder.PreparedStatement()
            .Insert(ScheduleContract.TABLE_NAME, ScheduleContract.COLUMNS).getQuery();

    public StopTimeImport(DataSource dataSource, String folder) {
        super(dataSource, FIELD_NAMES, folder + PATH,
                new StopTimeFieldSetMapper(), INSERT_QUERY, new SchedulePreparedStatementSetter());
    }

    public static class SchedulePreparedStatementSetter implements ItemPreparedStatementSetter<Schedule> {
        @Override
        public void setValues(Schedule item, PreparedStatement ps) throws SQLException {
            ps.setObject(1, item.getId());
            ps.setString(2, item.getPlatform());
            ps.setTime(3, item.getPlannedArrival());
            ps.setTime(4, item.getPlannedDeparture());
            ps.setObject(5, item.getStop());
            ps.setObject(6, item.getJourney());
            ps.setTimestamp(7, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
