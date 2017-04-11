package ch.bernmobil.vibe.staticdata.mapper.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StopMapper {
    private static HashMap<String, StopMapper> mappings = new HashMap<>();

    private String gtfsId;
    private Long id;

    private StopMapper(String gtfsId, Long id) {
        this.gtfsId = gtfsId;
        this.id = id;
    }

    public String getGtfsId() {
        return gtfsId;
    }

    public Long getId() {
        return id;
    }

    public static void addMapping(String gtfsId, Long id) {
        mappings.put(gtfsId, new StopMapper(gtfsId, id));
    }

    public static StopMapper getMappingByStopId(String gtfsId) {
        return mappings.get(gtfsId);
    }

    public static List<StopMapper> getAll() {
        return new ArrayList<>(mappings.values());
    }
}
