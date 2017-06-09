package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.CalendarDateTestData;
import org.jooq.DSLContext;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CalendarDateImportTest {
    @Test
    public void insertQuery() throws Exception {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        CalendarDateImport calendarDateImport = new CalendarDateImport(null, dslContext, null, null);
        Insert<Record> insert = calendarDateImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into calendar_date (id, valid_from, valid_until, journey, days, update) values (?, ?, ?, ?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        UpdateTimestampManager manager = new UpdateTimestampManager();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        manager.setActiveUpdateTimestamp(timestamp);
        CalendarDateImport.CalendarDatePreparedStatementSetter setter = new CalendarDateImport.CalendarDatePreparedStatementSetter(manager);
        CalendarDateTestData testData = new CalendarDateTestData();
        CalendarDate date = testData.get(0);
        setter.setValues(date, ps);
        verify(ps, times(1)).setObject(eq(1), eq(date.getId()));
        verify(ps, times(1)).setDate(eq(2), eq(date.getValidFrom()));
        verify(ps, times(1)).setDate(eq(3), eq(date.getValidUntil()));
        verify(ps, times(1)).setObject(eq(4), eq(date.getJourney()));
        verify(ps, times(1)).setObject(eq(5), any(PGobject.class));
        verify(ps, times(1)).setTimestamp(eq(6), eq(timestamp));
    }

}