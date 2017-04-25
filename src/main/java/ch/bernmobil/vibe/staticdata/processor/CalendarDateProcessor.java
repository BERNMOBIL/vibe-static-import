package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.importer.AreaImport;
import ch.bernmobil.vibe.staticdata.importer.CalendarDateImport;
import ch.bernmobil.vibe.staticdata.mapper.sync.CalendarDateMapper;
import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import org.springframework.batch.item.ItemProcessor;

public class CalendarDateProcessor extends Processor<GtfsCalendarDate, CalendarDate> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");

    @Override
    public CalendarDate process(GtfsCalendarDate item) throws Exception {
        SequentialIdGenerator idGenerator = getIdGenerator(CalendarDateImport.getTableName());

        Date validFrom = new Date(dateFormat.parse(item.getDate()).getTime());
        Date validUntil = new Date(dateFormat.parse(item.getDate()).getTime());

        DayOfWeek day = validFrom.toLocalDate().getDayOfWeek();
        JsonObject json = saveDaysToJson(day);

        JourneyMapper journeyMapper = JourneyMapper.getMappingByServiceId(item.getServiceId());
        if(journeyMapper ==  null) {
            return null;
        }
        long journeyId = journeyMapper.getId();

        long gtfsServiceId = Long.parseLong(item.getServiceId());
        long id = idGenerator.getId();
        CalendarDateMapper.addMapping(gtfsServiceId, id);
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