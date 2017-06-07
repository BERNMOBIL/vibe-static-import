package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.entitiy.Schedule;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.ScheduleTestData;
import org.jooq.DSLContext;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StopTimeImportTest {
    @Test
    public void insertQuery() throws Exception {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        StopTimeImport stopTimeImport = new StopTimeImport(null, dslContext, null);
        Insert<Record> insert = stopTimeImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into schedule (id, platform, planned_arrival, planned_departure, stop, journey, update) values (?, ?, ?, ?, ?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        StopTimeImport.SchedulePreparedStatementSetter setter = new StopTimeImport.SchedulePreparedStatementSetter();
        ScheduleTestData testData = new ScheduleTestData();
        Schedule data = testData.get(0);
        setter.setValues(data, ps);
        verify(ps, times(1)).setObject(eq(1), eq(data.getId()));
        verify(ps, times(1)).setString(eq(2), eq(data.getPlatform()));
        verify(ps, times(1)).setTime(eq(3), eq(data.getPlannedArrival()));
        verify(ps, times(1)).setTime(eq(4), eq(data.getPlannedDeparture()));
        verify(ps, times(1)).setObject(eq(5), eq(data.getStop()));
        verify(ps, times(1)).setObject(eq(6), eq(data.getJourney()));
        verify(ps, times(1)).setTimestamp(eq(7), any(Timestamp.class));
    }

}