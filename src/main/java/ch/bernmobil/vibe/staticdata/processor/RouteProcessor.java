package ch.bernmobil.vibe.staticdata.processor;


import ch.bernmobil.vibe.shared.entitiy.Route;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsRoute;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Class to convert {@link GtfsRoute} into a {@link Route}.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Component
public class RouteProcessor extends Processor<GtfsRoute, Route> {
    private final MapperStore<String, RouteMapping> mapperStore;

    /**
     * Constructor demanding a {@link MapperStore} which stores {@link RouteMapping}.
     * @param mapperStore to save {@link RouteMapping}
     */
    @Autowired
    public RouteProcessor(@Qualifier("routeMapperStore") MapperStore<String, RouteMapping> mapperStore) {
        this.mapperStore = mapperStore;
    }

    /**
     * Process a {@link GtfsRoute}, extract all necessary information and save it into a {@link Route}. This method
     * save {@link RouteMapping} information in {@link #mapperStore}.
     * @param item to be processed
     * @return {@link Route} which contains all necessary information from {@link GtfsRoute}
     * @throws Exception will be thrown if there is a {@link RuntimeException} during processing.
     */
    @Override
    public Route process(GtfsRoute item) throws Exception {
        int type = Integer.parseInt(item.getRouteType());
        UUID id = idGenerator.next();
        mapperStore.addMapping(item.getRouteId(), new RouteMapping(item.getRouteId(), id));
        return new Route(id, type, item.getRouteShortName());
    }
}