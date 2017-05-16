package ch.bernmobil.vibe.staticdata.processor;


import ch.bernmobil.vibe.staticdata.entitiy.Stop;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapping;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class StopProcessor extends Processor<GtfsStop, Stop>{
    private final MapperStore<String, StopMapping> stopMapper;
    private final MapperStore<String, AreaMapping> areaMapper;

    @Autowired
    public StopProcessor(@Qualifier("stopMapperStore") MapperStore<String, StopMapping> stopMapper,
            @Qualifier("areaMapperStore") MapperStore<String, AreaMapping> areaMapper) {
        this.stopMapper = stopMapper;
        this.areaMapper = areaMapper;
    }

    @Override
    public Stop process(GtfsStop item) throws Exception {
        String parentStation = item.getParentStation();
        if(!parentStation.isEmpty()) {
            UUID id = idGenerator.getId();
            String stopId = item.getStopId();
            stopMapper.addMapping(stopId, new StopMapping(stopId, item.getStopName(), id));

            UUID areaId = areaMapper.getMapping(item.getParentStation()).getId();
            idGenerator.next();
            //stopMapper.addMapping(stopId, new StopMapping(stopId, item.getStopName(), id));

            return new Stop(id, item.getStopName(), areaId);
        }
        return null;
    }
}