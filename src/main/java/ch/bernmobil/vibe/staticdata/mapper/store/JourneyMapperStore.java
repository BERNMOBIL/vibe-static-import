package ch.bernmobil.vibe.staticdata.mapper.store;

import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapping;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JourneyMapperStore extends MapperStore<String, JourneyMapping> {
    private final Map<String, String> internalKeyMappings = new HashMap<>();

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

}
