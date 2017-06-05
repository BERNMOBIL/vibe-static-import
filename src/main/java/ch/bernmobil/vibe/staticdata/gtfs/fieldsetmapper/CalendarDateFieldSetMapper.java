package ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsCalendarDate;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CalendarDateFieldSetMapper implements FieldSetMapper<GtfsCalendarDate> {

    @Override
    public GtfsCalendarDate mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsCalendarDate calendarDate = new GtfsCalendarDate();

        calendarDate.setServiceId(fieldSet.readString("service_id"));
        calendarDate.setDate(fieldSet.readString("date"));
        calendarDate.setExceptionType(fieldSet.readString("exception_type"));

        return calendarDate;
    }
}
