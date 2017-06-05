package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entitiy.Schedule;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsStopTime;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.StopMapperStore;
import java.sql.Time;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ScheduleProcessor extends Processor<GtfsStopTime, Schedule> {
    private  final Logger logger = Logger.getLogger(ScheduleProcessor.class);
    private final StopMapperStore stopMapper;
    private final JourneyMapperStore journeyMapper;

    @Autowired
    public ScheduleProcessor(
            @Qualifier("stopMapperStore") StopMapperStore stopMapper,
            @Qualifier("journeyMapperStore") JourneyMapperStore journeyMapper) {
        this.stopMapper = stopMapper;
        this.journeyMapper = journeyMapper;
    }

    @Override
    public Schedule process(GtfsStopTime item) throws Exception {
        Time plannedArrival = Time.valueOf(item.getArrivalTime());
        Time plannedDeparture = Time.valueOf(item.getDepartureTime());

        StopMapping stopMapping = stopMapper.getMapping(item.getStopId());
        JourneyMapping journeyMapping = journeyMapper.getMappingByTripId(item.getTripId());

        if(stopMapping == null || journeyMapping == null) {
            logger.warn(
                    String.format("Combination of stop id '%s' and trip id '%s' could not be found. Skipping item %s",
                            item.getStopId(),
                            item.getTripId(),
                            item));
            return null;
        }

        UUID stopId = stopMapping.getId();
        UUID journeyId = journeyMapping.getId();

        String platform = parsePlatform(item.getStopId());

        UUID id = idGenerator.getId();
        idGenerator.next();

        logger.debug(String.format("Save schedule with properties: %s, %s, %s, %s, %s, %s", id, platform, plannedArrival, plannedDeparture, stopId, journeyId));
        return new Schedule(id, platform, plannedArrival, plannedDeparture, stopId, journeyId);
    }

    private String parsePlatform(String stopId)  {
        return stopId.split("_")[1];
    }
}