package ch.bernmobil.vibe.staticdata.processor;


import ch.bernmobil.vibe.staticdata.entitiy.Route;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsRoute;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapping;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RouteProcessor extends Processor<GtfsRoute, Route> {
    private final MapperStore<String, RouteMapping> mapperStore;

    @Autowired
    public RouteProcessor(@Qualifier("routeMapperStore") MapperStore<String, RouteMapping> mapperStore) {
        this.mapperStore = mapperStore;
    }

    @Override
    public Route process(GtfsRoute item) throws Exception {
        int type = Integer.parseInt(item.getRouteType());
        UUID id = idGenerator.getId();
        mapperStore.addMapping(item.getRouteId(), new RouteMapping(item.getRouteId(), id));
        idGenerator.next();
        return new Route(id, type, item.getRouteShortName());
    }
}