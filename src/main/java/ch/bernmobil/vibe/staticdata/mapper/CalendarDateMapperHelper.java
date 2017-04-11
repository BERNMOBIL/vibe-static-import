package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.mapper.sync.CalendarDateMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;

public class CalendarDateMapperHelper extends Mapper<CalendarDateMapper>{
    private final static String QUERY =  "INSERT INTO calendar_date_mapper(gtfs_id, id) VALUES(?, ?)";

    public CalendarDateMapperHelper(DataSource dataSource) {
        super(dataSource, QUERY, new CalendarDatePreparedStatementSetter());
    }

    @Bean
    @StepScope
    public CalendarDateBatchReader reader() {
        return new CalendarDateBatchReader();
    }

    public static class CalendarDatePreparedStatementSetter implements ItemPreparedStatementSetter<CalendarDateMapper> {

        @Override
        public void setValues(CalendarDateMapper item, PreparedStatement ps) throws SQLException {
            ps.setLong(1, item.getGtfsId());
            ps.setLong(2, item.getId());
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
