package ch.bernmobil.vibe.staticdata.processor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.bernmobil.vibe.shared.entitiy.CalendarDate;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.idprovider.UuidGenerator;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@ActiveProfiles("testConfiguration")
public class CalendarDateProcessorTest {
    private MapperStore<String, CalendarDateMapping> mapperStore;
    private JourneyMapperStore journeyMapperStore;
    private UuidGenerator idGenerator;

    @Test
    public void processor() throws Exception {
        UUID calendarDateId = UUID.fromString("92e1a0ef-91ef-4850-baa6-4cb6e243bf95");
        when(idGenerator.getId()).thenReturn(calendarDateId);
        String tripId = "01-trip";
        String serviceId = "0002";
        JourneyMapping journeyMapping = new JourneyMapping(tripId, serviceId, idGenerator.getId());
        List<JourneyMapping> journeyMappingList = new ArrayList<>();
        journeyMappingList.add(journeyMapping);
        when(journeyMapperStore.getMappingsByServiceId(anyString())).thenReturn(journeyMappingList);

        GtfsCalendarDate gtfsCalendarDate = buildCalendarDate(serviceId, "20170101");

        CalendarDateProcessor processor = new CalendarDateProcessor(mapperStore, journeyMapperStore);
        processor.setIdGenerator(idGenerator);
        List<CalendarDate> c = processor.process(gtfsCalendarDate);

        assertThat(c.get(0).getId(), is(calendarDateId));
        assertThat(c.get(0).getJourney(), is(calendarDateId));

        Date sqlDate = Date.valueOf("2017-01-01");
        assertThat(c.get(0).getValidFrom(), is(sqlDate));
        assertThat(c.get(0).getValidUntil(), is(sqlDate));

        JsonObject json = new JsonParser().parse("{service_days: [\"SUNDAY\"]}").getAsJsonObject();
        assertThat(c.get(0).getDays(), is(json));

        verify(mapperStore, times(1)).addMapping(anyString(), any(CalendarDateMapping.class));
        verify(idGenerator, times(1)).next();

    }

    private GtfsCalendarDate buildCalendarDate(String serviceId, String date) {
        GtfsCalendarDate calendarDate = new GtfsCalendarDate();
        calendarDate.setDate(date);
        calendarDate.setServiceId(serviceId);
        return calendarDate;
    }

    @Autowired
    public void setMapperStore(
            MapperStore<String, CalendarDateMapping> mapperStore) {
        this.mapperStore = mapperStore;
    }

    @Autowired
    public void setIdGenerator(UuidGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Autowired
    public void setJourneyMapperStore(
            JourneyMapperStore journeyMapperStore) {
        this.journeyMapperStore = journeyMapperStore;
    }
}