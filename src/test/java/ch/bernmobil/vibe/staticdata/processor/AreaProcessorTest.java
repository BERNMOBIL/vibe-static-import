package ch.bernmobil.vibe.staticdata.processor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.bernmobil.vibe.staticdata.entity.Area;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@ActiveProfiles("testConfiguration")
public class AreaProcessorTest {
    private MapperStore<String, AreaMapping> store;
    private SequentialIdGenerator idGenerator;

    @Test
    public void stopWithParentStation() throws Exception {
        long areaId = 1;
        when(idGenerator.getId()).thenReturn(areaId);

        AreaProcessor processor = new AreaProcessor(store);

        String stopId = "123";
        GtfsStop stop = buildGtfsStop("stop", stopId, "");

        Area a = processor.process(stop);

        verify(idGenerator, times(1)).next();
        verify(store, times(1)).addMapping(eq(stopId), any(AreaMapping.class));

        assertThat(a.getId(), is(areaId));
        assertThat(a.getName(), is("stop"));
    }

    @Test
    public void stopWithoutParentStation() throws Exception {
        long areaId = 1L;
        when(idGenerator.getId()).thenReturn(areaId);

        AreaProcessor processor = new AreaProcessor(store);

        String stopId = "123";
        GtfsStop stop = buildGtfsStop("stop", stopId, "111");

        Area a = processor.process(stop);

        verify(idGenerator, never()).next();
        verify(store, never()).addMapping(eq(stopId), any(AreaMapping.class));

        assertThat(a, is(nullValue()));

    }

    private GtfsStop buildGtfsStop(String stopName, String stopId, String parentStation){
        GtfsStop stop = new GtfsStop();
        stop.setStopName(stopName);
        stop.setStopId(stopId);
        stop.setParentStation(parentStation);
        return stop;
    }

    @Autowired
    public void setStore(
            MapperStore<String, AreaMapping> store) {
        this.store = store;
    }

    @Autowired
    public void setIdGenerator(SequentialIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}