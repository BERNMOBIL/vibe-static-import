package ch.bernmobil.vibe.staticdata.processor;


import ch.bernmobil.vibe.shared.entitiy.Stop;
import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsStop;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.StopMapperStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Converts a {@link GtfsStop} into a {@link Stop}.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Component
public class StopProcessor extends Processor<GtfsStop, Stop>{
    private final StopMapperStore stopMapper;
    private final MapperStore<String, AreaMapping> areaMapper;

    /**
     * Constructor demanding {@link StopMapperStore} to save new {@link StopMapping} and a {@link MapperStore} for
     * {@link AreaMapping} to get the relationship to {@link ch.bernmobil.vibe.shared.entitiy.Area}.
     * @param stopMapper to save {@link StopMapping}.
     * @param areaMapper to get {@link AreaMapping}.
     */
    @Autowired
    public StopProcessor(@Qualifier("stopMapperStore") StopMapperStore stopMapper,
            @Qualifier("areaMapperStore") MapperStore<String, AreaMapping> areaMapper) {
        this.stopMapper = stopMapper;
        this.areaMapper = areaMapper;
    }

    /**
     * Processes a {@link GtfsStop}, extracts all information and saves into a {@link Stop}. But only if
     * {@link GtfsStop#parentStation} is not empty, since {@link Stop} needs a relation to an {@link ch.bernmobil.vibe.shared.entitiy.Area}.
     * If {@link GtfsStop#parentStation} is an empty {@link String} it will return null.
     * @param item to be processed
     * @return {@link Stop} which contains all necessary information of {@link GtfsStop} or, if {@link GtfsStop#parentStation}
     * is empty, null.
     * @throws Exception is thrown if a {@link RuntimeException} occurrs.
     */
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