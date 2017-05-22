package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.StopMapperContract;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Qualifier;

public class StopMapperImport extends MapperImport<StopMapping> {
    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement()
            .Insert(StopMapperContract.TABLE_NAME, StopMapperContract.COLUMNS).getQuery();

    public StopMapperImport(DataSource dataSource,
            @Qualifier("stopMapperStore") MapperStore<String, StopMapping> mapperStore) {
        super(dataSource, INSERT_QUERY, new StopMapperPreparedStatementSetter(), mapperStore);
    }

    public static class StopMapperPreparedStatementSetter implements ItemPreparedStatementSetter<StopMapping> {
        @Override
        public void setValues(StopMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setObject(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
