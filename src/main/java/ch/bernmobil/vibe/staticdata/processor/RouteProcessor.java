package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Route;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsRoute;
import org.springframework.batch.item.ItemProcessor;

public class RouteProcessor implements ItemProcessor<GtfsRoute, Route> {
    @Override
    public Route process(GtfsRoute item) throws Exception {
        int type = Integer.parseInt(item.getRouteType());
        return new Route(type, item.getRouteId());
    }
}