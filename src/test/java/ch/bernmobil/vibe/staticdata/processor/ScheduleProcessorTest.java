package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entitiy.Schedule;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsStopTime;
import ch.bernmobil.vibe.staticdata.idprovider.UuidGenerator;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.StopMapperStore;
import ch.bernmobil.vibe.staticdata.testenvironment.GtfsEntitiyBuilder;
import ch.bernmobil.vibe.staticdata.testenvironment.JourneyMappingTestData;
import ch.bernmobil.vibe.staticdata.testenvironment.StopMappingTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Time;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@ActiveProfiles("testConfiguration")
public class ScheduleProcessorTest {
    private StopMapperStore stopMapper;
    private JourneyMapperStore journeyMapper;
    private UuidGenerator idGenerator;

    @Test
    public void process() throws Exception {
        JourneyMappingTestData journeyData = new JourneyMappingTestData();
        StopMappingTestData stopData = new StopMappingTestData();

        JourneyMapping journeyMapping = journeyData.get(0);
        StopMapping stopMapping = stopData.get(0);
        UUID uuid = journeyMapping.getId();

        String arrival = "12:00:00";
        String departure = "12:03:00";

        GtfsStopTime stopTime = GtfsEntitiyBuilder.buildStopTime(stopMapping.getGtfsId(), journeyMapping.getGtfsTripId(), arrival, departure);

        when(stopMapper.getMapping(stopMapping.getGtfsId())).thenReturn(stopMapping);
        when(journeyMapper.getMappingByTripId(journeyMapping.getGtfsTripId())).thenReturn(journeyMapping);
        when(idGenerator.getId()).thenReturn(uuid);

        ScheduleProcessor processor = new ScheduleProcessor(stopMapper, journeyMapper);
        processor.setIdGenerator(idGenerator);

        Schedule schedule = processor.process(stopTime);

        verify(idGenerator, times(1)).next();

        assertThat(schedule, not(nullValue()));
        assertThat(schedule.getId(), is(uuid));
        assertThat(schedule.getPlatform(), is("1"));
        assertThat(schedule.getPlannedArrival(), is(Time.valueOf(arrival)));
        assertThat(schedule.getPlannedDeparture(), is(Time.valueOf(departure)));
        assertThat(schedule.getStop(), is(uuid));
        assertThat(schedule.getJourney(), is(uuid));
    }

    @Test
    public void processNoStopMapping() throws Exception {
        JourneyMappingTestData journeyData = new JourneyMappingTestData();
        StopMappingTestData stopData = new StopMappingTestData();

        JourneyMapping journeyMapping = journeyData.get(0);
        StopMapping stopMapping = stopData.get(0);

        String arrival = "12:00:00";
        String departure = "12:03:00";

        GtfsStopTime stopTime = GtfsEntitiyBuilder.buildStopTime(stopMapping.getGtfsId(), journeyMapping.getGtfsTripId(), arrival, departure);

        when(journeyMapper.getMappingByTripId(journeyMapping.getGtfsTripId())).thenReturn(journeyMapping);
        when(stopMapper.getMapping(stopMapping.getGtfsId())).thenReturn(null);


        ScheduleProcessor processor = new ScheduleProcessor(stopMapper, journeyMapper);
        processor.setIdGenerator(idGenerator);

        Schedule schedule = processor.process(stopTime);

        verify(idGenerator, never()).next();

        assertThat(schedule, is(nullValue()));
    }

    @Test
    public void processNoJourneyMapping() throws Exception {
        JourneyMappingTestData journeyData = new JourneyMappingTestData();
        StopMappingTestData stopData = new StopMappingTestData();

        JourneyMapping journeyMapping = journeyData.get(0);
        StopMapping stopMapping = stopData.get(0);

        String arrival = "12:00:00";
        String departure = "12:03:00";

        GtfsStopTime stopTime = GtfsEntitiyBuilder.buildStopTime(stopMapping.getGtfsId(), journeyMapping.getGtfsTripId(), arrival, departure);

        when(stopMapper.getMapping(stopMapping.getGtfsId())).thenReturn(stopMapping);
        when(journeyMapper.getMappingByTripId(journeyMapping.getGtfsTripId())).thenReturn(null);


        ScheduleProcessor processor = new ScheduleProcessor(stopMapper, journeyMapper);
        processor.setIdGenerator(idGenerator);

        Schedule schedule = processor.process(stopTime);

        verify(idGenerator, never()).next();

        assertThat(schedule, is(nullValue()));
    }

    @Autowired
    public void setStopMapper(StopMapperStore stopMapper) {
        this.stopMapper = stopMapper;
    }

    @Autowired
    public void setJourneyMapper(JourneyMapperStore journeyMapper) {
        this.journeyMapper = journeyMapper;
    }

    @Autowired
    public void setIdGenerator(UuidGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}