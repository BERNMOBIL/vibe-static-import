package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.mapping.JourneyMappingTestData;
import org.jooq.DSLContext;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

import static ch.bernmobil.vibe.staticdata.importer.mapping.JourneyMapperImport.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class JourneyMapperImportTest {
    @Test
    public void insertQuery() {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        JourneyMapperImport mapperImport = new JourneyMapperImport(null, null, dslContext);
        Insert<Record> insert = mapperImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into journey_mapper (gtfs_trip_id, gtfs_service_id, id, update) values (?, ?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        JourneyMapperPreparedStatementSetter setter = new JourneyMapperPreparedStatementSetter();
        JourneyMappingTestData testData = new JourneyMappingTestData();
        JourneyMapping mapping = testData.get(0);
        setter.setValues(mapping, ps);
        verify(ps, times(1)).setString(eq(1), eq(mapping.getGtfsTripId()));
        verify(ps, times(1)).setString(eq(2), eq(mapping.getGtfsServiceId()));
        verify(ps, times(1)).setObject(eq(3), eq(mapping.getId()));
        verify(ps, times(1)).setTimestamp(eq(4), any(Timestamp.class));
    }

}