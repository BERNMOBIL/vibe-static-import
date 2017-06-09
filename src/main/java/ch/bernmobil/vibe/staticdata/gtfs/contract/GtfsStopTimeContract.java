package ch.bernmobil.vibe.staticdata.gtfs.contract;

/**
 * Contract to define properties of the "stop_times.txt" CSV file from GTFS static source.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public final class GtfsStopTimeContract {
    public static final String FILENAME = "stop_times.txt";
    public static final String TRIP_ID = "trip_id";
    public static final String ARRIVAL_TIME = "arrival_time";
    public static final String DEPARTURE_TIME = "departure_time";
    public static final String STOP_ID = "stop_id";
    public static final String STOP_SEQUENCE = "stop_sequence";
    public static final String STOP_HEADSIGN = "stop_headsign";
    public static final String PICKUP_TYPE = "pickup_type";
    public static final String DROP_OFF_TYPE = "drop_off_type";
    public static final String SHAPE_DIST_TRAVELED = "shape_dist_traveled";
    public static final String[] FIELD_NAMES = {TRIP_ID, ARRIVAL_TIME, DEPARTURE_TIME, STOP_ID, STOP_SEQUENCE, STOP_HEADSIGN, PICKUP_TYPE, DROP_OFF_TYPE, SHAPE_DIST_TRAVELED};

    private GtfsStopTimeContract() {}
}
