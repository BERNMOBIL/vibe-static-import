package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.QueryBuilder;
import ch.bernmobil.vibe.staticdata.UpdateManager;
import ch.bernmobil.vibe.staticdata.entity.Stop;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.StopFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;


public class StopImport extends Import<GtfsStop, Stop> {
    private static final String[] FIELD_NAMES = {"stop_id", "stop_code", "stop_name", "stop_desc", "stop_lat", "stop_lon", "zone_id", "stop_url", "location_type", "parent_station"};
    private static final String PATH = "stops.txt";
    private static final String TABLE_NAME = "stop";
    private static final String[] DATABASE_FIELDS = {"id", "name", "area", "update"};
    private static final String INSERT_QUERY = new QueryBuilder.PreparedStatement().Insert(TABLE_NAME, DATABASE_FIELDS).getQuery();



    public StopImport(DataSource dataSource, String folder) {
        super(dataSource, FIELD_NAMES, folder + PATH, new StopFieldSetMapper(), INSERT_QUERY, new StopPreparedStatementSetter());
    }

    public static class StopPreparedStatementSetter implements ItemPreparedStatementSetter<Stop> {

        @Override
        public void setValues(Stop item, PreparedStatement ps) throws SQLException {
            ps.setObject(1, item.getId());
            ps.setString(2, item.getName());
            ps.setObject(3, item.getArea());
            ps.setTimestamp(4, UpdateManager.getLatestUpdateTimestamp());
        }
    }

    public static String getTableName() {
        return TABLE_NAME;
    }
}
