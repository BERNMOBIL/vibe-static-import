package ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsRouteContract;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsRoute;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Provides mapping between a single row in the "routes.txt" and {@link GtfsRoute}
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class RouteFieldSetMapper implements FieldSetMapper<GtfsRoute> {

    @Override
    public GtfsRoute mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsRoute route = new GtfsRoute();
        route.setRouteId(fieldSet.readString(GtfsRouteContract.ROUTE_ID));
        route.setAgencyId(fieldSet.readString(GtfsRouteContract.AGENCY_ID));
        route.setRouteShortName(fieldSet.readString(GtfsRouteContract.ROUTE_SHORT_NAME));
        route.setRouteLongName(fieldSet.readString(GtfsRouteContract.ROUTE_LONG_NAME));
        route.setRouteDesc(fieldSet.readString(GtfsRouteContract.ROUTE_DESC));
        route.setRouteType(fieldSet.readString(GtfsRouteContract.ROUTE_TYPE));
        route.setRouteUrl(fieldSet.readString(GtfsRouteContract.ROUTE_URL));
        route.setRouteColor(fieldSet.readString(GtfsRouteContract.ROUTE_COLOR));
        route.setRouteTextColor(fieldSet.readString(GtfsRouteContract.ROUTE_TEXT_COLOR));
        return route;
    }

}
