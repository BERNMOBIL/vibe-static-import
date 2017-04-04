package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Area;
import ch.bernmobil.vibe.staticdata.entity.Stop;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import org.springframework.batch.item.ItemProcessor;

public class AreaProcessor implements ItemProcessor<GtfsStop, Area> {
    @Override
    public Area process(GtfsStop item) throws Exception {
        String name = item.getStopName();
        String parentStation = item.getParentStation();

        return parentStation.equals("") ? new Area(name, item.getStopId()) : null;
    }
}