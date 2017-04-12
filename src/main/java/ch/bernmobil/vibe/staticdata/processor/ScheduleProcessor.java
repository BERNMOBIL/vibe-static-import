package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Schedule;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStopTime;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapper;
import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapper;
import java.sql.Time;
import org.springframework.batch.item.ItemProcessor;

public class ScheduleProcessor implements ItemProcessor<GtfsStopTime, Schedule> {
    private SequentialIdGenerator idGenerator = new SequentialIdGenerator();

    @Override
    public Schedule process(GtfsStopTime item) throws Exception {
        Time plannedArrival = Time.valueOf(item.getArrivalTime());
        Time plannedDeparture = Time.valueOf(item.getDepartureTime());

        long stop = StopMapper.getMappingByStopId(item.getStopId()).getId();
        long journey = JourneyMapper.getMappingByTripId(item.getTripId()).getId();

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