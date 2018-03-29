package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entity.CalendarDate;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsCalendar;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.ListMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Component
public class CalendarProcessor extends Processor<GtfsCalendar, List<CalendarDate>> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private final ListMapperStore<String, CalendarDateMapping> calendarDateMapper;
    private final JourneyMapperStore journeyMapperStore;

    @Autowired
    public CalendarProcessor(
            @Qualifier("calendarListMapperStore") ListMapperStore<String, CalendarDateMapping> calendarDateMapper,
            @Qualifier("journeyMapperStore") JourneyMapperStore journeyMapperStore) {
        this.calendarDateMapper = calendarDateMapper;
        this.journeyMapperStore = journeyMapperStore;
    }


    @Override
    public List<CalendarDate> process(GtfsCalendar item) throws Exception {
        List<JourneyMapping> journeyMappings = journeyMapperStore.getMappingsByServiceId(item.getServiceId());

        if(journeyMappings.isEmpty()) {
            return null;
        }

        List<CalendarDate> calendarDates = new ArrayList<>();
        for (JourneyMapping journeyMapping : journeyMappings) {
            Date validFrom = new Date(dateFormat.parse(item.getStartDate()).getTime());
            Date validUntil = new Date(dateFormat.parse(item.getEndDate()).getTime());

            Collection<DayOfWeek> serviceDays = getDaysOfWeek(item);
            JsonObject serviceDaysJson = saveDaysToJson(serviceDays);

            UUID journeyId = journeyMapping.getId();
            UUID id = idGenerator.getId();
            calendarDateMapper.addMapping(item.getServiceId(), new CalendarDateMapping(item.getServiceId(), id));

            idGenerator.next();
            calendarDates.add(new CalendarDate(id, validFrom, validUntil, journeyId, serviceDaysJson));
        }

        return calendarDates;
    }

    private Collection<DayOfWeek> getDaysOfWeek(GtfsCalendar calendar) {
        Collection<DayOfWeek> collection = new ArrayList<>(7);
        if(calendar.getMonday().equals("1")) {
            collection.add(DayOfWeek.MONDAY);
        }
        if(calendar.getTuesday().equals("1")) {
            collection.add(DayOfWeek.TUESDAY);
        }
        if(calendar.getWednsday().equals("1")) {
            collection.add(DayOfWeek.WEDNESDAY);
        }
        if(calendar.getThursday().equals("1")) {
            collection.add(DayOfWeek.THURSDAY);
        }
        if(calendar.getFriday().equals("1")) {
            collection.add(DayOfWeek.FRIDAY);
        }
        if(calendar.getSaturday().equals("1")) {
            collection.add(DayOfWeek.SATURDAY);
        }
        if(calendar.getSunday().equals("1")) {
            collection.add(DayOfWeek.SUNDAY);
        }
        return collection;
    }

    /**
     * Builds a {@link JsonObject} which contains a {@link JsonArray} with all the given days, in the defined format.
     * @param days on which the {@link CalendarDate} instance is valid.
     * @return {@link JsonObject} containing an {@link JsonArray} with all days.
     */
    @SuppressWarnings("Duplicates")
    private JsonObject saveDaysToJson(Collection<DayOfWeek> days) {
        JsonArray list = new JsonArray();
        for (DayOfWeek dayOfWeek : days) {
            list.add(new JsonPrimitive(dayOfWeek.toString()));
        }
        JsonObject json = new JsonObject();
        json.add("service_days", list);
        return json;
    }
}
