package ch.bernmobil.vibe.staticdata.mapper.sync;

public class JourneyMapping {
    private String gtfsTripId;
    private String gtfsServiceId;
    private long id;

    public JourneyMapping(String gtfsTripId, String gtfsServiceId, long id) {
        this.gtfsTripId = gtfsTripId;
        this.gtfsServiceId = gtfsServiceId;
        this.id = id;
    }

    public String getGtfsTripId() {
        return gtfsTripId;
    }

    public void setGtfsTripId(String gtfsTripId) {
        this.gtfsTripId = gtfsTripId;
    }

    public String getGtfsServiceId() {
        return gtfsServiceId;
    }

    public void setGtfsServiceId(String gtfsServiceId) {
        this.gtfsServiceId = gtfsServiceId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
