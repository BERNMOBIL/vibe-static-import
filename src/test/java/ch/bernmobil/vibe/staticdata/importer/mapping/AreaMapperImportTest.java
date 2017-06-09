package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.mapping.AreaMappingTestData;
import org.jooq.DSLContext;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static ch.bernmobil.vibe.staticdata.importer.mapping.AreaMapperImport.AreaMapperPreparedStatementSetter;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class AreaMapperImportTest {

    @Test
    public void insertQuery() {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        AreaMapperImport mapperImport = new AreaMapperImport(null, null, dslContext, null);
        Insert<Record> insert = mapperImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into area_mapper (gtfs_id, id, update) values (?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        UpdateTimestampManager manager = new UpdateTimestampManager();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        manager.setActiveUpdateTimestamp(timestamp);
        AreaMapperPreparedStatementSetter setter = new AreaMapperPreparedStatementSetter(manager);
        AreaMappingTestData testData = new AreaMappingTestData();
        AreaMapping mapping = testData.get(0);
        setter.setValues(mapping, ps);
        verify(ps, times(1)).setString(eq(1), eq(mapping.getGtfsId()));
        verify(ps, times(1)).setObject(eq(2), eq(mapping.getId()));
        verify(ps, times(1)).setTimestamp(eq(3), eq(timestamp));
    }
}