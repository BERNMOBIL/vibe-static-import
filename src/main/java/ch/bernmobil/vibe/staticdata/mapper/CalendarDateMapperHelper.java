package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.CalendarDateMapping;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Qualifier;

public class CalendarDateMapperHelper extends Mapper<CalendarDateMapping>{
    private final static String QUERY =  "INSERT INTO calendar_date_mapper(gtfs_id, id) VALUES(?, ?)";

    public CalendarDateMapperHelper(DataSource dataSource,
                                    @Qualifier("calendarDateMapperStore") MapperStore<Long, CalendarDateMapping> mapperStore) {
        super(dataSource, QUERY, new CalendarDatePreparedStatementSetter(), mapperStore);
    }

    public static class CalendarDatePreparedStatementSetter implements ItemPreparedStatementSetter<CalendarDateMapping> {

        @Override
        public void setValues(CalendarDateMapping item, PreparedStatement ps) throws SQLException {
            ps.setLong(1, item.getGtfsId());
            ps.setLong(2, item.getId());
        }
    }
}
