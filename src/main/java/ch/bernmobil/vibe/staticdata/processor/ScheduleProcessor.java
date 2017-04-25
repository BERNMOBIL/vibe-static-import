package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Schedule;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStopTime;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.importer.StopTimeImport;
import ch.bernmobil.vibe.staticdata.mapper.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapping;
import java.sql.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleProcessor extends Processor<GtfsStopTime, Schedule> {
    private final MapperStore<String, StopMapping> stopMapper;
    private final JourneyMapperStore journeyMapper;

    @Autowired
    public ScheduleProcessor(MapperStore<String, StopMapping> stopMapper,
            JourneyMapperStore journeyMapper) {
        this.stopMapper = stopMapper;
        this.journeyMapper = journeyMapper;
    }

    @Override
    public Schedule process(GtfsStopTime item) throws Exception {
        SequentialIdGenerator idGenerator = getIdGenerator(StopTimeImport.getTableName());
        Time plannedArrival = Time.valueOf(item.getArrivalTime());
        Time plannedDeparture = Time.valueOf(item.getDepartureTime());

        StopMapping stopMapping = stopMapper.getMapping(item.getStopId());
        JourneyMapping journeyMapping = journeyMapper.getMappingByTripId(item.getTripId());

        if(stopMapping == null || journeyMapping == null) {
            return null;
        }

        long stopId = stopMapping.getId();
        long journeyId = journeyMapping.getId();

        String platform = parsePlatform(item.getStopId());

        long id = idGenerator.getId();
        idGenerator.next();
        return new Schedule(id, platform, plannedArrival, plannedDeparture, stopId, journeyId);
    }

    private String parsePlatform(String stopId)  {
        String[] splitString = stopId.split("_");
        return splitString[1];
    }
}