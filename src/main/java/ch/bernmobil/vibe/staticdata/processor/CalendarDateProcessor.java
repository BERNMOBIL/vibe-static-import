package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.mapper.sync.CalendarDateMapper;
import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapper;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.batch.item.ItemProcessor;

public class CalendarDateProcessor implements ItemProcessor<GtfsCalendarDate, CalendarDate> {
    private SequentialIdGenerator idGenerator = new SequentialIdGenerator();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");

    @Override
    public CalendarDate process(GtfsCalendarDate item) throws Exception {

        Date validFrom = new Date(dateFormat.parse(item.getDate()).getTime());
        Date validUntil = new Date(dateFormat.parse(item.getDate()).getTime());

        DayOfWeek day = validFrom.toLocalDate().getDayOfWeek();
        JSONObject json = saveDaysToJson(day);

        long journeyId = JourneyMapper.getMappingByServiceId(item.getServiceId()).getId();

        long gtfsServiceId = Long.parseLong(item.getServiceId());
        long id = idGenerator.getId();
        CalendarDateMapper.addMapping(gtfsServiceId, id);
        idGenerator.next();
        return new CalendarDate(id, validFrom, validUntil, journeyId, json);
    }

    // JSON Object uses raw types to save in its internal hash-map.
    @SuppressWarnings("unchecked")
    private JSONObject saveDaysToJson(DayOfWeek day) {
        JSONArray list = new JSONArray();
        list.add(day);
        JSONObject json = new JSONObject();
        json.put("service_days", list);
        return json;
    }
}