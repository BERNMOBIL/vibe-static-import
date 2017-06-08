package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.mapping.CalendarDateMappingTestData;
import org.jooq.DSLContext;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static ch.bernmobil.vibe.staticdata.importer.mapping.CalendarDateMapperImport.CalendarDatePreparedStatementSetter;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class CalendarDateMapperImportTest {
    @Test
    public void insertQuery() throws Exception {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        CalendarDateMapperImport mapperImport = new CalendarDateMapperImport(null, null, dslContext, null);
        Insert<Record> query = mapperImport.insertQuery();
        assertThat(query.getSQL(), is("insert into calendar_date_mapper (gtfs_id, id, update) values (?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        UpdateTimestampManager manager = new UpdateTimestampManager();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        manager.setActiveUpdateTimestamp(timestamp);
        CalendarDatePreparedStatementSetter setter = new CalendarDatePreparedStatementSetter(manager);
        CalendarDateMappingTestData testData = new CalendarDateMappingTestData();
        CalendarDateMapping mapping = testData.get(0);
        setter.setValues(mapping, ps);
        verify(ps, times(1)).setLong(eq(1), eq(mapping.getGtfsId()));
        verify(ps, times(1)).setObject(eq(2), eq(mapping.getId()));
        verify(ps, times(1)).setTimestamp(eq(3), eq(timestamp));
    }

}