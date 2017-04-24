package ch.bernmobil.vibe.staticdata.mapper.store;

import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JourneyMapperStore extends MapperStore<String, JourneyMapping> {
    private Map<String, String> internalKeyMappings = new HashMap<>();

    @Override
    public void addMapping(String gtfsTripId, JourneyMapping mapping) {
        internalKeyMappings.put(mapping.getGtfsServiceId(), gtfsTripId);
        mappingMap.put(gtfsTripId, mapping);
    }

    public JourneyMapping getMappingByTripId(String gtfsTripId) {
        return super.getMapping(gtfsTripId);
    }

    public JourneyMapping getMappingByServiceId(String gtfsServiceId) {
        return mappingMap.get(internalKeyMappings.get(gtfsServiceId));
    }

    @Override
    public List<JourneyMapping> getMappings() {
        return super.getMappings();
    }
}
