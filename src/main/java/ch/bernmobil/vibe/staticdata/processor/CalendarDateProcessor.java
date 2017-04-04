package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.CalendarDate;
import ch.bernmobil.vibe.staticdata.entity.sync.JourneyMapper;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsCalendarDate;
import java.sql.Date;
import java.text.SimpleDateFormat;
import org.springframework.batch.item.ItemProcessor;

public class CalendarDateProcessor implements ItemProcessor<GtfsCalendarDate, CalendarDate> {
    @Override
    public CalendarDate process(GtfsCalendarDate item) throws Exception {

        Date validFrom = new Date(new SimpleDateFormat("dd-MM-yyyy").parse("20-03-2017").getTime());
        Date validUntil = new Date(new SimpleDateFormat("dd-MM-yyyy").parse("22-03-2017").getTime());
        Long journeyId = JourneyMapper.getMappingByServiceId(item.getServiceId()).getId();

        Long gtfsServiceId = Long.parseLong(item.getServiceId());

        return new CalendarDate(validFrom, validUntil, journeyId, gtfsServiceId);
    }
}