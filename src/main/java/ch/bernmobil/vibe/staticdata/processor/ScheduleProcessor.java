package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Schedule;
import ch.bernmobil.vibe.staticdata.entity.sync.JourneyMapper;
import ch.bernmobil.vibe.staticdata.entity.sync.StopMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStopTime;
import java.sql.Time;
import org.springframework.batch.item.ItemProcessor;

public class ScheduleProcessor implements ItemProcessor<GtfsStopTime, Schedule> {

    @Override
    public Schedule process(GtfsStopTime item) throws Exception {
        String plattform = "Plattform";

        Time plannedArrival = Time.valueOf(item.getArrivalTime());
        Time plannedDeparture = Time.valueOf(item.getDepartureTime());
        Long stop = StopMapper.getMappingByStopId(item.getStopId()).getId();
        Long journey = JourneyMapper.getMappingByTripId(item.getTripId()).getId();

        return new Schedule(plattform, plannedArrival, plannedDeparture, stop, journey);
    }
}