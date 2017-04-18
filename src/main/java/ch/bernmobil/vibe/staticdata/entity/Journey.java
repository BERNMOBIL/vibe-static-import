package ch.bernmobil.vibe.staticdata.entity;

public class Journey {
    private long id;
    private String headsign;
    private long route;

    public Journey(long id, String headsign, long route) {
        this.id = id;
        this.headsign = headsign;
        this.route = route;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeadsign() {
        return headsign;
    }

    public void setHeadsign(String headsign) {
        this.headsign = headsign;
    }

    public long getRoute() {
        return route;
    }

    public void setRoute(long route) {
        this.route = route;
    }
}
