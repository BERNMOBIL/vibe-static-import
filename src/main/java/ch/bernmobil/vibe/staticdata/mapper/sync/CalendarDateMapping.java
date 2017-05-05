package ch.bernmobil.vibe.staticdata.mapper.sync;

import java.util.UUID;

public class CalendarDateMapping {
    private long gtfsId;
    private UUID id;

    public CalendarDateMapping(long gtfsId, UUID id) {
        this.gtfsId = gtfsId;
        this.id = id;
    }

    public long getGtfsId() {
        return gtfsId;
    }

    public void setGtfsId(long gtfsId) {
        this.gtfsId = gtfsId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
