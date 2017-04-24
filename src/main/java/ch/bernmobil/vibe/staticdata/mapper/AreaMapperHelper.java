package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.UpdateManager;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.support.ListItemReader;

public class AreaMapperHelper extends Mapper<AreaMapper>{
    private final static String TABLE_NAME = "area_mapper";
    private final static String FIELDS[] = {"gtfs_id", "id", "update"};
    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement().Insert(TABLE_NAME, FIELDS).getQuery();

    public AreaMapperHelper(DataSource dataSource) {
        super(dataSource, INSERT_QUERY, new AreaMapperPreparedStatementSetter());
    }

    @Override
    public AreaBatchReader reader() {
        return new AreaBatchReader();
    }

    public static class AreaMapperPreparedStatementSetter implements ItemPreparedStatementSetter<AreaMapper> {

        @Override
        public void setValues(AreaMapper item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getLatestUpdateTimestamp());
        }
    }

    public static class AreaBatchReader implements ItemReader<AreaMapper> {
        private static ListItemReader<AreaMapper> reader;

        @Override
        public AreaMapper read() throws Exception{
            if(reader == null) {
                reader = new ListItemReader<>(AreaMapper.getAll());
            }
            return reader.read();
        }
    }
}
