package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
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
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class StopTimeImportTest {
    @Test
    public void insertQuery() throws Exception {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        StopTimeImport stopTimeImport = new StopTimeImport(null, dslContext, null, null);
        Insert<Record> insert = stopTimeImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into schedule (id, platform, planned_arrival, planned_departure, stop, journey, update) values (?, ?, ?, ?, ?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        UpdateTimestampManager manager = new UpdateTimestampManager();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        manager.setActiveUpdateTimestamp(timestamp);
        StopTimeImport.SchedulePreparedStatementSetter setter = new StopTimeImport.SchedulePreparedStatementSetter(manager);
        ScheduleTestData testData = new ScheduleTestData();
        Schedule data = testData.get(0);
        setter.setValues(data, ps);
        verify(ps, times(1)).setObject(eq(1), eq(data.getId()));
        verify(ps, times(1)).setString(eq(2), eq(data.getPlatform()));
        verify(ps, times(1)).setTime(eq(3), eq(data.getPlannedArrival()));
        verify(ps, times(1)).setTime(eq(4), eq(data.getPlannedDeparture()));
        verify(ps, times(1)).setObject(eq(5), eq(data.getStop()));
        verify(ps, times(1)).setObject(eq(6), eq(data.getJourney()));
        verify(ps, times(1)).setTimestamp(eq(7), eq(timestamp));
    }

}