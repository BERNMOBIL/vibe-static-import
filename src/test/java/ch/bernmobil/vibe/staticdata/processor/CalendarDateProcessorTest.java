package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.shared.entitiy.CalendarDate;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.idprovider.UuidGenerator;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.testenvironment.GtfsEntitiyBuilder;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.JourneyMappingTestData;
import ch.bernmobil.vibe.staticdata.testenvironment.testdata.TestData;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@ActiveProfiles("testConfiguration")
public class CalendarDateProcessorTest {
    private MapperStore<String, CalendarDateMapping> mapperStore;
    private JourneyMapperStore journeyMapperStore;
    private UuidGenerator idGenerator;

    @Test
    public void processCalendarDate() throws Exception {
        TestData<JourneyMapping> testData = new JourneyMappingTestData();
        List<JourneyMapping> journeyMappingList = testData.getDataSource()
                .stream().limit(1).collect(Collectors.toList());

        JourneyMapping mapping = testData.get(0);
        String serviceId = mapping.getGtfsServiceId();

        when(idGenerator.getId()).thenReturn(mapping.getId());
        when(journeyMapperStore.getMappingsByServiceId(eq(serviceId))).thenReturn(journeyMappingList);

        String gtfsDate = "20170101";
        GtfsCalendarDate gtfsCalendarDate = GtfsEntitiyBuilder.buildCalendarDate(serviceId, gtfsDate);

        CalendarDateProcessor processor = new CalendarDateProcessor(mapperStore, journeyMapperStore);
        processor.setIdGenerator(idGenerator);
        List<CalendarDate> c = processor.process(gtfsCalendarDate);

        assertThat(c, hasSize(1));
        CalendarDate result = c.get(0);
        assertThat(result.getId(), is(mapping.getId()));
        assertThat(result.getJourney(), is(mapping.getId()));

        Date sqlDate = Date.valueOf("2017-01-01");
        assertThat(result.getValidFrom(), is(sqlDate));
        assertThat(result.getValidUntil(), is(sqlDate));

        JsonObject json = new JsonParser().parse("{service_days: [\"SUNDAY\"]}").getAsJsonObject();
        assertThat(result.getDays(), is(json));

        verify(mapperStore, times(1)).addMapping(eq(serviceId + gtfsDate), any(CalendarDateMapping.class));
        verify(idGenerator, times(1)).next();

    }

    @Test
    public void processCalendarDateNoJourneyMapping() throws Exception {
        UUID calendarDateId = UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95");
        when(idGenerator.getId()).thenReturn(calendarDateId);
        when(journeyMapperStore.getMappingByTripId(anyString())).thenReturn(null);

        String serviceId = "0002";
        GtfsCalendarDate gtfsCalendarDate = GtfsEntitiyBuilder.buildCalendarDate(serviceId, "20170101");

        CalendarDateProcessor processor = new CalendarDateProcessor(mapperStore, journeyMapperStore);
        processor.setIdGenerator(idGenerator);

        List<CalendarDate> c = processor.process(gtfsCalendarDate);
        assertThat(c, is(nullValue()));

        verify(mapperStore, never()).addMapping(anyString(), any(CalendarDateMapping.class));
        verify(idGenerator, never()).next();
    }

    @Test
    public void processCalendarDateMultipleJourneyMapping() throws Exception {
        TestData<JourneyMapping> testData = new JourneyMappingTestData();
        List<JourneyMapping> journeyMappingList = testData.getDataSource()
                .stream().limit(2).collect(Collectors.toList());

        JourneyMapping firstMapping = testData.get(0);
        JourneyMapping secondMapping = testData.get(1);
        when(journeyMapperStore.getMappingsByServiceId(eq(firstMapping.getGtfsServiceId()))).thenReturn(journeyMappingList);
        when(idGenerator.getId()).thenReturn(firstMapping.getId()).thenReturn(secondMapping.getId());

        String gtfsDate = "20170101";
        GtfsCalendarDate gtfsCalendarDate = GtfsEntitiyBuilder.buildCalendarDate(firstMapping.getGtfsServiceId(), gtfsDate);

        CalendarDateProcessor processor = new CalendarDateProcessor(mapperStore, journeyMapperStore);
        processor.setIdGenerator(idGenerator);

        List<CalendarDate> list = processor.process(gtfsCalendarDate);

        verify(mapperStore, times(2)).addMapping(anyString(), any(CalendarDateMapping.class));
        verify(idGenerator, times(2)).next();

        assertThat(list, hasSize(2));
        CalendarDate first = list.get(0);

        assertThat(first.getId(), is(firstMapping.getId()));
        assertThat(first.getJourney(), is(firstMapping.getId()));

        Date sqlDate = Date.valueOf("2017-01-01");
        assertThat(first.getValidFrom(), is(sqlDate));
        assertThat(first.getValidUntil(), is(sqlDate));

        JsonObject json = new JsonParser().parse("{service_days: [\"SUNDAY\"]}").getAsJsonObject();
        assertThat(first.getDays(), is(json));

        CalendarDate second = list.get(1);

        assertThat(second.getId(), is(secondMapping.getId()));
        assertThat(second.getJourney(), is(secondMapping.getId()));

        assertThat(second.getValidFrom(), is(sqlDate));
        assertThat(second.getValidUntil(), is(sqlDate));

        assertThat(second.getDays(), is(json));

    }

    @Autowired
    public void setMapperStore(MapperStore<String, CalendarDateMapping> mapperStore) {
        this.mapperStore = mapperStore;
    }

    @Autowired
    public void setIdGenerator(UuidGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Autowired
    public void setJourneyMapperStore(JourneyMapperStore journeyMapperStore) {
        this.journeyMapperStore = journeyMapperStore;
    }
}