package ch.bernmobil.vibe.staticdata.importer.preparedstatementsetter;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.entity.CalendarDate;
import com.google.gson.JsonObject;
import org.postgresql.util.PGobject;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class implementing {@link ItemPreparedStatementSetter} to set the prepared statement values in the query
 */
public class CalendarDatePreparedStatementSetter implements ItemPreparedStatementSetter<CalendarDate> {
    private final UpdateTimestampManager updateTimestampManager;

    public CalendarDatePreparedStatementSetter(UpdateTimestampManager updateTimestampManager) {
        this.updateTimestampManager = updateTimestampManager;
    }

    /**
     * Set the values of the prepared statement
     * @param item Area which will be saved
     * @param ps {@link PreparedStatement} into these values will be written
     * @throws SQLException Exception will be thrown if the database returns an error
     */
    @Override
    public void setValues(CalendarDate item, PreparedStatement ps) throws SQLException {
            ps.setObject(1, item.getId());
            ps.setDate(2, item.getValidFrom());
            ps.setDate(3, item.getValidUntil());
            ps.setObject(4, item.getJourney());
            ps.setObject(5, createPgJson(item.getDays()));
            ps.setTimestamp(6, updateTimestampManager.getActiveUpdateTimestamp());
    }

    /**
     * Method to map the {@link JsonObject} content into a PostgreSQL JSON entity.
     * @param days The object containing a the defined JSON array
     * @return A object holding the JSON data to save it into the database
     * @throws SQLException Exception will be thrown if the database returns an error
     */
    private PGobject createPgJson(JsonObject days) throws SQLException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("json");
        jsonObject.setValue(days.toString());
        return jsonObject;
    }
}
