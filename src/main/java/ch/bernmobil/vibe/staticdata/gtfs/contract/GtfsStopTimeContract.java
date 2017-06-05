package ch.bernmobil.vibe.staticdata.gtfs.contract;


public final class GtfsStopTimeContract {
    public final static String FILENAME = "stop_times.txt";
    public final static String[] FIELD_NAMES = {"trip_id" , "arrival_time", "departure_time", "stop_id", "stop_sequence", "stop_headsign", "pickup_type", "drop_off_type", "shape_dist_traveled"};
}
