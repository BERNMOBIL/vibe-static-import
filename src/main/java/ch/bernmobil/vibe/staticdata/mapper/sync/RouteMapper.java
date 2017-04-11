package ch.bernmobil.vibe.staticdata.mapper.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteMapper {
    private static HashMap<String, RouteMapper> mappings = new HashMap<>();

    private String gtfsId;
    private Long id;

    private RouteMapper(String gtfsId, Long id) {
        this.gtfsId = gtfsId;
        this.id = id;
    }

    public String getGtfsId() {
        return gtfsId;
    }

    public void setGtfsId(String gtfsId) {
        this.gtfsId = gtfsId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static void addMapping(String gtfsId, Long id) {
        mappings.put(gtfsId, new RouteMapper(gtfsId, id));
    }

    public static RouteMapper getMappingByStopId(String gtfsId) {
        return mappings.get(gtfsId);
    }

    public static List<RouteMapper> getAll() {
        return new ArrayList<>(mappings.values());
    }

}
