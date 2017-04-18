package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Route;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsRoute;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapper;
import org.springframework.batch.item.ItemProcessor;

public class RouteProcessor implements ItemProcessor<GtfsRoute, Route> {
    private SequentialIdGenerator idGenerator = new SequentialIdGenerator();

    @Override
    public Route process(GtfsRoute item) throws Exception {
        int type = Integer.parseInt(item.getRouteType());
        long id = idGenerator.getId();
        RouteMapper.addMapping(item.getRouteId(), id);
        idGenerator.next();
        return new Route(id, type, item.getRouteShortName());
    }
}