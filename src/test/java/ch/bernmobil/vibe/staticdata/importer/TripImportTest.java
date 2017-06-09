package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.entity.Journey;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.JourneyTestData;
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
import static org.mockito.Mockito.*;

public class TripImportTest {
    @Test
    public void insertQuery() throws Exception {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        TripImport tripImport = new TripImport(null, dslContext, null, null);
        Insert<Record> insert = tripImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into journey (id, headsign, route, terminal_station, update) values (?, ?, ?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        UpdateTimestampManager manager = new UpdateTimestampManager();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        manager.setActiveUpdateTimestamp(timestamp);
        TripImport.JourneyPreparedStatementSetter setter = new TripImport.JourneyPreparedStatementSetter(manager);
        JourneyTestData testData = new JourneyTestData();
        Journey data = testData.get(0);
        setter.setValues(data, ps);
        verify(ps, times(1)).setObject(eq(1), eq(data.getId()));
        verify(ps, times(1)).setString(eq(2), eq(data.getHeadsign()));
        verify(ps, times(1)).setObject(eq(3), eq(data.getRoute()));
        verify(ps, times(1)).setObject(eq(4), eq(data.getTerminalStation()));
        verify(ps, times(1)).setTimestamp(eq(5), eq(timestamp));
    }
}