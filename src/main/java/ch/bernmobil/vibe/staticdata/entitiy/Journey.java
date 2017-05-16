package ch.bernmobil.vibe.staticdata.entitiy;

import java.util.UUID;

public class Journey {
    private UUID id;
    private String headsign;
    private UUID route;


    private UUID terminalStation;

    public Journey(UUID id, String headsign, UUID route, UUID terminalStation) {
        this.id = id;
        this.headsign = headsign;
        this.route = route;
        this.terminalStation = terminalStation;
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

    public UUID getTerminalStation() {
        return terminalStation;
    }

    public void setTerminalStation(UUID terminalStation) {
        this.terminalStation = terminalStation;
    }
}
