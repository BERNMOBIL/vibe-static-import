package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entitiy.Journey;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsTrip;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.StopMapperStore;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JourneyProcessor extends Processor<GtfsTrip, Journey> {
    private final MapperStore<String, RouteMapping> mapperStore;
    private final JourneyMapperStore journeyMapperStore;
    private final StopMapperStore stopMapperStore;

    @Autowired
    public JourneyProcessor(MapperStore<String, RouteMapping> mapperStore,
                            @Qualifier("journeyMapperStore") JourneyMapperStore journeyMapperStore,
                            StopMapperStore stopMapperStore) {
        this.mapperStore = mapperStore;
        this.journeyMapperStore = journeyMapperStore;
        this.stopMapperStore = stopMapperStore;
    }

    @Override
    public Journey process(GtfsTrip item) throws Exception {
        String headsign = item.getTripHeadsign();
        UUID route = mapperStore.getMapping(item.getRouteId()).getId();

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