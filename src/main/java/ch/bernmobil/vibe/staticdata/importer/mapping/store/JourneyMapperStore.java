package ch.bernmobil.vibe.staticdata.importer.mapping.store;

import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides mapping connection between {@link ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsTrip},
 * {@link ch.bernmobil.vibe.shared.entitiy.Journey} and the service id which GTFS uses in several other entities. It
 * uses two different {@link Map} so it is possible to get a mapping by service id or trip id.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 *
 */
@Component
public class JourneyMapperStore extends MapperStore<String, JourneyMapping> {
    private final Map<String, Collection<String>> internalKeyMappings = new HashMap<>();

    /**
     * {@inheritDoc}
     * Add a {@link JourneyMapping} to the store. It will be searchable by {@link JourneyMapping#gtfsTripId} and
     * {@link JourneyMapping#gtfsServiceId}.
     * @param gtfsTripId which is used as index to be searched for.
     * @param mapping which contains all other mapping information.
     */
    @Override
    public void addMapping(String gtfsTripId, JourneyMapping mapping) {
        String gtfsServiceId = mapping.getGtfsServiceId();
        if(!internalKeyMappings.containsKey(gtfsServiceId)) {
            internalKeyMappings.put(gtfsServiceId, new ArrayList<>());
        }
        internalKeyMappings.get(gtfsServiceId).add(gtfsTripId);
        mappingMap.put(gtfsTripId, mapping);
    }

    /**
     * Retrieve a {@link JourneyMapping} by its trip id.
     * @param gtfsTripId to which the {@link JourneyMapping} corresponds.
     * @return {@link JourneyMapping} from the trip id.
     */
    public JourneyMapping getMappingByTripId(String gtfsTripId) {
        return super.getMapping(gtfsTripId);
    }

    /**
     * Retrieve a {@link List} of {@link JourneyMapping} by their service id.
     * @param gtfsServiceId corresponding to the searched {@link JourneyMapping}.
     * @return {@link List} of {@link JourneyMapping} from the service id.
     */
    public List<JourneyMapping> getMappingsByServiceId(String gtfsServiceId) {
        return internalKeyMappings.get(gtfsServiceId).stream()
                .map(mappingMap::get)
                .collect(Collectors.toList());
    }

}
