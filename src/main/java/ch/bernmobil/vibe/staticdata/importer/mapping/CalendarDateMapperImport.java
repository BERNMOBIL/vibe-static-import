package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.CalendarDateMapperContract;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Qualifier;

public class CalendarDateMapperImport extends MapperImport<CalendarDateMapping> {

    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement()
            .Insert(CalendarDateMapperContract.TABLE_NAME, CalendarDateMapperContract.COLUMNS).getQuery();

    public CalendarDateMapperImport(DataSource dataSource,
            @Qualifier("calendarDateMapperStore") MapperStore<Long, CalendarDateMapping> mapperStore) {
        super(dataSource, INSERT_QUERY, new CalendarDatePreparedStatementSetter(), mapperStore);
    }

    public static class CalendarDatePreparedStatementSetter implements ItemPreparedStatementSetter<CalendarDateMapping> {
        @Override
        public void setValues(CalendarDateMapping item, PreparedStatement ps) throws SQLException {
            ps.setLong(1, item.getGtfsId());
            ps.setObject(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
