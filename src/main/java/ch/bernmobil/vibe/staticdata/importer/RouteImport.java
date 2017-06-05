package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.RouteContract;
import ch.bernmobil.vibe.shared.entitiy.Route;
import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsRouteContract;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsRoute;
import ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper.RouteFieldSetMapper;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.table;

/**
 * Class to configure the import of a {@link GtfsRoute}, representing GTFS Calendar Dates, and saving a {@link Route}.
 */
public class RouteImport extends Import<GtfsRoute, Route> {
    private final DSLContext dslContext;

    /**
     * Constructing an import configuration instance using a {@link DataSource} and a folder with the latest GTFS data.
     * @param dataSource DataSource object which holds the connection to the static data source.
     * @param dslContext Object of the JOOQ Query Builder to generate the insert statement.
     * @param folder The folder on the filesystem which contains the latest GTFS data.
     */
    public RouteImport(DataSource dataSource, DSLContext dslContext, String folder) {
        super(dataSource, GtfsRouteContract.FIELD_NAMES, folder + GtfsRouteContract.FILENAME,
                new RouteFieldSetMapper(), new RoutePreparedStatementSetter());
        this.dslContext = dslContext;
    }

    /**
     * Builds a query for inserting {@link Route} objects into the database. Metadata from {@link RouteContract} is used
     * to set table name and field names, as well as the required "?" for the {@link PreparedStatement} are added here.
     * @return A JOOQ instance of an insert query.
     */
    @Override
    Insert<Record> insertQuery() {
        Collection<Field<?>> fields = Arrays.stream(RouteContract.COLUMNS).map(DSL::field).collect(Collectors.toList());
        return dslContext.insertInto(table(RouteContract.TABLE_NAME), fields)
                .values(Collections.nCopies(RouteContract.COLUMNS.length, "?"));
    }

    /**
     * Class implementing {@link ItemPreparedStatementSetter} to set the prepared statement values in the query
     */
    public static class RoutePreparedStatementSetter implements ItemPreparedStatementSetter<Route> {
        /**
         * Set the values of the prepared statement
         * @param item Area which will be safed
         * @param ps {@link PreparedStatement} into these values will be written
         * @throws SQLException Exception will be thrown if the database returns an error
         */
        @Override
        public void setValues(Route item, PreparedStatement ps) throws SQLException {
            ps.setObject(1, item.getId());
            ps.setInt(2, item.getType());
            ps.setString(3, item.getLine());
            ps.setTimestamp(4, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
