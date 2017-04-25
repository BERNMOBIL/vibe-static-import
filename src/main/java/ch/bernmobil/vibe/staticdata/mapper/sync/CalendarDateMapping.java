package ch.bernmobil.vibe.staticdata.mapper.sync;

public class CalendarDateMapping {
    private long gtfsId;
    private long id;

    public CalendarDateMapping(long gtfsId, long id) {
        this.gtfsId = gtfsId;
        this.id = id;
    }

    public long getGtfsId() {
        return gtfsId;
    }

    public void setGtfsId(long gtfsId) {
        this.gtfsId = gtfsId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
