package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.UpdateManager;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.CalendarDateMapping;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Qualifier;

public class CalendarDateMapperHelper extends Mapper<CalendarDateMapping>{
    private final static String TABLE_NAME = "calendar_date_mapper";
    private final static String FIELDS[] = {"gtfs_id", "id", "update"};
    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement().Insert(TABLE_NAME, FIELDS).getQuery();

    public CalendarDateMapperHelper(DataSource dataSource,
           @Qualifier("calendarDateMapperStore") MapperStore<Long, CalendarDateMapping> mapperStore) {
        super(dataSource, INSERT_QUERY, new CalendarDatePreparedStatementSetter(), mapperStore);
    }

    public static class CalendarDatePreparedStatementSetter implements ItemPreparedStatementSetter<CalendarDateMapping> {
        @Override
        public void setValues(CalendarDateMapping item, PreparedStatement ps) throws SQLException {
            ps.setLong(1, item.getGtfsId());
            ps.setLong(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getLatestUpdateTimestamp());
        }
    }
}
