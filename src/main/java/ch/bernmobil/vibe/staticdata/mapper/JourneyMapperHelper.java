package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.UpdateManager;
import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.support.ListItemReader;

public class JourneyMapperHelper extends Mapper<JourneyMapper>{
    private final static String TABLE_NAME = "journey_mapper";
    private final static String FIELDS[] = {"gtfs_trip_id", "gtfs_service_id", "id", "update"};
    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement().Insert(TABLE_NAME, FIELDS).getQuery();

    private static final Logger logger = Logger.getLogger(JourneyMapperHelper.class);

    public JourneyMapperHelper(DataSource dataSource) {
        super(dataSource, INSERT_QUERY, new JourneyMapperPreparedStatementSetter());
    }

    @Override
    public JourneyBatchReader reader() {
        return new JourneyBatchReader();
    }

    public static class JourneyMapperPreparedStatementSetter implements ItemPreparedStatementSetter<JourneyMapper> {

        @Override
        public void setValues(JourneyMapper item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsTripId());
            ps.setString(2, item.getGtfsServiceId());
            ps.setLong(3, item.getId());
            ps.setTimestamp(4, UpdateManager.getLatestUpdateTimestamp());
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
