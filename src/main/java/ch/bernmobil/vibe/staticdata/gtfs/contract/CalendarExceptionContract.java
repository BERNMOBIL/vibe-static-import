package ch.bernmobil.vibe.staticdata.gtfs.contract;

@SuppressWarnings("All")
public final class CalendarExceptionContract {
    public static final String TABLE_NAME = "calendar_exception";
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String TYPE = "type";
    public static final String CALENDAR_DATE = "calendar_date";
    public static final String UPDATE = "update";

    public static final String[] COLUMNS = {ID, DATE, TYPE, CALENDAR_DATE, UPDATE};
    private CalendarExceptionContract(){}
}
