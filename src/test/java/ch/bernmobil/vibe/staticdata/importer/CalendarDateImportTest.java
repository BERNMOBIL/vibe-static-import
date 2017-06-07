package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.entitiy.CalendarDate;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CalendarDateImportTest {
    @Test
    public void insertQuery() throws Exception {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        CalendarDateImport calendarDateImport = new CalendarDateImport(null, dslContext, null);
        Insert<Record> insert = calendarDateImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into calendar_date (id, valid_from, valid_until, journey, days, update) values (?, ?, ?, ?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        CalendarDateImport.CalendarDatePreparedStatementSetter setter = new CalendarDateImport.CalendarDatePreparedStatementSetter();
        CalendarDateTestData testData = new CalendarDateTestData();
        CalendarDate date = testData.get(0);
        setter.setValues(date, ps);
        verify(ps, times(1)).setObject(eq(1), eq(date.getId()));
        verify(ps, times(1)).setDate(eq(2), eq(date.getValidFrom()));
        verify(ps, times(1)).setDate(eq(3), eq(date.getValidUntil()));
        verify(ps, times(1)).setObject(eq(4), eq(date.getJourney()));
        verify(ps, times(1)).setObject(eq(5), any(PGobject.class));
        verify(ps, times(1)).setTimestamp(eq(6), any(Timestamp.class));
    }

}