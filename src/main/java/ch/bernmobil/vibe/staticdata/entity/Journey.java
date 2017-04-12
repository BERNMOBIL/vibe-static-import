package ch.bernmobil.vibe.staticdata.entity;

public class Journey {
    private Long id;
    private String headsign;
    private Long route;
    private Long journeyDisruption;

    public Journey(long id, String headsign, Long route) {
        this.id = id;
        this.headsign = headsign;
        this.route = route;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadsign() {
        return headsign;
    }

    public void setHeadsign(String headsign) {
        this.headsign = headsign;
    }

    public Long getRoute() {
        return route;
    }

    public void setRoute(Long route) {
        this.route = route;
    }

    public Long getJourneyDisruption() {
        return journeyDisruption;
    }

    public void setJourneyDisruption(Long journeyDistruption) {
        this.journeyDisruption = journeyDistruption;
    }
}
