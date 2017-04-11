package ch.bernmobil.vibe.staticdata.mapper.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JourneyMapper {
    private static HashMap<String, JourneyMapper> mappings = new HashMap<>();
    private static HashMap<String, String> internalKeyMappings = new HashMap<>();

    private String gtfsTripId;
    private String gtfsServiceId;
    private Long id;

    private JourneyMapper(String gtfsTripId, String gtfsServiceId, Long id) {
        this.gtfsTripId = gtfsTripId;
        this.gtfsServiceId = gtfsServiceId;
        this.id = id;
    }

    public String getGtfsTripId() {
        return gtfsTripId;
    }

    public String getGtfsServiceId() {
        return gtfsServiceId;
    }

    public Long getId() {
        return id;
    }

    public static void addMapping(String gtfsTripId, String gtfsServiceId, Long id) {
        internalKeyMappings.put(gtfsServiceId, gtfsTripId);
        mappings.put(gtfsTripId, new JourneyMapper(gtfsTripId, gtfsServiceId, id));
    }

    public static JourneyMapper getMappingByTripId(String gtfsTripId) {
        return mappings.get(gtfsTripId);
    }

    public static JourneyMapper getMappingByServiceId(String gtfsServiceId) {
        return mappings.get(internalKeyMappings.get(gtfsServiceId));
    }
    public static List<JourneyMapper> getAll() {
        return new ArrayList<>(mappings.values());
    }

}
