package ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsCalendarDateContract;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsCalendarDate;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Provides mapping between a single row in the "calendar_dates.txt" and {@link GtfsCalendarDate}
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class CalendarDateFieldSetMapper implements FieldSetMapper<GtfsCalendarDate> {

    @Override
    public GtfsCalendarDate mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsCalendarDate calendarDate = new GtfsCalendarDate();
        calendarDate.setServiceId(fieldSet.readString(GtfsCalendarDateContract.SERVICE_ID));
        calendarDate.setDate(fieldSet.readString(GtfsCalendarDateContract.DATE));
        calendarDate.setExceptionType(fieldSet.readString(GtfsCalendarDateContract.EXCEPTION_TYPE));
        return calendarDate;
    }
}
