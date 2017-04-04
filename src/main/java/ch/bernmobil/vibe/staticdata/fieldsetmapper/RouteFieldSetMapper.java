package ch.bernmobil.vibe.staticdata.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsRoute;
import org.apache.log4j.Logger;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class RouteFieldSetMapper implements FieldSetMapper<GtfsRoute> {
    private final static Logger logger = Logger.getLogger(RouteFieldSetMapper.class);

    @Override
    public GtfsRoute mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsRoute route = new GtfsRoute();

        route.setRouteId(fieldSet.readString("route_id"));
        route.setAgencyId(fieldSet.readString("agency_id"));
        route.setRouteShortName(fieldSet.readString("route_short_name"));
        route.setRouteLongName(fieldSet.readString("route_long_name"));
        route.setRouteDesc(fieldSet.readString("route_desc"));
        route.setRouteType(fieldSet.readString("route_type"));
        route.setRouteUrl(fieldSet.readString("route_url"));
        route.setRouteColor(fieldSet.readString("route_color"));
        route.setRouteTextColor(fieldSet.readString("route_text_color"));
        return route;
    }

}
