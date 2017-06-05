package ch.bernmobil.vibe.staticdata.importer.mapping;

import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.CalendarDateMapperContract;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
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
 * Class configuring the import for {@link CalendarDateMapping}.
 */
public class CalendarDateMapperImport extends MapperImport<CalendarDateMapping> {
    private final DSLContext dslContext;

    /**
     * Constructor which takes all needed objects to write {@link CalendarDateMapping} to database.
     * @param dataSource A {@link DataSource} which holds a connection to the mapping database.
     * @param mapperStore A {@link MapperStore} which contains all {@link CalendarDateMapping} to be saved.
     * @param dslContext The context to generate SQL queries using JOOQ.
     */
    public CalendarDateMapperImport(DataSource dataSource,
                                    @Qualifier("calendarDateMapperStore") MapperStore<Long, CalendarDateMapping> mapperStore,
                                    DSLContext dslContext) {
        super(dataSource, new CalendarDatePreparedStatementSetter(), mapperStore);
        this.dslContext = dslContext;
    }

    /**
     * Builds a query for inserting {@link CalendarDateMapping} objects into the database. Metadata from
     * {@link CalendarDateMapperContract} is used to set table name and field names, as well as the required "?"
     * for the {@link PreparedStatement} are added here.
     * @return A JOOQ instance of an insert query.
     * @return
     */
    @Override
    Insert<Record> insertQuery() {
        Collection<Field<?>> fields = Arrays.stream(CalendarDateMapperContract.COLUMNS).map(DSL::field).collect(Collectors.toList());
        return dslContext.insertInto(table(CalendarDateMapperContract.TABLE_NAME), fields)
                .values(Collections.nCopies(CalendarDateMapperContract.COLUMNS.length, "?"));
    }

    /**
     * Class which implements {@link ItemPreparedStatementSetter} for {@link CalendarDateMapping}
     */
    public static class CalendarDatePreparedStatementSetter implements ItemPreparedStatementSetter<CalendarDateMapping> {
        /**
         * Method to fill a {@link PreparedStatement} with values from a {@link CalendarDateMapping} for saving it into the database.
         * @param item {@link CalendarDateMapping} which is to be written into the database.
         * @param ps {@link PreparedStatement} from which JDBC will create a query.
         * @throws SQLException If database returns an error, this exception will be thrown.
         */
        @Override
        public void setValues(CalendarDateMapping item, PreparedStatement ps) throws SQLException {
            ps.setLong(1, item.getGtfsId());
            ps.setObject(2, item.getId());
            ps.setTimestamp(3, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
