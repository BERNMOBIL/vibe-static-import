package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.mapping.StopMappingTestData;
import org.jooq.DSLContext;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

import static ch.bernmobil.vibe.staticdata.importer.mapping.StopMapperImport.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StopMapperImportTest {
    @Test
    public void insertQuery() {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        StopMapperImport mapperImport = new StopMapperImport(null, null, dslContext);
        Insert<Record> insert = mapperImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into stop_mapper (gtfs_id, id, update) values (?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        StopMapperPreparedStatementSetter setter = new StopMapperPreparedStatementSetter();
        StopMappingTestData testData = new StopMappingTestData();
        StopMapping mapping = testData.get(0);
        setter.setValues(mapping, ps);
        verify(ps, times(1)).setString(eq(1), eq(mapping.getGtfsId()));
        verify(ps, times(1)).setObject(eq(2), eq(mapping.getId()));
        verify(ps, times(1)).setTimestamp(eq(3), any(Timestamp.class));
    }

}