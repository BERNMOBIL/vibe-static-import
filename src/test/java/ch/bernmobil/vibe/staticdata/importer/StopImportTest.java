package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.entity.Stop;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.StopTestData;
import org.jooq.DSLContext;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class StopImportTest {
    @Test
    public void insertQuery() throws Exception {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        StopImport stopImport = new StopImport(null, dslContext, null, null);
        Insert<Record> insert = stopImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into stop (id, name, area, update) values (?, ?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        UpdateTimestampManager manager = new UpdateTimestampManager();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        manager.setActiveUpdateTimestamp(timestamp);
        StopImport.StopPreparedStatementSetter setter = new StopImport.StopPreparedStatementSetter(manager);
        StopTestData testData = new StopTestData();
        Stop data = testData.get(0);
        setter.setValues(data, ps);
        verify(ps, times(1)).setObject(eq(1), eq(data.getId()));
        verify(ps, times(1)).setString(eq(2), eq(data.getName()));
        verify(ps, times(1)).setObject(eq(3), eq(data.getArea()));
        verify(ps, times(1)).setTimestamp(eq(4), eq(timestamp));
    }
}