package ch.bernmobil.vibe.staticdata.importer;


import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.contract.CalendarDateContract;
import ch.bernmobil.vibe.shared.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsCalendarDateContract;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper.CalendarDateFieldSetMapper;
import ch.bernmobil.vibe.staticdata.writer.ListUnpackingItemWriter;
import com.google.gson.JsonObject;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.postgresql.util.PGobject;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.table;

/**
 * Class to configure the import of a {@link GtfsCalendarDate}, representing GTFS Calendar Dates, and saving a {@link CalendarDate}.
 */
public class CalendarDateImport extends Import<GtfsCalendarDate,CalendarDate> {
    private final DSLContext dslContext;

    /**
     * Constructing an import configuration instance using a {@link DataSource} and a folder with the latest GTFS data.
     * @param dataSource which holds the connection to the static data source.
     * @param dslContext of the JOOQ Query Builder to generate the insert statement.
     * @param folder on the filesystem which contains the latest GTFS data.
     * @param updateTimestampManager which provides access to the latest timestamp.
     */
    public CalendarDateImport(DataSource dataSource, DSLContext dslContext, String folder, UpdateTimestampManager updateTimestampManager) {
        super(dataSource, GtfsCalendarDateContract.FIELD_NAMES, folder + GtfsCalendarDateContract.FILENAME,
                new CalendarDateFieldSetMapper(),
                new CalendarDatePreparedStatementSetter(updateTimestampManager));
        this.dslContext = dslContext;
    }

    /**
     * Builds a query for inserting {@link CalendarDate} objects into the database. Metadata from {@link CalendarDateContract} is used
     * to set table name and field names, as well as the required "?" for the {@link PreparedStatement} are added here.
     * @return A JOOQ instance of an insert query.
     */
    @Override
    Insert<Record> insertQuery() {
        Collection<Field<?>> fields = Arrays.stream(CalendarDateContract.COLUMNS).map(DSL::field).collect(Collectors.toList());
        return dslContext
                .insertInto(table(CalendarDateContract.TABLE_NAME), fields)
                .values(Collections.nCopies(CalendarDateContract.COLUMNS.length, "?"));
    }

    /**
     * The GTFS Calendar Date has a N:M relationship to the GTFS Trip. Because of that the {@link org.springframework.batch.item.ItemReader}
     * will pass a {@link List} of {@link CalendarDate} to the writer. The {@link ItemWriter} processes a {@link List} of
     * the given generic parameter in chunks, hence it expects a {@link CalendarDate}. In this structure, the
     * {@link ItemWriter}cannot flatten a {@link List} of Lists, which would be the result of this N:M relationship.
     * @return An {@link ItemWriter} which writes a {@link List} of {@link CalendarDate}.
     */
    public ItemWriter<List<CalendarDate>> listItemWriter() {
        return new ListUnpackingItemWriter<>(writer());
    }

    /**
     * Class implementing {@link ItemPreparedStatementSetter} to set the prepared statement values in the query
     */
    public static class CalendarDatePreparedStatementSetter implements ItemPreparedStatementSetter<CalendarDate> {
        private final UpdateTimestampManager updateTimestampManager;

        public CalendarDatePreparedStatementSetter(UpdateTimestampManager updateTimestampManager) {
            this.updateTimestampManager = updateTimestampManager;
        }

        /**
         * Set the values of the prepared statement
         * @param item Area which will be saved
         * @param ps {@link PreparedStatement} into these values will be written
         * @throws SQLException Exception will be thrown if the database returns an error
         */
        @Override
        public void setValues(CalendarDate item, PreparedStatement ps) throws SQLException {
                ps.setObject(1, item.getId());
                ps.setDate(2, item.getValidFrom());
                ps.setDate(3, item.getValidUntil());
                ps.setObject(4, item.getJourney());
                ps.setObject(5, createPgJson(item.getDays()));
                ps.setTimestamp(6, updateTimestampManager.getActiveUpdateTimestamp());
        }

        /**
         * Method to map the {@link JsonObject} content into a PostgreSQL JSON entity.
         * @param days The object containing a the defined JSON array
         * @return A object holding the JSON data to save it into the database
         * @throws SQLException Exception will be thrown if the database returns an error
         */
        private PGobject createPgJson(JsonObject days) throws SQLException {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(days.toString());
            return jsonObject;
        }
    }

}
