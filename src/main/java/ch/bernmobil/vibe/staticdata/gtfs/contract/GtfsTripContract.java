package ch.bernmobil.vibe.staticdata.gtfs.contract;

/**
 * Contract to define properties of the "trips.txt" CSV file from GTFS static source.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public final class GtfsTripContract {
    public static final String FILENAME = "trips.txt";
    public static final String ROUTE_ID = "route_id";
    public static final String SERVICE_ID = "service_id";
    public static final String TRIP_ID = "trip_id";
    public static final String TRIP_HEADSIGN = "trip_headsign";
    public static final String TRIP_SHORT_NAME = "trip_short_name";
    public static final String DIRECTION_ID = "direction_id";
    public static final String[] FIELD_NAMES = {ROUTE_ID, SERVICE_ID, TRIP_ID, TRIP_HEADSIGN, TRIP_SHORT_NAME, DIRECTION_ID};

    private GtfsTripContract() {}
}
