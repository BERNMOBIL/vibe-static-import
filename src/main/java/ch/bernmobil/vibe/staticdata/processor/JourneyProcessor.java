package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entity.Journey;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsTrip;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.StopMapperStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Class to convert a {@link GtfsTrip} to a {@link Journey}.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Component
public class JourneyProcessor extends Processor<GtfsTrip, Journey> {
    private final MapperStore<String, RouteMapping> routeMapperStore;
    private final JourneyMapperStore journeyMapperStore;
    private final StopMapperStore stopMapperStore;

    /**
     * Constructor to collect all {@link MapperStore} which are necessary to convert a {@link GtfsTrip}
     * @param routeMapperStore to get and save {@link RouteMapping}.
     * @param journeyMapperStore to get and save {@link JourneyMapping}.
     * @param stopMapperStore to get and save {@link StopMapping}.
     */
    @Autowired
    public JourneyProcessor(@Qualifier("routeMapperStore") MapperStore<String, RouteMapping> routeMapperStore,
                            @Qualifier("journeyMapperStore") JourneyMapperStore journeyMapperStore,
                            StopMapperStore stopMapperStore) {
        this.routeMapperStore = routeMapperStore;
        this.journeyMapperStore = journeyMapperStore;
        this.stopMapperStore = stopMapperStore;
    }

    /**
     * Process a {@link GtfsTrip}, extract all necessary information and save it into a {@link Journey}. The terminal
     * stop is determined by comparing the {@link GtfsTrip#tripHeadsign} and
     * {@link ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStop#stopName}. If these do not match, {@link Journey#terminalStation}
     * will be null.
     * @param item to be processed
     * @return {@link Journey} with all necessary information from the given {@link GtfsTrip}.
     * @throws Exception will be thrown if there is a {@link RuntimeException} during processing.
     */
    @Override
    public Journey process(GtfsTrip item) throws Exception {
        String headsign = item.getTripHeadsign();
        UUID route = routeMapperStore.getMapping(item.getRouteId()).getId();

        String gtfsId = item.getTripId();
        String gtfsServiceId = item.getServiceId();

        StopMapping stopMapping = stopMapperStore.getStopByStopName(headsign);
        UUID terminalStation = null;
        if(stopMapping != null) {
            terminalStation = stopMapping.getId();
        }

        UUID id = idGenerator.getId();
        journeyMapperStore.addMapping(gtfsId, new JourneyMapping(gtfsId, gtfsServiceId, id));
        idGenerator.next();

        return new Journey(id, headsign, route, terminalStation);
    }
}