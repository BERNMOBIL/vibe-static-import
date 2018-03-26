package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entity.CalendarDate;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class for converting {@link GtfsCalendarDate} into a {@link List} of {@link CalendarDate} (because the N:M relationship
 * between {@link GtfsCalendarDate} and {@link ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsTrip}).
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Component
public class CalendarDateProcessor extends Processor<GtfsCalendarDate, List<CalendarDate>> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private final MapperStore<String, CalendarDateMapping> calendarDateMapper;
    private final JourneyMapperStore journeyMapperStore;

    /**
     * Constructs an instance using a {@link JourneyMapperStore} and another {@link MapperStore} for
     * {@link CalendarDateMapping}.
     * @param calendarDateMapper which manages mappings for {@link CalendarDateMapping}
     * @param journeyMapperStore which manages mappings for {@link JourneyMapping}
     */
    @Autowired
    public CalendarDateProcessor(
            @Qualifier("calendarDateMapperStore") MapperStore<String, CalendarDateMapping> calendarDateMapper,
            @Qualifier("journeyMapperStore") JourneyMapperStore journeyMapperStore) {
        this.calendarDateMapper = calendarDateMapper;
        this.journeyMapperStore = journeyMapperStore;
    }

    /**
     * Converts a {@link GtfsCalendarDate} into a {@link List} of {@link CalendarDate}. While converting it needs
     * to save the relation to a {@link ch.bernmobil.vibe.shared.entity.Journey}, hence it needs to find all the
     * {@link JourneyMapping} for a given GTFS service id. If there are no {@link JourneyMapping} with the service id
     * nothing will be processed and null is returned. Otherwise it extracts all needed information from the
     * {@link GtfsCalendarDate} and saves it into a {@link List}. The {@link MapperStore} for {@link CalendarDateMapping}
     * is filled with a compound key consisting of the {@link GtfsCalendarDate#serviceId} and {@link GtfsCalendarDate#date}.
     * This is because the service id is not unique, and it would overwrite existing mappings in the {@link MapperStore}.
     * But the combination of {@link GtfsCalendarDate#serviceId} and {@link GtfsCalendarDate#date} is unique.
     * @param item to be processed
     * @return {@link List} of {@link CalendarDate} with valid data and relations, null if not processed.
     * @throws Exception will be thrown if there is any error during processing the {@link GtfsCalendarDate}.
     */
    @Override
    public List<CalendarDate> process(GtfsCalendarDate item) throws Exception {
        Date validFrom = new Date(dateFormat.parse(item.getDate()).getTime());
        Date validUntil = new Date(dateFormat.parse(item.getDate()).getTime());

        DayOfWeek day = validFrom.toLocalDate().getDayOfWeek();
        JsonObject days = saveDaysToJson(day);

        List<JourneyMapping> journeyMappings = journeyMapperStore.getMappingsByServiceId(item.getServiceId());
        if(journeyMappings.isEmpty()) {
            return null;
        }

        List<CalendarDate> calendarDates = new ArrayList<>();

        for(JourneyMapping journeyMapping : journeyMappings) {
            UUID journeyId = journeyMapping.getId();
            String mappingKey = String.format("%s%s", item.getServiceId(), item.getDate());
            UUID id = idGenerator.getId();
            calendarDateMapper.addMapping(mappingKey, new CalendarDateMapping(item.getServiceId(), id));
            idGenerator.next();
            calendarDates.add(new CalendarDate(id, validFrom, validUntil, journeyId, days));
        }

        return calendarDates;
    }

    /**
     * Builds a {@link JsonObject} which contains a {@link JsonArray} with all the given days, in the defined format.
     * @param days on which the {@link CalendarDate} instance is valid.
     * @return {@link JsonObject} containing an {@link JsonArray} with all days.
     */
    private JsonObject saveDaysToJson(DayOfWeek... days) {
        JsonArray list = new JsonArray();
        for (DayOfWeek dayOfWeek : days) {
            list.add(new JsonPrimitive(dayOfWeek.toString()));
        }
        JsonObject json = new JsonObject();
        json.add("service_days", list);
        return json;
    }
}