package ch.bernmobil.vibe.staticdata.mapper;

import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.support.ListItemReader;

public class AreaMapperHelper extends Mapper<AreaMapper>{
    private final static String QUERY =  "INSERT INTO area_mapper(gtfs_id, id) VALUES(?, ?)";

    public AreaMapperHelper(DataSource dataSource) {
        super(dataSource, QUERY, new AreaMapperPreparedStatementSetter());
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
