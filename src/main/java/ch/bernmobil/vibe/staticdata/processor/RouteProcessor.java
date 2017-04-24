package ch.bernmobil.vibe.staticdata.processor;

import ch.bernmobil.vibe.staticdata.entity.Route;
import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsRoute;
import ch.bernmobil.vibe.staticdata.idprovider.SequentialIdGenerator;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapping;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouteProcessor implements ItemProcessor<GtfsRoute, Route> {
    private final SequentialIdGenerator idGenerator;
    private final MapperStore<String, RouteMapping> mapperStore;

    @Autowired
    public RouteProcessor(SequentialIdGenerator idGenerator,
            MapperStore<String, RouteMapping> mapperStore) {
        this.idGenerator = idGenerator;
        this.mapperStore = mapperStore;
    }

    @Override
    public Route process(GtfsRoute item) throws Exception {
        int type = Integer.parseInt(item.getRouteType());
        long id = idGenerator.getId();
        mapperStore.addMapping(item.getRouteId(), new RouteMapping(item.getRouteId(), id));
        idGenerator.next();
        return new Route(id, type, item.getRouteShortName());
    }
}