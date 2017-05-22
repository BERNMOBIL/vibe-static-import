package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.JourneyMapperContract;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Qualifier;

public class JourneyMapperImport extends MapperImport<JourneyMapping> {

    private final static String INSERT_QUERY = new QueryBuilder.PreparedStatement()
            .Insert(JourneyMapperContract.TABLE_NAME, JourneyMapperContract.COLUMNS).getQuery();


    public JourneyMapperImport(DataSource dataSource,
            @Qualifier("journeyMapperStore") JourneyMapperStore mapperStore) {
        super(dataSource, INSERT_QUERY, new JourneyMapperPreparedStatementSetter(), mapperStore);
    }

    public static class JourneyMapperPreparedStatementSetter implements ItemPreparedStatementSetter<JourneyMapping> {
        @Override
        public void setValues(JourneyMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsTripId());
            ps.setString(2, item.getGtfsServiceId());
            ps.setObject(3, item.getId());
            ps.setTimestamp(4, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
