package ch.bernmobil.vibe.staticdata.gtfs.contract;

/**
 * Contract to define properties of the "calendar_dates.txt" CSV file from GTFS static source.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public final class GtfsCalendarDateContract {
    public static final String FILENAME = "calendar_dates.txt";
    public static final String SERVICE_ID = "service_id";
    public static final String DATE = "date";
    public static final String EXCEPTION_TYPE = "exception_type";
    public static final String[] FIELD_NAMES = {SERVICE_ID, DATE, EXCEPTION_TYPE};

    private GtfsCalendarDateContract() {}
}
