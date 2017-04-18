package ch.bernmobil.vibe.staticdata.importer;


import ch.bernmobil.vibe.staticdata.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.CalendarDateFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsCalendarDate;
import javax.sql.DataSource;

import com.google.gson.JsonObject;
import org.postgresql.util.PGobject;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CalendarDateImport extends Import<GtfsCalendarDate, CalendarDate> {
    private static String[] fieldNames = {"service_id", "date", "exception_type"};
    private static String path = "calendar_dates.txt";
    private static final String insertQuery = "INSERT INTO calendar_date (id, valid_from, valid_until, journey, days) VALUES(?, ?, ?, ?, ?)";

    public CalendarDateImport(DataSource dataSource, String folder) {
        super(dataSource, fieldNames, folder + path, new CalendarDateFieldSetMapper(), insertQuery, new CalendarDatePreparedStatementSetter());
    }

    public static class CalendarDatePreparedStatementSetter implements ItemPreparedStatementSetter<CalendarDate> {

        @Override
        public void setValues(CalendarDate item, PreparedStatement ps) throws SQLException {
            ps.setLong(1, item.getId());
            ps.setDate(2, item.getValidFrom());
            ps.setDate(3, item.getValidUntil());
            ps.setLong(4, item.getJourney());
            ps.setObject(5, createPgJson(item.getDays()));
        }

        private PGobject createPgJson(JsonObject days) throws SQLException {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(days.toString());
            return jsonObject;
        }
    }
}
