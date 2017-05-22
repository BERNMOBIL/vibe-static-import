package ch.bernmobil.vibe.staticdata.importer;


import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.contract.CalendarDateContract;
import ch.bernmobil.vibe.shared.entitiy.CalendarDate;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.CalendarDateFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.writer.ListUnpackingItemWriter;
import com.google.gson.JsonObject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.postgresql.util.PGobject;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class CalendarDateImport extends Import<GtfsCalendarDate,CalendarDate> {
    private static final String[] FIELD_NAMES = {"service_id", "date", "exception_type"};
    private static final String PATH = "calendar_dates.txt";
    private static final String INSERT_QUERY = new QueryBuilder.PreparedStatement().Insert(
            CalendarDateContract.TABLE_NAME,
            CalendarDateContract.COLUMNS).getQuery();

    public CalendarDateImport(DataSource dataSource, String folder) {
        super(dataSource, FIELD_NAMES, folder + PATH,
                new CalendarDateFieldSetMapper(), INSERT_QUERY,
                new CalendarDatePreparedStatementSetter());
    }

    public ItemWriter<List<CalendarDate>> listItemWriter() {
        return new ListUnpackingItemWriter<>(writer());
    }

    public static class CalendarDatePreparedStatementSetter implements ItemPreparedStatementSetter<CalendarDate> {
        @Override
        public void setValues(CalendarDate item, PreparedStatement ps) throws SQLException {
                ps.setObject(1, item.getId());
                ps.setDate(2, item.getValidFrom());
                ps.setDate(3, item.getValidUntil());
                ps.setObject(4, item.getJourney());
                ps.setObject(5, createPgJson(item.getDays()));
                ps.setTimestamp(6, UpdateManager.getActiveUpdateTimestamp());
        }

        private PGobject createPgJson(JsonObject days) throws SQLException {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(days.toString());
            return jsonObject;
        }
    }

}
