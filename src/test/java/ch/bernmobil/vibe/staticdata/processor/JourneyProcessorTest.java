package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entitiy.Journey;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsTrip;
import ch.bernmobil.vibe.staticdata.idprovider.UuidGenerator;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.StopMapperStore;
import ch.bernmobil.vibe.staticdata.testenvironment.GtfsEntitiyBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@ActiveProfiles("testConfiguration")
public class JourneyProcessorTest {
    private MapperStore<String, RouteMapping> routeMapperStore;
    private JourneyMapperStore journeyMapperStore;
    private StopMapperStore stopMapperStore;
    private UuidGenerator idGenerator;

    @Test
    public void process() throws Exception {
        String routeId = "1-route";
        String stopName = "Rapperswil";
        String tripId = "1-trip";
        GtfsTrip trip = GtfsEntitiyBuilder.buildTrip(stopName, tripId, routeId, "1-service");

        UUID uuid = UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95");
        RouteMapping routeMapping = new RouteMapping(routeId, uuid);
        when(routeMapperStore.getMapping(eq(routeId))).thenReturn(routeMapping);

        StopMapping stopMapping = new StopMapping("1-stop", stopName, uuid);
        when(stopMapperStore.getStopByStopName(eq(stopName))).thenReturn(stopMapping);

        when(idGenerator.getId()).thenReturn(uuid);

        JourneyProcessor processor = new JourneyProcessor(routeMapperStore, journeyMapperStore, stopMapperStore);
        processor.setIdGenerator(idGenerator);

        Journey journey = processor.process(trip);

        verify(journeyMapperStore, times(1)).addMapping(eq(tripId), any(JourneyMapping.class));
        verify(idGenerator, times(1)).next();

        assertThat(journey.getId(), is(uuid));
        assertThat(journey.getHeadsign(), is(stopName));
        assertThat(journey.getRoute(), is(uuid));
        assertThat(journey.getTerminalStation(), is(uuid));


    }

    @Test
    public void processNoTerminalStop() throws Exception {
        String routeId = "1-route";
        String stopName = "Rapperswil";
        String tripId = "1-trip";
        GtfsTrip trip = GtfsEntitiyBuilder.buildTrip(stopName, tripId, routeId, "1-service");

        UUID uuid = UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95");
        RouteMapping routeMapping = new RouteMapping(routeId, uuid);
        when(routeMapperStore.getMapping(eq(routeId))).thenReturn(routeMapping);

        when(stopMapperStore.getStopByStopName(eq(stopName))).thenReturn(null);

        when(idGenerator.getId()).thenReturn(uuid);

        JourneyProcessor processor = new JourneyProcessor(routeMapperStore, journeyMapperStore, stopMapperStore);
        processor.setIdGenerator(idGenerator);

        Journey journey = processor.process(trip);

        verify(journeyMapperStore, times(1)).addMapping(eq(tripId), any(JourneyMapping.class));
        verify(idGenerator, times(1)).next();

        assertThat(journey.getId(), is(uuid));
        assertThat(journey.getHeadsign(), is(stopName));
        assertThat(journey.getRoute(), is(uuid));
        assertThat(journey.getTerminalStation(), is(nullValue()));

    }

    @Autowired
    public void setRouteMapperStore(MapperStore<String, RouteMapping> routeMapperStore) {
        this.routeMapperStore = routeMapperStore;
    }

    @Autowired
    public void setJourneyMapperStore(JourneyMapperStore journeyMapperStore) {
        this.journeyMapperStore = journeyMapperStore;
    }

    @Autowired
    public void setIdGenerator(UuidGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Autowired
    public void setStopMapperStore(StopMapperStore stopMapperStore) {
        this.stopMapperStore = stopMapperStore;
    }
}