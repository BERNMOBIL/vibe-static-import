package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.mapper.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.CalendarDateMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapping;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CalendarDateProcessor extends Processor<GtfsCalendarDate, List<CalendarDate>> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private final MapperStore<String, CalendarDateMapping> calendarDateMapper;
    private final JourneyMapperStore journeyMapperStore;

    @Autowired
    public CalendarDateProcessor(
            @Qualifier("calendarDateMapperStore") MapperStore<String, CalendarDateMapping> calendarDateMapper,
            @Qualifier("journeyMapperStore") JourneyMapperStore journeyMapperStore) {
        this.calendarDateMapper = calendarDateMapper;
        this.journeyMapperStore = journeyMapperStore;
    }

    @Override
    public List<CalendarDate> process(GtfsCalendarDate item) throws Exception {
        Date validFrom = new Date(dateFormat.parse(item.getDate()).getTime());
        Date validUntil = new Date(dateFormat.parse(item.getDate()).getTime());

        DayOfWeek day = validFrom.toLocalDate().getDayOfWeek();
        JsonObject json = saveDaysToJson(day);

        List<JourneyMapping> journeyMappings = journeyMapperStore.getMappingsByServiceId(item.getServiceId());
        if(journeyMappings.isEmpty()) {
            return null;
        }

        List<CalendarDate> calendarDates = new ArrayList<>();

        for(JourneyMapping journeyMapping : journeyMappings) {
            UUID journeyId = journeyMapping.getId();
            Long gtfsServiceId = Long.parseLong(item.getServiceId());
            String mappingKey = item.getServiceId() + item.getDate();
            UUID id = idGenerator.getId();
            calendarDateMapper.addMapping(mappingKey, new CalendarDateMapping(gtfsServiceId, id));
            idGenerator.next();
            calendarDates.add(new CalendarDate(id, validFrom, validUntil, journeyId, json));
        }

        return calendarDates;
    }
    
    private JsonObject saveDaysToJson(DayOfWeek day) {
        JsonArray list = new JsonArray();
        list.add(new JsonPrimitive(day.toString()));
        JsonObject json = new JsonObject();
        json.add("service_days", list);
        return json;
    }
}