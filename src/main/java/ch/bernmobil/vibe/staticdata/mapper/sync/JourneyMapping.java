package ch.bernmobil.vibe.staticdata.mapper.sync;

import java.util.UUID;

public class JourneyMapping {
    private String gtfsTripId;
    private String gtfsServiceId;
    private UUID id;

    public JourneyMapping(String gtfsTripId, String gtfsServiceId, UUID id) {
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
