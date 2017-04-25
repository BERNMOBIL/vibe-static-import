package ch.bernmobil.vibe.staticdata.processor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.bernmobil.vibe.staticdata.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.mapper.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.CalendarDateMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapping;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Date;
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
    private MapperStore<Long, CalendarDateMapping> mapperStore;
    private JourneyMapperStore journeyMapperStore;
    private SequentialIdGenerator idGenerator;

    @Test
    public void processor() throws Exception {
        long calendarDateId = 1L;
        when(idGenerator.getId()).thenReturn(calendarDateId);
        String tripId = "01-trip";
        String serviceId = "0002";
        JourneyMapping journeyMapping = new JourneyMapping(tripId, serviceId, idGenerator.getId());
        when(journeyMapperStore.getMappingByServiceId(anyString())).thenReturn(journeyMapping);

        GtfsCalendarDate gtfsCalendarDate = buildCalendarDate(serviceId, "20170101");

        CalendarDateProcessor processor = new CalendarDateProcessor(idGenerator, mapperStore, journeyMapperStore);
        CalendarDate c = processor.process(gtfsCalendarDate);

        assertThat(c.getId(), is(calendarDateId));
        assertThat(c.getJourney(), is(calendarDateId));

        Date sqlDate = Date.valueOf("2017-01-01");
        assertThat(c.getValidFrom(), is(sqlDate));
        assertThat(c.getValidUntil(), is(sqlDate));

        JsonObject json = new JsonParser().parse("{service_days: [\"SUNDAY\"]}").getAsJsonObject();
        assertThat(c.getDays(), is(json));

        verify(mapperStore, times(1)).addMapping(eq(Long.parseLong(serviceId)), any(CalendarDateMapping.class));
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
            MapperStore<Long, CalendarDateMapping> mapperStore) {
        this.mapperStore = mapperStore;
    }

    @Autowired
    public void setIdGenerator(SequentialIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Autowired
    public void setJourneyMapperStore(
            JourneyMapperStore journeyMapperStore) {
        this.journeyMapperStore = journeyMapperStore;
    }
}