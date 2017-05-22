package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.AreaMapperContract;
import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Qualifier;

public class AreaMapperImport extends MapperImport<AreaMapping> {

    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement()
            .Insert(AreaMapperContract.TABLE_NAME, AreaMapperContract.COLUMNS).getQuery();

    public AreaMapperImport(DataSource dataSource,
            @Qualifier("areaMapperStore") MapperStore<String, AreaMapping> mapperStore) {
        super(dataSource, INSERT_QUERY, new AreaMapperPreparedStatementSetter(), mapperStore);
    }

    public static class AreaMapperPreparedStatementSetter implements ItemPreparedStatementSetter<AreaMapping> {
        @Override
        public void setValues(AreaMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setObject(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
