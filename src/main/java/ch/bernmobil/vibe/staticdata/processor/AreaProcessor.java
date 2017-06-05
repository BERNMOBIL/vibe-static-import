package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entitiy.Area;
import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsStop;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AreaProcessor extends Processor<GtfsStop,Area> {
    private final MapperStore<String, AreaMapping> mappingStore;

    @Autowired
    public AreaProcessor(@Qualifier("areaMapperStore") MapperStore<String, AreaMapping> mappingStore) {
        this.mappingStore = mappingStore;
    }

    @Override
    public Area process(GtfsStop item) throws Exception {
        String name = item.getStopName();
        String parentStation = item.getParentStation();
        if(parentStation.isEmpty()) {
            Area area = new Area(idGenerator.getId(), name);
            mappingStore.addMapping(item.getStopId(), new AreaMapping(item.getStopId(), idGenerator.getId()));
            idGenerator.next();
            return area;
        }
        return null;
    }
}