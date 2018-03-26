package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.contract.CalendarDateContract;
import ch.bernmobil.vibe.shared.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsCalendarContract;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsCalendar;
import ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper.CalendarFieldSetMapper;
import ch.bernmobil.vibe.staticdata.importer.preparedstatementsetter.CalendarDatePreparedStatementSetter;
import ch.bernmobil.vibe.staticdata.writer.ListUnpackingItemWriter;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.batch.item.ItemWriter;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.table;

public class CalendarImport extends Import<GtfsCalendar, CalendarDate> {
    private final DSLContext dslContext;

    public CalendarImport(DataSource dataSource, DSLContext dslContext, String folder, UpdateTimestampManager updateTimestampManager) {
        super(dataSource, GtfsCalendarContract.FIELD_NAMES,
                folder + GtfsCalendarContract.FILENAME,
                new CalendarFieldSetMapper(),
                new CalendarDatePreparedStatementSetter(updateTimestampManager));
        this.dslContext = dslContext;
    }

    @Override
    Insert<Record> insertQuery() {
        Collection<Field<?>> fields = Arrays.stream(CalendarDateContract.COLUMNS).map(DSL::field).collect(Collectors.toList());
        return dslContext
                .insertInto(table(CalendarDateContract.TABLE_NAME), fields)
                .values(Collections.nCopies(CalendarDateContract.COLUMNS.length, "?"));
    }

    public ItemWriter<List<CalendarDate>> listItemWriter() {
        return new ListUnpackingItemWriter<>(writer());
    }
}
