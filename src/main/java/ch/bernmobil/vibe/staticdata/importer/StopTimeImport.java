package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.contract.ScheduleContract;
import ch.bernmobil.vibe.shared.entitiy.Schedule;
import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsStopTimeContract;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsStopTime;
import ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper.StopTimeFieldSetMapper;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.table;

/**
 * Class to configure the import of a {@link GtfsStopTime}, representing GTFS Calendar Dates, and saving a {@link Schedule}.
 */
public class StopTimeImport extends Import<GtfsStopTime, Schedule> {
    private final DSLContext dslContext;

    /**
     * Constructing an import configuration instance using a {@link DataSource} and a folder with the latest GTFS data.
     * @param dataSource which holds the connection to the static data source.
     * @param dslContext of the JOOQ Query Builder to generate the insert statement.
     * @param folder on the filesystem which contains the latest GTFS data.
     * @param updateTimestampManager which provides access to the latest timestamp.
     */
    public StopTimeImport(DataSource dataSource, DSLContext dslContext, String folder, UpdateTimestampManager updateTimestampManager) {
        super(dataSource, GtfsStopTimeContract.FIELD_NAMES, folder + GtfsStopTimeContract.FILENAME,
                new StopTimeFieldSetMapper(), new SchedulePreparedStatementSetter(updateTimestampManager));
        this.dslContext = dslContext;
    }

    /**
     * Builds a query for inserting {@link Schedule} objects into the database. Metadata from {@link ScheduleContract} is used
     * to set table name and field names, as well as the required "?" for the {@link PreparedStatement} are added here.
     * @return A JOOQ instance of an insert query.
     */
    @Override
    Insert<Record> insertQuery() {
        Collection<Field<?>> fields = Arrays.stream(ScheduleContract.COLUMNS).map(DSL::field).collect(Collectors.toList());
        return dslContext.insertInto(table(ScheduleContract.TABLE_NAME), fields)
                .values(Collections.nCopies(ScheduleContract.COLUMNS.length, "?"));
    }

    /**
     * Class implementing {@link ItemPreparedStatementSetter} to set the prepared statement values in the query
     */
    public static class SchedulePreparedStatementSetter implements ItemPreparedStatementSetter<Schedule> {
        private final UpdateTimestampManager updateTimestampManager;

        public SchedulePreparedStatementSetter(UpdateTimestampManager updateTimestampManager) {
            this.updateTimestampManager = updateTimestampManager;
        }

        /**
         * Set the values of the prepared statement
         * @param item Area which will be safed
         * @param ps {@link PreparedStatement} into these values will be written
         * @throws SQLException Exception will be thrown if the database returns an error
         */
        @Override
        public void setValues(Schedule item, PreparedStatement ps) throws SQLException {
            ps.setObject(1, item.getId());
            ps.setString(2, item.getPlatform());
            ps.setTime(3, item.getPlannedArrival());
            ps.setTime(4, item.getPlannedDeparture());
            ps.setObject(5, item.getStop());
            ps.setObject(6, item.getJourney());
            ps.setTimestamp(7, updateTimestampManager.getActiveUpdateTimestamp());
        }
    }
}
