package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Stop;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.importer.StopImport;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StopProcessor extends Processor<GtfsStop, Stop>{
    private final MapperStore<String, StopMapping> stopMapper;
    private final MapperStore<String, AreaMapping> areaMapper;

    @Autowired
    public StopProcessor(MapperStore<String, StopMapping> stopMapper,
            MapperStore<String, AreaMapping> areaMapper) {
        this.stopMapper = stopMapper;
        this.areaMapper = areaMapper;
    }

    @Override
    public Stop process(GtfsStop item) throws Exception {
        SequentialIdGenerator idGenerator = getIdGenerator(StopImport.getTableName());
        String parentStation = item.getParentStation();
        if(!parentStation.isEmpty()) {
            long id = idGenerator.getId();
            String stopId = item.getStopId();
            stopMapper.addMapping(stopId, new StopMapping(stopId, id));
            long areaId = areaMapper.getMapping(item.getParentStation()).getId();
            idGenerator.next();
            stopMapper.addMapping(stopId, new StopMapping(stopId, id));
            return new Stop(id, item.getStopName(), areaId);
        }
        return null;
    }
}