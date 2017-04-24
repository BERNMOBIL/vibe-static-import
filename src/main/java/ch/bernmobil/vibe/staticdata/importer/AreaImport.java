package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.entity.Area;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.StopFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;


public class AreaImport extends Import<GtfsStop, Area> {
    private static final String[] fieldNames = {"stop_id", "stop_code", "stop_name", "stop_desc", "stop_lat", "stop_lon", "zone_id", "stop_url", "location_type", "parent_station"};
    private static final String path = "stops.txt";
    private static final String insertQuery = "INSERT INTO area (id, name) VALUES(?, ?)";


    public AreaImport(DataSource dataSource, String folder) {
        super(dataSource, fieldNames, folder + path, new StopFieldSetMapper(), insertQuery, new AreaPreparedStatementSetter());
    }

    public static class AreaPreparedStatementSetter implements ItemPreparedStatementSetter<Area> {

        @Override
        public void setValues(Area item, PreparedStatement ps) throws SQLException {
            ps.setLong(1, item.getId());
            ps.setString(2, item.getName());
        }
    }

}
