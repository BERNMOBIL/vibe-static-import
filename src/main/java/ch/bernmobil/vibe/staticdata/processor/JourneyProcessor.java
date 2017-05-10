package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Journey;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsTrip;
import ch.bernmobil.vibe.staticdata.mapper.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapping;

import java.util.Optional;
import java.util.UUID;

import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JourneyProcessor extends Processor<GtfsTrip, Journey> {
    private final MapperStore<String, RouteMapping> mapperStore;
    private final JourneyMapperStore journeyMapperStore;
    private final MapperStore<String, StopMapping> stopMapperStore;

    @Autowired
    public JourneyProcessor(MapperStore<String, RouteMapping> mapperStore,
                            @Qualifier("journeyMapperStore") JourneyMapperStore journeyMapperStore,
                            MapperStore<String, StopMapping> stopMapperStore) {
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

        Optional<StopMapping> stopMapping = stopMapperStore
                .getMappings()
                .stream()
                .filter(s -> s.getName().equals(headsign))
                .findFirst();

        UUID terminalStation = null;
        if(stopMapping.isPresent()) {
            terminalStation = stopMapping.get().getId();
        }

        UUID id = idGenerator.getId();
        journeyMapperStore.addMapping(gtfsId, new JourneyMapping(gtfsId, gtfsServiceId, id));
        idGenerator.next();

        return new Journey(id, headsign, route, terminalStation);
    }
}