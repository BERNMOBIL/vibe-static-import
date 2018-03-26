package ch.bernmobil.vibe.staticdata.gtfs.contract;

/**
 * Contract to define properties of the "routes.txt" CSV file from GTFS static source.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public final class GtfsRouteContract {
    public static final String FILENAME = "routes.txt";
    public static final String ROUTE_ID = "route_id";
    public static final String AGENCY_ID = "agency_id";
    public static final String ROUTE_SHORT_NAME = "route_short_name";
    public static final String ROUTE_LONG_NAME = "route_long_name";
    public static final String ROUTE_DESC = "route_desc";
    public static final String ROUTE_TYPE = "route_type";
    public static final String[] FIELD_NAMES = {ROUTE_ID, AGENCY_ID, ROUTE_SHORT_NAME, ROUTE_LONG_NAME, ROUTE_DESC, ROUTE_TYPE};

    private GtfsRouteContract() {}
}



