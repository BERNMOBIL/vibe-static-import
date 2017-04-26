package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Area;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapping;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AreaProcessor implements ItemProcessor<GtfsStop, Area> {
    private final SequentialIdGenerator idGenerator;
    private final MapperStore<String, AreaMapping> mappingStore;

    @Autowired
    public AreaProcessor(SequentialIdGenerator idGenerator,
                         @Qualifier("areaMapperStore") MapperStore<String, AreaMapping> mappingStore) {
        this.idGenerator = idGenerator;
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