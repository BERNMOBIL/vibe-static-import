package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.contract.CalendarDateContract;
import ch.bernmobil.vibe.shared.entity.CalendarException;
import ch.bernmobil.vibe.staticdata.gtfs.contract.CalendarExceptionContract;
import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsCalendarDateContract;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper.CalendarDateFieldSetMapper;
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

public class CalendarExceptionImport extends Import<GtfsCalendarDate, CalendarException> {
    private final DSLContext dslContext;

    public CalendarExceptionImport(DataSource dataSource, DSLContext dslContext, String folder, UpdateTimestampManager updateTimestampManager) {
        super(dataSource, GtfsCalendarDateContract.FIELD_NAMES,
                folder + GtfsCalendarDateContract.FILENAME,
                new CalendarDateFieldSetMapper(),
                new CalendarExceptionFieldSetMapper(updateTimestampManager));
        this.dslContext = dslContext;
    }

    @Override
    Insert<Record> insertQuery() {
        Collection<Field<?>> fields = Arrays.stream(CalendarExceptionContract.COLUMNS).map(DSL::field).collect(Collectors.toList());
        return dslContext
                .insertInto(table(CalendarExceptionContract.TABLE_NAME), fields)
                .values(Collections.nCopies(CalendarExceptionContract.COLUMNS.length, "?"));
    }


    private static class CalendarExceptionFieldSetMapper implements ItemPreparedStatementSetter<CalendarException> {
        private final UpdateTimestampManager updateTimestampManager;

        public CalendarExceptionFieldSetMapper(UpdateTimestampManager updateTimestampManager) {
            this.updateTimestampManager = updateTimestampManager;
        }

        @Override
        public void setValues(CalendarException item, PreparedStatement ps) throws SQLException {
            ps.setObject(1, item.getId());
            ps.setDate(2, item.getDate());
            ps.setInt(3, item.getType());
            ps.setObject(4, item.getCalendarDate());
            ps.setTimestamp(5, updateTimestampManager.getActiveUpdateTimestamp());
        }
    }
}
