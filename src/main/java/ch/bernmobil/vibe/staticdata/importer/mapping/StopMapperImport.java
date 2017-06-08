package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.contract.StopMapperContract;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.table;

/**
 * Class configuring the import for {@link StopMapping}.
 */
public class StopMapperImport extends MapperImport<StopMapping> {
    private final DSLContext dslContext;

    /**
     * Constructor which takes all needed objects to write {@link StopMapping} to database.
     * @param dataSource A {@link DataSource} which holds a connection to the mapping database.
     * @param mapperStore A {@link MapperStore} which contains all {@link StopMapping} to be saved.
     * @param dslContext The context to generate SQL queries using JOOQ.
     * @param updateTimestampManager which provides access to the latest timestamp.
     */
    public StopMapperImport(DataSource dataSource,
                            @Qualifier("stopMapperStore") MapperStore<String, StopMapping> mapperStore,
                            DSLContext dslContext, UpdateTimestampManager updateTimestampManager) {
        super(dataSource, new StopMapperPreparedStatementSetter(updateTimestampManager), mapperStore);
        this.dslContext = dslContext;
    }

    /**
     * Builds a query for inserting {@link StopMapping} objects into the database. Metadata from {@link StopMapperContract} is used
     * to set table name and field names, as well as the required "?" for the {@link PreparedStatement} are added here.
     * @return A JOOQ instance of an insert query.
     */
    @Override
    Insert<Record> insertQuery() {
        Collection<Field<?>> fields = Arrays.stream(StopMapperContract.COLUMNS).map(DSL::field).collect(Collectors.toList());
        return dslContext.insertInto(table(StopMapperContract.TABLE_NAME), fields)
                .values(Collections.nCopies(StopMapperContract.COLUMNS.length, "?"));
    }

    /**
     * Class which implements {@link ItemPreparedStatementSetter} for {@link StopMapping}
     */
    public static class StopMapperPreparedStatementSetter implements ItemPreparedStatementSetter<StopMapping> {
        private final UpdateTimestampManager updateTimestampManager;

        public StopMapperPreparedStatementSetter(UpdateTimestampManager updateTimestampManager) {
            this.updateTimestampManager = updateTimestampManager;
        }

        /**
         * Method to fill a {@link PreparedStatement} with values from a {@link StopMapping} for saving it into the database.
         * @param item {@link StopMapping} which is to be written into the database.
         * @param ps {@link PreparedStatement} from which JDBC will create a query.
         * @throws SQLException If database returns an error, this exception will be thrown.
         */
        @Override
        public void setValues(StopMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setObject(2, item.getId());
            ps.setTimestamp(3, updateTimestampManager.getActiveUpdateTimestamp());
        }
    }
}
