package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.AreaContract;
import ch.bernmobil.vibe.shared.entitiy.Area;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.StopFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;


public class AreaImport extends Import<GtfsStop, Area> {
    //TODO: contract class for csv field names
    private static final String[] FIELD_NAMES = {"stop_id", "stop_code", "stop_name", "stop_desc", "stop_lat", "stop_lon", "zone_id", "stop_url", "location_type", "parent_station"};
    private static final String PATH = "stops.txt";
    private static final String INSERT_QUERY =
            new QueryBuilder.PreparedStatement().Insert(AreaContract.TABLE_NAME, AreaContract.COLUMNS).getQuery();


    public AreaImport(DataSource dataSource, String folder) {
        super(dataSource, FIELD_NAMES, folder + PATH,
                new StopFieldSetMapper(), INSERT_QUERY, new AreaPreparedStatementSetter());
    }

    public static class AreaPreparedStatementSetter implements ItemPreparedStatementSetter<Area> {

        @Override
        public void setValues(Area item, PreparedStatement ps) throws SQLException {
            ps.setObject(1, item.getId());
            ps.setString(2, item.getName());
            // TODO: inject
            ps.setTimestamp(3, UpdateManager.getActiveUpdateTimestamp());
        }
    }
}
