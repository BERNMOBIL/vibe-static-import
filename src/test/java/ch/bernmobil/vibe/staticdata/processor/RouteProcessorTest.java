package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entitiy.Route;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsRoute;
import ch.bernmobil.vibe.staticdata.idprovider.UuidGenerator;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@ActiveProfiles("testConfiguration")
public class RouteProcessorTest {
    private MapperStore<String, RouteMapping> routeMapperStore;
    private UuidGenerator idGenerator;

    @Test
    public void process() throws Exception {
        GtfsRoute gtfsRoute = new GtfsRoute();
        String routeId = "1-gtfsRoute";
        gtfsRoute.setRouteId(routeId);
        String routeType = "2";
        gtfsRoute.setRouteType(routeType);
        String line = "M23";
        gtfsRoute.setRouteShortName(line);

        UUID uuid = UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95");

        when(idGenerator.next()).thenReturn(uuid);

        RouteProcessor processor = new RouteProcessor(routeMapperStore);
        processor.setIdGenerator(idGenerator);

        Route route = processor.process(gtfsRoute);

        verify(routeMapperStore, times(1)).addMapping(eq(routeId), any(RouteMapping.class));

        assertThat(route.getId(), is(uuid));
        assertThat(route.getType(), is(Integer.parseInt(routeType)));
        assertThat(route.getLine(), is(line));
    }

    @Autowired
    public void setRouteMapperStore(MapperStore<String, RouteMapping> mapperStore) {
        this.routeMapperStore = mapperStore;
    }

    @Autowired
    public void setIdGenerator(UuidGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}