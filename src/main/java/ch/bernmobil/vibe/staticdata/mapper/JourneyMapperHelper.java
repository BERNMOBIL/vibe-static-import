package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;

public class JourneyMapperHelper extends Mapper<JourneyMapper>{
    private final static String QUERY =  "INSERT INTO journey_mapper(gtfs_trip_id, gtfs_service_id, id) VALUES(?, ?, ?)";

    public JourneyMapperHelper(DataSource dataSource) {
        super(dataSource, QUERY, new JourneyMapperPreparedStatementSetter());
    }

    @Bean
    @StepScope
    public JourneyBatchReader reader() {
        return new JourneyBatchReader();
    }

    public static class JourneyMapperPreparedStatementSetter implements ItemPreparedStatementSetter<JourneyMapper> {

        @Override
        public void setValues(JourneyMapper item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsTripId());
            ps.setString(2, item.getGtfsServiceId());
            ps.setLong(3, item.getId());
        }
    }

    public static class JourneyBatchReader implements ItemReader<JourneyMapper> {
        private static ListItemReader<JourneyMapper> reader;

        @Override
        public JourneyMapper read() throws Exception {
            if(reader == null) {
                reader = new ListItemReader<>(JourneyMapper.getAll());
            }
            return reader.read();
        }
    }

}
