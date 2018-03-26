package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entity.Area;
import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStop;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class for converting {@link GtfsStop} into a {@link Area}.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Component
public class AreaProcessor extends Processor<GtfsStop,Area> {
    private final MapperStore<String, AreaMapping> mappingStore;

    /**
     * Constructor saved the correct {@link MapperStore} to put id mappings of {@link GtfsStop} and {@link Area}.
     * @param mappingStore to save {@link AreaMapping} which will later be written into the database.
     */
    @Autowired
    public AreaProcessor(@Qualifier("areaMapperStore") MapperStore<String, AreaMapping> mappingStore) {
        this.mappingStore = mappingStore;
    }

    /**
     * Process a {@link GtfsStop}, extract all necessary information, and save it into a new {@link Area}
     * but only if {@link GtfsStop#parentStation} is empty, since this distinguishes it
     * from a {@link ch.bernmobil.vibe.shared.entity.Stop}. For every processed entity, a new {@link AreaMapping}
     * is added into the {@link #mappingStore}.
     * @param item to be processed
     * @return A new {@link Area} out of {@link GtfsStop} if {@link GtfsStop#parentStation} is an empty string. Otherwise
     * null
     * @throws Exception if there is any {@link RuntimeException} while processing the {@link GtfsStop}.
     */
    @SuppressWarnings("RedundantThrows")
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