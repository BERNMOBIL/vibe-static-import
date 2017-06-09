package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.contract.StopContract;
import ch.bernmobil.vibe.shared.entity.Stop;
import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsStopContract;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStop;
import ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper.StopFieldSetMapper;
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
 * Class to configure the import of a {@link GtfsStop}, representing GTFS Calendar Dates, and saving a {@link Stop}.
 */
public class StopImport extends Import<GtfsStop, Stop> {
    private final DSLContext dslContext;

    /**
     * Constructing an import configuration instance using a {@link DataSource} and a folder with the latest GTFS data.
     * @param dataSource which holds the connection to the static data source.
     * @param dslContext  of the JOOQ Query Builder to generate the insert statement.
     * @param folder on the filesystem which contains the latest GTFS data.
     * @param updateTimestampManager which provides access to the latest timestamp.
     */
    public StopImport(DataSource dataSource, DSLContext dslContext, String folder, UpdateTimestampManager updateTimestampManager) {
        super(dataSource, GtfsStopContract.FIELD_NAMES, folder + GtfsStopContract.FILENAME,
                new StopFieldSetMapper(), new StopPreparedStatementSetter(updateTimestampManager));
        this.dslContext = dslContext;
    }

    /**
     * Builds a query for inserting {@link Stop} objects into the database. Metadata from {@link StopContract} is used
     * to set table name and field names, as well as the required "?" for the {@link PreparedStatement} are added here.
     * @return A JOOQ instance of an insert query.
     */
    @Override
    Insert<Record> insertQuery() {
        Collection<Field<?>> fields = Arrays.stream(StopContract.COLUMNS).map(DSL::field).collect(Collectors.toList());
        return dslContext.insertInto(table(StopContract.TABLE_NAME), fields)
                .values(Collections.nCopies(StopContract.COLUMNS.length, "?"));
    }

    /**
     * Class implementing {@link ItemPreparedStatementSetter} to set the prepared statement values in the query
     */
    public static class StopPreparedStatementSetter implements ItemPreparedStatementSetter<Stop> {
        private final UpdateTimestampManager updateTimestampManager;

        public StopPreparedStatementSetter(UpdateTimestampManager updateTimestampManager) {
            this.updateTimestampManager = updateTimestampManager;
        }

        /**
         * Set the values of the prepared statement
         * @param item Area which will be saved
         * @param ps {@link PreparedStatement} into these values will be written
         * @throws SQLException Exception will be thrown if the database returns an error
         */
        @Override
        public void setValues(Stop item, PreparedStatement ps) throws SQLException {
            ps.setObject(1, item.getId());
            ps.setString(2, item.getName());
            ps.setObject(3, item.getArea());
            ps.setTimestamp(4, updateTimestampManager.getActiveUpdateTimestamp());
        }
    }
}
