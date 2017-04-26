package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Journey;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsTrip;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.mapper.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapping;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JourneyProcessor implements ItemProcessor<GtfsTrip, Journey> {
    private final SequentialIdGenerator idGenerator;
    private final MapperStore<String, RouteMapping> mapperStore;
    private final JourneyMapperStore journeyMapperStore;

    @Autowired
    public JourneyProcessor(SequentialIdGenerator idGenerator,
            MapperStore<String, RouteMapping> mapperStore,
            @Qualifier("journeyMapperStore") JourneyMapperStore journeyMapperStore) {
        this.idGenerator = idGenerator;
        this.mapperStore = mapperStore;
        this.journeyMapperStore = journeyMapperStore;
    }

    @Override
    public Journey process(GtfsTrip item) throws Exception {
        String headsign = item.getTripHeadsign();
        long route = mapperStore.getMapping(item.getRouteId()).getId();

        String gtfsId = item.getTripId();
        String gtfsServiceId = item.getServiceId();
        long id = idGenerator.getId();
        journeyMapperStore.addMapping(gtfsId, new JourneyMapping(gtfsId, gtfsServiceId, id));
        idGenerator.next();
        return new Journey(id, headsign, route);
    }
}