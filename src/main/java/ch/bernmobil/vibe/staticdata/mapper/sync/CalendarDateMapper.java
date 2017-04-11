package ch.bernmobil.vibe.staticdata.mapper.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarDateMapper {
    private static HashMap<Long, CalendarDateMapper> mappings = new HashMap<>();

    private Long gtfsId;
    private Long id;

    private CalendarDateMapper(Long gtfsId, Long id) {
        this.gtfsId = gtfsId;
        this.id = id;
    }

    public Long getGtfsId() {
        return gtfsId;
    }

    public Long getId() {
        return id;
    }

    public static void addMapping(Long gtfsId, Long id) {
        mappings.put(gtfsId, new CalendarDateMapper(gtfsId, id));
    }

    public static CalendarDateMapper getMappingByCalendarId(Long gtfsId) {
        return mappings.get(gtfsId);
    }
    public static List<CalendarDateMapper> getAll() {
        return new ArrayList<>(mappings.values());
    }
}
