package ch.bernmobil.vibe.staticdata.mapper.sync;

import java.util.UUID;

public class StopMapping {
    private String gtfsId;
    private UUID id;

    public StopMapping(String gtfsId, UUID id) {
        this.gtfsId = gtfsId;
        this.id = id;
    }

    public String getGtfsId() {
        return gtfsId;
    }

    public void setGtfsId(String gtfsId) {
        this.gtfsId = gtfsId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
