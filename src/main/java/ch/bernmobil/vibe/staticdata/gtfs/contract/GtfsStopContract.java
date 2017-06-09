package ch.bernmobil.vibe.staticdata.gtfs.contract;

/**
 * Contract to define properties of the "stops.txt" CSV file from GTFS static source.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public final class GtfsStopContract {
    public static final String FILENAME = "stops.txt";
    public static final String STOP_ID = "stop_id";
    public static final String STOP_CODE = "stop_code";
    public static final String STOP_NAME = "stop_name";
    public static final String STOP_DESC = "stop_desc";
    public static final String STOP_LAT = "stop_lat";
    public static final String STOP_LON = "stop_lon";
    public static final String ZONE_ID = "zone_id";
    public static final String STOP_URL = "stop_url";
    public static final String LOCATION_TYPE = "location_type";
    public static final String PARENT_STATION = "parent_station";
    public static final String[] FIELD_NAMES = {STOP_ID, STOP_CODE, STOP_NAME, STOP_DESC, STOP_LAT, STOP_LON, ZONE_ID, STOP_URL, LOCATION_TYPE, PARENT_STATION};

    private GtfsStopContract() {}
}
