package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entity.CalendarException;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.ListMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class CalendarExceptionProcessor extends Processor<GtfsCalendarDate, List<CalendarException>> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private final ListMapperStore<String, CalendarDateMapping> calendarDateMapper;

    public CalendarExceptionProcessor(@Qualifier("calendarListMapperStore")ListMapperStore<String, CalendarDateMapping> calendarDateMapper) {
        this.calendarDateMapper = calendarDateMapper;
    }

    @Override
    public List<CalendarException> process(GtfsCalendarDate item) throws Exception {
        Date date = new Date(dateFormat.parse(item.getDate()).getTime());
        int exceptionType = Integer.parseInt(item.getExceptionType());
        Set<CalendarDateMapping> mappings = calendarDateMapper.getMapping(item.getServiceId());
        List<CalendarException> exceptions = new ArrayList<>(mappings.size());
        for (CalendarDateMapping mapping : mappings) {
            UUID calendarDateId = mapping.getId();
            exceptions.add(new CalendarException(idGenerator.next(), date, exceptionType, calendarDateId));
        }

        return exceptions;
    }
}
