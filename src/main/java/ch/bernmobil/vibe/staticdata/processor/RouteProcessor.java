package ch.bernmobil.vibe.staticdata.processor;


import ch.bernmobil.vibe.shared.entitiy.Route;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsRoute;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
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
        UUID id = idGenerator.next();
        mapperStore.addMapping(item.getRouteId(), new RouteMapping(item.getRouteId(), id));
        return new Route(id, type, item.getRouteShortName());
    }
}