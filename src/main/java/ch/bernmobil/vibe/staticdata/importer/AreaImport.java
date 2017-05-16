package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.shared.entity.java.Area;
import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.UpdateManager;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.StopFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;


public class AreaImport extends Import<GtfsStop, Area> {
    private static final String[] FIELD_NAMES = {"stop_id", "stop_code", "stop_name", "stop_desc", "stop_lat", "stop_lon", "zone_id", "stop_url", "location_type", "parent_station"};
    private static final String PATH = "stops.txt";
    private static final String TABLE_NAME = "area";
    private static final String[] DATABASE_FIELDS = {"id", "name", "update"};
    private static final String INSERT_QUERY = new QueryBuilder.PreparedStatement().Insert(TABLE_NAME, DATABASE_FIELDS).getQuery();


    public AreaImport(DataSource dataSource, String folder) {
        super(dataSource, FIELD_NAMES, folder + PATH, new StopFieldSetMapper(), INSERT_QUERY, new AreaPreparedStatementSetter());
    }

    public static class AreaPreparedStatementSetter implements ItemPreparedStatementSetter<Area> {

        @Override
        public void setValues(Area item, PreparedStatement ps) throws SQLException {
            ps.setObject(1, item.getId());
            ps.setString(2, item.getName());
            // TODO: inject
            ps.setTimestamp(3, UpdateManager.activeUpdateTimestamp);
        }
    }

    public static String getTableName() {
        return TABLE_NAME;
    }
}
