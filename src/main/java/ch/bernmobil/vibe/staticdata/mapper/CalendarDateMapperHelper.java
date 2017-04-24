package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.UpdateManager;
import ch.bernmobil.vibe.staticdata.mapper.sync.CalendarDateMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.support.ListItemReader;

public class CalendarDateMapperHelper extends Mapper<CalendarDateMapper>{
    private final static String TABLE_NAME = "calendar_date_mapper";
    private final static String FIELDS[] = {"gtfs_id", "id", "update"};
    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement().Insert(TABLE_NAME, FIELDS).getQuery();

    public CalendarDateMapperHelper(DataSource dataSource) {
        super(dataSource, INSERT_QUERY, new CalendarDatePreparedStatementSetter());
    }

    @Override
    public CalendarDateBatchReader reader() {
        return new CalendarDateBatchReader();
    }

    public static class CalendarDatePreparedStatementSetter implements ItemPreparedStatementSetter<CalendarDateMapper> {

        @Override
        public void setValues(CalendarDateMapper item, PreparedStatement ps) throws SQLException {
            ps.setLong(1, item.getGtfsId());
            ps.setLong(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getLatestUpdateTimestamp());
        }
    }

    public static class CalendarDateBatchReader implements ItemReader<CalendarDateMapper> {
        private static ListItemReader<CalendarDateMapper> reader;

        @Override
        public CalendarDateMapper read() throws Exception {
            if(reader == null) {
                reader = new ListItemReader<>(CalendarDateMapper.getAll());
            }
            return reader.read();
        }
    }

}
