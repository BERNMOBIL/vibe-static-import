package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.RouteMapperContract;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
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
 * Class configuring the import for {@link RouteMapping}.
 */
public class RouteMapperImport extends MapperImport<RouteMapping> {
    private final DSLContext dslContext;

    /**
     * Constructor which takes all needed objects to write {@link RouteMapping} to database.
     * @param dataSource A {@link DataSource} which holds a connection to the mapping database.
     * @param mapperStore A {@link MapperStore} which contains all {@link RouteMapping} to be saved.
     * @param dslContext The context to generate SQL queries using JOOQ.
     */
    public RouteMapperImport(DataSource dataSource,
                             @Qualifier("routeMapperStore") MapperStore<String, RouteMapping> mapperStore,
                             DSLContext dslContext) {
        super(dataSource, new RouteMapperPreparedStatementSetter(), mapperStore);
        this.dslContext = dslContext;
    }

    /**
     * Builds a query for inserting {@link RouteMapping} objects into the database. Metadata from {@link RouteMapperContract} is used
     * to set table name and field names, as well as the required "?" for the {@link PreparedStatement} are added here.
     * @return A JOOQ instance of an insert query.
     */
    @Override
    Insert<Record> insertQuery() {
        Collection<Field<?>> fields = Arrays.stream(RouteMapperContract.COLUMNS).map(DSL::field).collect(Collectors.toList());
        return dslContext.insertInto(table(RouteMapperContract.TABLE_NAME), fields)
                .values(Collections.nCopies(RouteMapperContract.COLUMNS.length, "?"));
    }

    /**
     * Class which implements {@link ItemPreparedStatementSetter} for {@link RouteMapping}
     */
    public static class RouteMapperPreparedStatementSetter implements ItemPreparedStatementSetter<RouteMapping> {
        /**
         * Method to fill a {@link PreparedStatement} with values from a {@link RouteMapping} for saving it into the database.
         * @param item {@link RouteMapping} which is to be written into the database.
         * @param ps {@link PreparedStatement} from which JDBC will create a query.
         * @throws SQLException If database returns an error, this exception will be thrown.
         */
        @Override
        public void setValues(RouteMapping item, PreparedStatement ps) throws SQLException {
            ps.setString(1, item.getGtfsId());
            ps.setObject(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
