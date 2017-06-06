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

import ch.bernmobil.vibe.shared.entitiy.Area;
import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsStop;
import ch.bernmobil.vibe.staticdata.idprovider.UuidGenerator;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import java.util.UUID;

import ch.bernmobil.vibe.staticdata.testenvironment.GtfsEntitiyBuilder;
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
    private UuidGenerator idGenerator;

    @Test
    public void stopWithParentStation() throws Exception{
        UUID areaId = UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95");
        when(idGenerator.getId()).thenReturn(areaId);

        AreaProcessor processor = new AreaProcessor(store);
        processor.setIdGenerator(idGenerator);

        String stopId = "123";
        GtfsStop stop = GtfsEntitiyBuilder.buildStop("stop", stopId, "");

        Area a = processor.process(stop);

        verify(idGenerator, times(1)).next();
        verify(store, times(1)).addMapping(eq(stopId), any(AreaMapping.class));

        assertThat(a.getId(), is(areaId));
        assertThat(a.getName(), is("stop"));
    }

    @Test
    public void stopWithoutParentStation() throws Exception {
        UUID areaId = UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95");
        when(idGenerator.getId()).thenReturn(areaId);

        AreaProcessor processor = new AreaProcessor(store);
        processor.setIdGenerator(idGenerator);

        String stopId = "123";
        GtfsStop stop = GtfsEntitiyBuilder.buildStop("stop", stopId, "111");

        Area a = processor.process(stop);

        verify(idGenerator, never()).next();
        verify(store, never()).addMapping(eq(stopId), any(AreaMapping.class));

        assertThat(a, is(nullValue()));

    }

    @Autowired
    public void setStore(MapperStore<String, AreaMapping> mapperStore) {
        this.store = mapperStore;
    }

    @Autowired
    public void setIdGenerator(UuidGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}