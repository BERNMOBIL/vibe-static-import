package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.entity.Schedule;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.StopTimeFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStopTime;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StopTimeImport extends Import<GtfsStopTime, Schedule> {
    private static String[] fieldNames = {"trip_id" , "arrival_time", "departure_time", "stop_id", "stop_sequence", "stop_headsign", "pickup_type", "drop_off_type", "shape_dist_traveled"};
    private static String path = "stop_times.txt";
    private static final String insertQuery = "INSERT INTO schedule (id, platform, planned_arrival, planned_departure, stop, journey) VALUES(?, ?, ?, ?, ?, ?)";

    public StopTimeImport(DataSource dataSource, String folder) {
        super(dataSource, fieldNames, folder + path, new StopTimeFieldSetMapper(), insertQuery, new SchedulePreparedStatementSetter());
    }

    public static class SchedulePreparedStatementSetter implements ItemPreparedStatementSetter<Schedule> {

        @Override
        public void setValues(Schedule item, PreparedStatement ps) throws SQLException {
            ps.setLong(1, item.getId());
            ps.setString(2, item.getPlatform());
            ps.setTime(3, item.getPlannedArrival());
            ps.setTime(4, item.getPlannedDeparture());
            ps.setLong(5, item.getStop());
            ps.setLong(6, item.getJourney());
        }
    }
}
