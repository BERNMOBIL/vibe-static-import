package ch.bernmobil.vibe.staticdata.processor;


import ch.bernmobil.vibe.shared.entitiy.Stop;
import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsStop;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.StopMapperStore;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class StopProcessor extends Processor<GtfsStop, Stop>{
    private final MapperStore<String, StopMapping> stopMapper;
    private final MapperStore<String, AreaMapping> areaMapper;

    @Autowired
    public StopProcessor(@Qualifier("stopMapperStore") StopMapperStore stopMapper,
            @Qualifier("areaMapperStore") MapperStore<String, AreaMapping> areaMapper) {
        this.stopMapper = stopMapper;
        this.areaMapper = areaMapper;
    }

    @Override
    public Stop process(GtfsStop item) throws Exception {
        String parentStation = item.getParentStation();
        if(!parentStation.isEmpty()) {
            UUID id = idGenerator.next();
            String stopId = item.getStopId();
            stopMapper.addMapping(stopId, new StopMapping(stopId, item.getStopName(), id));

            UUID areaId = areaMapper.getMapping(item.getParentStation()).getId();

            return new Stop(id, item.getStopName(), areaId);
        }
        return null;
    }
}