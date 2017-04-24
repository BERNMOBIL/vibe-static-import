package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Stop;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.importer.AreaImport;
import ch.bernmobil.vibe.staticdata.importer.StopImport;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapper;
import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapper;
import org.springframework.batch.item.ItemProcessor;

public class StopProcessor extends Processor<GtfsStop, Stop>{

    @Override
    public Stop process(GtfsStop item) throws Exception {
        SequentialIdGenerator idGenerator = getIdGenerator(StopImport.getTableName());
        String parentStation = item.getParentStation();
        if(!parentStation.isEmpty()) {
            long id = idGenerator.getId();
            StopMapper.addMapping(item.getStopId(), id);
            long areaId = AreaMapper.getMappingByStopId(item.getParentStation()).getId();
            idGenerator.next();
            StopMapper.addMapping(item.getStopId(), id);
            return new Stop(id, item.getStopName(), areaId);
        }

        return null;
    }
}