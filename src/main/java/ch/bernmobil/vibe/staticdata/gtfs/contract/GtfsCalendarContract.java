package ch.bernmobil.vibe.staticdata.gtfs.contract;

/**
 * Contract to define properties of the "calendar_dates.txt" CSV file from GTFS static source.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public final class GtfsCalendarContract {
    public static final String FILENAME = "calendar.txt";
    public static final String SERVICE_ID = "service_id";
    public static final String MONDAY = "monday";
    public static final String TUESDAY = "tuesday";
    public static final String WEDNESDAY = "wednesday";
    public static final String THURSDAY = "thursday";
    public static final String FRIDAY = "friday";
    public static final String SATURDAY = "saturday";
    public static final String SUNDAY = "sunday";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String[] FIELD_NAMES = {SERVICE_ID, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, START_DATE, END_DATE};

    private GtfsCalendarContract() {}
}
