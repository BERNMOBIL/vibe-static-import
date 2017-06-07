package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.entitiy.Journey;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.JourneyTestData;
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

public class TripImportTest {
    @Test
    public void insertQuery() throws Exception {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        TripImport tripImport = new TripImport(null, dslContext, null);
        Insert<Record> insert = tripImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into journey (id, headsign, route, terminal_station, update) values (?, ?, ?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        TripImport.JourneyPreparedStatementSetter setter = new TripImport.JourneyPreparedStatementSetter();
        JourneyTestData testData = new JourneyTestData();
        Journey data = testData.get(0);
        setter.setValues(data, ps);
        verify(ps, times(1)).setObject(eq(1), eq(data.getId()));
        verify(ps, times(1)).setString(eq(2), eq(data.getHeadsign()));
        verify(ps, times(1)).setObject(eq(3), eq(data.getRoute()));
        verify(ps, times(1)).setObject(eq(4), eq(data.getTerminalStation()));
        verify(ps, times(1)).setTimestamp(eq(5), any(Timestamp.class));
    }
}