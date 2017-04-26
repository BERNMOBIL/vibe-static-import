package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Schedule;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStopTime;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.mapper.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapping;
import java.sql.Time;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ScheduleProcessor implements ItemProcessor<GtfsStopTime, Schedule> {
    private final SequentialIdGenerator idGenerator;
    private final MapperStore<String, StopMapping> stopMapper;
    private final JourneyMapperStore journeyMapper;

    @Autowired
    public ScheduleProcessor(SequentialIdGenerator idGenerator,
            @Qualifier("stopMapperStore") MapperStore<String, StopMapping> stopMapper,
            @Qualifier("journeyMapperStore") JourneyMapperStore journeyMapper) {
        this.idGenerator = idGenerator;
        this.stopMapper = stopMapper;
        this.journeyMapper = journeyMapper;
    }

    @Override
    public Schedule process(GtfsStopTime item) throws Exception {
        Time plannedArrival = Time.valueOf(item.getArrivalTime());
        Time plannedDeparture = Time.valueOf(item.getDepartureTime());

        long stop = stopMapper.getMapping(item.getStopId()).getId();
        long journey = journeyMapper.getMappingByTripId(item.getTripId()).getId();

        String platform = parsePlatform(item.getStopId());

        long id = idGenerator.getId();
        idGenerator.next();
        return new Schedule(id, platform, plannedArrival, plannedDeparture, stop, journey);
    }

    private String parsePlatform(String stopId)  {
        String[] splitString = stopId.split("_");
        return splitString[1];
    }
}