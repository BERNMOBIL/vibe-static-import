package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.entitiy.Area;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.AreaTestData;
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

public class AreaImportTest {
    @Test
    public void insertQuery() {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        AreaImport areaImport = new AreaImport(null, dslContext, null);
        Insert<Record> insert = areaImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into area (id, name, update) values (?, ?, ?)"));
    }

    @Test
    public void preparedStatementSetter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        AreaImport.AreaPreparedStatementSetter setter = new AreaImport.AreaPreparedStatementSetter();
        AreaTestData testData = new AreaTestData();
        Area data = testData.get(0);
        setter.setValues(data, ps);
        verify(ps, times(1)).setObject(eq(1), eq(data.getId()));
        verify(ps, times(1)).setString(eq(2), eq(data.getName()));
        verify(ps, times(1)).setTimestamp(eq(3), any(Timestamp.class));
    }

}