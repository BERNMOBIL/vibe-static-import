package ch.bernmobil.vibe.staticdata.importer;


import ch.bernmobil.vibe.staticdata.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.CalendarDateFieldSetMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsCalendarDate;
import javax.sql.DataSource;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Autowired;

public class CalendarDateImport extends Import<GtfsCalendarDate, CalendarDate> {
    static String[] fieldNames = {"service_id", "date", "exception_type"};
    static String path = "gtfs/calendar_dates.txt";
    private static final String insertQuery = "INSERT INTO calendar_date (id, valid_from, valid_until, journey) VALUES(?, ?, ?, ?)";

    private static ItemPreparedStatementSetter<CalendarDate> getItemPreparedStatementSetter() {
        return (item, ps) -> {
            ps.setLong(1, item.getId());
            ps.setDate(2, item.getValidFrom());
            ps.setDate(3, item.getValidUntil());
            ps.setLong(4, item.getJourney());
        };
    }

    @Autowired
    public CalendarDateImport(DataSource dataSource) {
        super(dataSource, fieldNames, path, new CalendarDateFieldSetMapper(), insertQuery, getItemPreparedStatementSetter());
    }
}
