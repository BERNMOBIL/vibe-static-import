package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entity.CalendarException;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Component
public class CalendarExceptionProcessor extends Processor<GtfsCalendarDate, CalendarException> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private final MapperStore<String, CalendarDateMapping> calendarDateMapper;

    public CalendarExceptionProcessor(@Qualifier("calendarDateMapperStore")MapperStore<String, CalendarDateMapping> calendarDateMapper) {
        this.calendarDateMapper = calendarDateMapper;
    }

    @Override
    public CalendarException process(GtfsCalendarDate item) throws Exception {
        Date date = new Date(dateFormat.parse(item.getDate()).getTime());
        int exceptionType = Integer.parseInt(item.getExceptionType());
        UUID id = idGenerator.getId();
        CalendarDateMapping mapping = calendarDateMapper.getMapping(item.getServiceId());
        UUID calendarDateId = mapping.getId();
        idGenerator.next();
        return new CalendarException(id, date, exceptionType, calendarDateId);
    }
}
