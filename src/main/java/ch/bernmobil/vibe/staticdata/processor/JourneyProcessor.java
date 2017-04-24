package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Journey;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsTrip;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.importer.AreaImport;
import ch.bernmobil.vibe.staticdata.importer.TripImport;
import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapper;
import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapper;
import org.springframework.batch.item.ItemProcessor;

public class JourneyProcessor extends Processor<GtfsTrip, Journey> {
    @Override
    public Journey process(GtfsTrip item) throws Exception {
        SequentialIdGenerator idGenerator = getIdGenerator(TripImport.getTableName());
        String headsign = item.getTripHeadsign();
        long route = RouteMapper.getMappingByStopId(item.getRouteId()).getId();

        String gtfsId = item.getTripId();
        String gtfsServiceId = item.getServiceId();
        long id = idGenerator.getId();
        JourneyMapper.addMapping(gtfsId, gtfsServiceId, id);
        idGenerator.next();
        return new Journey(id, headsign, route);
    }
}