package ch.bernmobil.vibe.staticdata.mapper.sync;

public class StopMapping {
    private String gtfsId;
    private long id;

    public StopMapping(String gtfsId, long id) {
        this.gtfsId = gtfsId;
        this.id = id;
    }

    public String getGtfsId() {
        return gtfsId;
    }

    public void setGtfsId(String gtfsId) {
        this.gtfsId = gtfsId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
