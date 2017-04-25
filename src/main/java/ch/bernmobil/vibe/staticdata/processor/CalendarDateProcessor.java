package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.importer.CalendarDateImport;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CalendarDateProcessor extends Processor<GtfsCalendarDate, CalendarDate> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private final MapperStore<Long, CalendarDateMapping> calendarDateMapper;
    private final JourneyMapperStore journeyMapperStore;

    @Autowired
    public CalendarDateProcessor(MapperStore<Long, CalendarDateMapping> calendarDateMapper,
            JourneyMapperStore journeyMapperStore) {
        this.calendarDateMapper = calendarDateMapper;
        this.journeyMapperStore = journeyMapperStore;
    }

    @Override
    public CalendarDate process(GtfsCalendarDate item) throws Exception {
        SequentialIdGenerator idGenerator = getIdGenerator(CalendarDateImport.getTableName());

        Date validFrom = new Date(dateFormat.parse(item.getDate()).getTime());
        Date validUntil = new Date(dateFormat.parse(item.getDate()).getTime());

        DayOfWeek day = validFrom.toLocalDate().getDayOfWeek();
        JsonObject json = saveDaysToJson(day);

        JourneyMapping journeyMapper = journeyMapperStore.getMappingByServiceId(item.getServiceId());
        if(journeyMapper ==  null) {
            return null;
        }
        long journeyId = journeyMapper.getId();

        long gtfsServiceId = Long.parseLong(item.getServiceId());
        long id = idGenerator.getId();
        calendarDateMapper.addMapping(gtfsServiceId, new CalendarDateMapping(gtfsServiceId, id));
        idGenerator.next();
        return new CalendarDate(id, validFrom, validUntil, journeyId, json);
    }
    
    private JsonObject saveDaysToJson(DayOfWeek day) {
        JsonArray list = new JsonArray();
        list.add(new JsonPrimitive(day.toString()));
        JsonObject json = new JsonObject();
        json.add("service_days", list);
        return json;
    }
}