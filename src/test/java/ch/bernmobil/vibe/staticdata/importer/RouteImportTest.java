package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.entitiy.Route;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.RouteTestData;
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

public class RouteImportTest {
    @Test
    public void insertQuery() throws Exception {
        DSLContext dslContext = DSL.using(SQLDialect.POSTGRES);
        RouteImport routeImport = new RouteImport(null, dslContext, null);
        Insert<Record> insert = routeImport.insertQuery();
        assertThat(insert.getSQL(), is("insert into route (id, type, line, update) values (?, ?, ?, ?)"));
    }

    @Test
    public void prepareStatementSetter() throws  Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        RouteImport.RoutePreparedStatementSetter setter = new RouteImport.RoutePreparedStatementSetter();
        RouteTestData testData = new RouteTestData();
        Route data = testData.get(0);
        setter.setValues(data, ps);
        verify(ps, times(1)).setObject(eq(1), eq(data.getId()));
        verify(ps, times(1)).setInt(eq(2), eq(data.getType()));
        verify(ps, times(1)).setString(eq(3), eq(data.getLine()));
        verify(ps, times(1)).setTimestamp(eq(4), any(Timestamp.class));
    }
}