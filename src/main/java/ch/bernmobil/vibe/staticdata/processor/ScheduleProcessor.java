package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entity.Schedule;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStopTime;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.StopMapperStore;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.UUID;

/**
 * Class to convert {@link GtfsStopTime} to {@link Schedule}.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Component
public class ScheduleProcessor extends Processor<GtfsStopTime, Schedule> {
    private  final Logger logger = Logger.getLogger(ScheduleProcessor.class);
    private final StopMapperStore stopMapper;
    private final JourneyMapperStore journeyMapper;

    /**
     * Constructor demanding all {@link MapperStore} to get mapping information.
     * @param stopMapper to get  {@link StopMapping}
     * @param journeyMapper to get and save {@link JourneyMapping}.
     */
    @Autowired
    public ScheduleProcessor(
            @Qualifier("stopMapperStore") StopMapperStore stopMapper,
            @Qualifier("journeyMapperStore") JourneyMapperStore journeyMapper) {
        this.stopMapper = stopMapper;
        this.journeyMapper = journeyMapper;
    }

    /**
     * Process a {@link GtfsStopTime}, extract all necessary information and save it into a {@link Schedule}. To
     * resolve all dependent entities, a {@link StopMapping} and a {@link JourneyMapping} is required. If
     * {@link GtfsStopTime#stopId} or {@link ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsTrip#tripId} is null, the
     * {@link Schedule} cannot be created correctly and null will be returned. If these information are present
     * the {@link Schedule#plannedArrival} and {@link Schedule#plannedDeparture} are parsed from the {@link GtfsStopTime}
     * as well as the platform is parsed out of the {@link GtfsStopTime#stopId}.
     * @param item to be processed
     * @return {@link Schedule} which contains all necessary information from a {@link GtfsStopTime}.
     * @throws Exception will be thrown if there is a {@link RuntimeException} during processing.
     */
    @SuppressWarnings("RedundantThrows")
    @Override
    public Schedule process(GtfsStopTime item) throws Exception {
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
        Time plannedArrival = Time.valueOf(item.getArrivalTime());
        Time plannedDeparture = Time.valueOf(item.getDepartureTime());

        UUID stopId = stopMapping.getId();
        UUID journeyId = journeyMapping.getId();

        String platform = parsePlatform(item.getStopId());

        UUID id = idGenerator.getId();
        idGenerator.next();

        return new Schedule(id, platform, plannedArrival, plannedDeparture, stopId, journeyId);
    }

    /**
     * Parse platform information out of {@link GtfsStopTime#stopId}. This GTFS provider encodes the platform into the
     * {@link ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStop#stopId}. A stop area, which is a top-level stop, without
     * {@link ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStop#parentStation}, is referenced by all child stops which
     * are distinguished by their physical position (e.g platform). This information is part of the
     * {@link ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStop#stopId}. The delimiter of the {@link ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStop#stopId}
     * and the platform is a "_".
     *
     * @see <a href=http://gtfs.geops.ch/doc/>http://gtfs.geops.ch/doc</a>
     * @see <a href=https://developers.google.com/transit/gtfs/reference/gtfs-extensions>https://developers.google.com/transit/gtfs/reference/gtfs-extensions</a>
     * @param stopId of the {@link ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStop} which contains a platform.
     * @return {@link String} which identifies the platform of the stop.
     */
    private String parsePlatform(String stopId)  {
        //TODO: Strategic
        String[] split = stopId.split(":");
        return split[split.length - 1];
    }
}