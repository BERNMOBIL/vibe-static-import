package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.UpdateManager;
import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.support.ListItemReader;

public class StopMapperHelper extends Mapper<StopMapper>{
    private final static String TABLE_NAME = "stop_mapper";
    private final static String FIELDS[] = {"gtfs_id", "id", "update"};
    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement().Insert(TABLE_NAME, FIELDS).getQuery();

    public StopMapperHelper(DataSource dataSource) {
        super(dataSource, INSERT_QUERY, new StopMapperPreparedStatementSetter());
    }

    @Override
    public StopBatchReader reader() {
        return new StopBatchReader();
    }

    public static class StopMapperPreparedStatementSetter implements ItemPreparedStatementSetter<StopMapper> {

        @Override
        public void setValues(StopMapper item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getLatestUpdateTimestamp());
        }
    }

    public static class StopBatchReader implements ItemReader<StopMapper> {
        private static ListItemReader<StopMapper> reader;

        @Override
        public StopMapper read() throws Exception{
            if(reader == null) {
                reader = new ListItemReader<>(StopMapper.getAll());
            }
            return reader.read();
        }
    }
}
