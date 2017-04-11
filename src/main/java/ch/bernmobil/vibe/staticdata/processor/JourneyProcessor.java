package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Journey;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsTrip;
import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapper;
import org.springframework.batch.item.ItemProcessor;

public class JourneyProcessor implements ItemProcessor<GtfsTrip, Journey> {
    @Override
    public Journey process(GtfsTrip item) throws Exception {
        String headsign = item.getTripHeadsign();
        Long route = RouteMapper.getMappingByStopId(item.getRouteId()).getId();

        String gtfsId = item.getTripId();
        String gtfsServiceId = item.getServiceId();

        return new Journey(headsign, route, gtfsId, gtfsServiceId);
    }
}