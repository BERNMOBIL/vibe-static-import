package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.mapper.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapping;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Qualifier;

public class JourneyMapperHelper extends Mapper<JourneyMapping>{
    private final static String QUERY =  "INSERT INTO journey_mapper(gtfs_trip_id, gtfs_service_id, id) VALUES(?, ?, ?)";
    private JourneyMapperStore mapperStore;

    public JourneyMapperHelper(DataSource dataSource,
                               @Qualifier("journeyMapperStore")JourneyMapperStore mapperStore) {
        super(dataSource, QUERY, new JourneyMapperPreparedStatementSetter(), mapperStore);
    }

    public static class JourneyMapperPreparedStatementSetter implements ItemPreparedStatementSetter<JourneyMapping> {

        @Override
        public void setValues(JourneyMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsTripId());
            ps.setString(2, item.getGtfsServiceId());
            ps.setLong(3, item.getId());
        }
    }
}
