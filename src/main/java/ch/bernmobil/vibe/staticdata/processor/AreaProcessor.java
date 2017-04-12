package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Area;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapper;
import org.springframework.batch.item.ItemProcessor;

public class AreaProcessor implements ItemProcessor<GtfsStop, Area> {
    private SequentialIdGenerator idGenerator = new SequentialIdGenerator();

    @Override
    public Area process(GtfsStop item) throws Exception {
        String name = item.getStopName();
        String parentStation = item.getParentStation();
        if(parentStation.isEmpty()) {
            Area area = new Area(idGenerator.getId(), name);
            AreaMapper.addMapping(item.getStopId(), idGenerator.getId());
            idGenerator.next();
            return area;
        }
        return null;
    }
}