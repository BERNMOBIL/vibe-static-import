package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Stop;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import org.springframework.batch.item.ItemProcessor;

public class StopProcessor implements ItemProcessor<GtfsStop, Stop> {
    @Override
    public Stop process(GtfsStop item) throws Exception {
        String parentStation = item.getParentStation();


        return parentStation != "" ? new Stop(item.getStopName(), parentStation, item.getStopId()) : null;
    }
}