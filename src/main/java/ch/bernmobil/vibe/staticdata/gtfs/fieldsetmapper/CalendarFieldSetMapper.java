package ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsCalendarContract;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsCalendar;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CalendarFieldSetMapper implements FieldSetMapper<GtfsCalendar> {
    @Override
    public GtfsCalendar mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsCalendar calendar = new GtfsCalendar();
        calendar.setServiceId(fieldSet.readString(GtfsCalendarContract.SERVICE_ID));
        calendar.setMonday(fieldSet.readString(GtfsCalendarContract.MONDAY));
        calendar.setTuesday(fieldSet.readString(GtfsCalendarContract.TUESDAY));
        calendar.setWednsday(fieldSet.readString(GtfsCalendarContract.WEDNESDAY));
        calendar.setThursday(fieldSet.readString(GtfsCalendarContract.THURSDAY));
        calendar.setFriday(fieldSet.readString(GtfsCalendarContract.FRIDAY));
        calendar.setSaturday(fieldSet.readString(GtfsCalendarContract.SATURDAY));
        calendar.setSunday(fieldSet.readString(GtfsCalendarContract.SUNDAY));
        calendar.setStartDate(fieldSet.readString(GtfsCalendarContract.START_DATE));
        calendar.setEndDate(fieldSet.readString(GtfsCalendarContract.END_DATE));
        return calendar;
    }
}
