package ch.bernmobil.vibe.staticdata.entity;

import java.util.UUID;

public class Journey {
    private UUID id;
    private String headsign;
    private UUID route;

    public Journey(UUID id, String headsign, UUID route) {
        this.id = id;
        this.headsign = headsign;
        this.route = route;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHeadsign() {
        return headsign;
    }

    public void setHeadsign(String headsign) {
        this.headsign = headsign;
    }

    public UUID getRoute() {
        return route;
    }

    public void setRoute(UUID route) {
        this.route = route;
    }
}
