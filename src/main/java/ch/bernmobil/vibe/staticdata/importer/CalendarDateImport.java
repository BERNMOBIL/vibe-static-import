package ch.bernmobil.vibe.staticdata.importer;


import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.contract.CalendarDateContract;
import ch.bernmobil.vibe.shared.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsCalendarDateContract;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper.CalendarDateFieldSetMapper;
import ch.bernmobil.vibe.staticdata.importer.preparedstatementsetter.CalendarDatePreparedStatementSetter;
import ch.bernmobil.vibe.staticdata.writer.ListUnpackingItemWriter;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.batch.item.ItemWriter;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
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

}
