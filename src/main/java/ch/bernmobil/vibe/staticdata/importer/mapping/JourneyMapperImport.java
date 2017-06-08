package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.contract.JourneyMapperContract;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
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
 * Class configuring the import for {@link JourneyMapping}.
 */
public class JourneyMapperImport extends MapperImport<JourneyMapping> {
    private final DSLContext dslContext;

    /**
     * Constructor which takes all needed objects to write {@link JourneyMapping} to database.
     * @param dataSource A {@link DataSource} which holds a connection to the mapping database.
     * @param mapperStore A {@link JourneyMapperStore} which contains all {@link JourneyMapping} to be saved.
     * @param dslContext The context to generate SQL queries using JOOQ.
     * @param updateTimestampManager which provides access to the latest timestamp.
     */
    public JourneyMapperImport(DataSource dataSource,
                               @Qualifier("journeyMapperStore") JourneyMapperStore mapperStore,
                               DSLContext dslContext, UpdateTimestampManager updateTimestampManager) {
        super(dataSource, new JourneyMapperPreparedStatementSetter(updateTimestampManager), mapperStore);
        this.dslContext = dslContext;
    }

    /**
     * Builds a query for inserting {@link JourneyMapping} objects into the database. Metadata from {@link JourneyMapperContract} is used
     * to set table name and field names, as well as the required "?" for the {@link PreparedStatement} are added here.
     * @return A JOOQ instance of an insert query.
     */
    @Override
    Insert<Record> insertQuery() {
        Collection<Field<?>> fields = Arrays.stream(JourneyMapperContract.COLUMNS).map(DSL::field).collect(Collectors.toList());
        return dslContext.insertInto(table(JourneyMapperContract.TABLE_NAME), fields)
                .values(Collections.nCopies(JourneyMapperContract.COLUMNS.length, "?"));
    }

    /**
     * Class which implements {@link ItemPreparedStatementSetter} for {@link JourneyMapping}
     */
    public static class JourneyMapperPreparedStatementSetter implements ItemPreparedStatementSetter<JourneyMapping> {
        private final UpdateTimestampManager updateTimestampManager;

        public JourneyMapperPreparedStatementSetter(UpdateTimestampManager updateTimestampManager) {
            this.updateTimestampManager = updateTimestampManager;
        }

        /**
         * Method to fill a {@link PreparedStatement} with values from a {@link JourneyMapping} for saving it into the database.
         * @param item {@link JourneyMapping} which is to be written into the database.
         * @param ps {@link PreparedStatement} from which JDBC will create a query.
         * @throws SQLException If database returns an error, this exception will be thrown.
         */
        @Override
        public void setValues(JourneyMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsTripId());
            ps.setString(2, item.getGtfsServiceId());
            ps.setObject(3, item.getId());
            ps.setTimestamp(4, updateTimestampManager.getActiveUpdateTimestamp());
        }
    }
}
