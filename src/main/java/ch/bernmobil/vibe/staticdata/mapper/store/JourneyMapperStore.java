package ch.bernmobil.vibe.staticdata.mapper.store;

import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapping;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JourneyMapperStore extends MapperStore<String, JourneyMapping> {
    private final Map<String, ArrayList<String>> internalKeyMappings = new HashMap<>();

    @Override
    public void addMapping(String gtfsTripId, JourneyMapping mapping) {
        String gtfsServiceId = mapping.getGtfsServiceId();
        if(!internalKeyMappings.containsKey(gtfsServiceId)) {
            internalKeyMappings.put(gtfsServiceId, new ArrayList<>());
        }
        internalKeyMappings.get(gtfsServiceId).add(gtfsTripId);
        mappingMap.put(gtfsTripId, mapping);
    }

    public JourneyMapping getMappingByTripId(String gtfsTripId) {
        return super.getMapping(gtfsTripId);
    }

    public List<JourneyMapping> getMappingsByServiceId(String gtfsServiceId) {
        ArrayList<JourneyMapping> journeyMappings = new ArrayList<>();
        for(String gtfsTripId : internalKeyMappings.get(gtfsServiceId)) {
            journeyMappings.add(mappingMap.get(gtfsTripId));
        }
        return journeyMappings;
    }

}
