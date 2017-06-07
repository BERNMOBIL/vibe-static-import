package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entitiy.Stop;
import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsStop;
import ch.bernmobil.vibe.staticdata.idprovider.UuidGenerator;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.StopMapperStore;
import ch.bernmobil.vibe.staticdata.testenvironment.GtfsEntitiyBuilder;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.mapping.StopMappingTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@ActiveProfiles("testConfiguration")
public class StopProcessorTest {
    private StopMapperStore stopMapper;
    private MapperStore<String, AreaMapping> areaMapper;
    private UuidGenerator idGenerator;

    @Test
    public void stopWithoutParentStation() throws Exception {
        StopProcessor processor = new StopProcessor(stopMapper, areaMapper);
        processor.setIdGenerator(idGenerator);

        String stopId = "123";
        GtfsStop stop = GtfsEntitiyBuilder.buildStop("Rapperswil", stopId, "");

        Stop a = processor.process(stop);

        verify(idGenerator, never()).next();
        verify(stopMapper, never()).addMapping(eq(stopId), any(StopMapping.class));

        assertThat(a, is(nullValue()));

    }

    @Test
    public void stopWithParentStation() throws Exception {
        StopMappingTestData testData = new StopMappingTestData();
        StopMapping stopMapping = testData.get(0);
        UUID uuid = stopMapping.getId();
        String areaId = "1-area";
        AreaMapping areaMapping = new AreaMapping(areaId, uuid);

        when(stopMapper.getMapping(stopMapping.getGtfsId())).thenReturn(stopMapping);
        when(areaMapper.getMapping(areaId)).thenReturn(areaMapping);
        when(idGenerator.next()).thenReturn(uuid);

        GtfsStop gtfsStop = GtfsEntitiyBuilder.buildStop(stopMapping.getName(), stopMapping.getGtfsId(), areaId);

        StopProcessor processor = new StopProcessor(stopMapper, areaMapper);
        processor.setIdGenerator(idGenerator);

        Stop stop = processor.process(gtfsStop);

        assertThat(stop, not(nullValue()));
        assertThat(stop.getArea(), is(uuid));
        assertThat(stop.getId(), is(uuid));
        assertThat(stop.getName(), is(stopMapping.getName()));
    }

    @Autowired
    public void setStopMapper(StopMapperStore stopMapper) {
        this.stopMapper = stopMapper;
    }

    @Autowired
    public void setAreaMapper(MapperStore<String, AreaMapping> mapperStore) {
        this.areaMapper = mapperStore;
    }

    @Autowired
    public void setIdGenerator(UuidGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}