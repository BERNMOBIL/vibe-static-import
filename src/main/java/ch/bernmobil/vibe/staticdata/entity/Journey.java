package ch.bernmobil.vibe.staticdata.entity;

import ch.bernmobil.vibe.staticdata.mapper.sync.JourneyMapper;

public class Journey {
    private static long idCounter = 0;
    private Long id;
    private String headsign;
    private Long route;
    private Long journeyDisruption;

    public Journey(String headsign, Long route, String gtfs_id, String gtfs_service_id) {
        this.id = ++idCounter;
        JourneyMapper.addMapping(gtfs_id, gtfs_service_id, id);

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
