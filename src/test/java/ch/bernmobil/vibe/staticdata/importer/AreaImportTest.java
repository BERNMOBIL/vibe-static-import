package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.entity.Area;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.AreaTestData;
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

public class AreaImportTest {
    @Test
    public void insertQuery() {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        AreaImport areaImport = new AreaImport(null, dslContext, null, null);
        Insert<Record> insert = areaImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into area (id, name, update) values (?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        UpdateTimestampManager manager = new UpdateTimestampManager();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        manager.setActiveUpdateTimestamp(timestamp);
        AreaImport.AreaPreparedStatementSetter setter = new AreaImport.AreaPreparedStatementSetter(manager);
        AreaTestData testData = new AreaTestData();
        Area data = testData.get(0);
        setter.setValues(data, ps);
        verify(ps, times(1)).setObject(eq(1), eq(data.getId()));
        verify(ps, times(1)).setString(eq(2), eq(data.getName()));
        verify(ps, times(1)).setTimestamp(eq(3), eq(timestamp));
    }

}