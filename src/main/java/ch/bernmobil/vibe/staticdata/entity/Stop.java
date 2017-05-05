package ch.bernmobil.vibe.staticdata.entity;

import java.util.UUID;

public class Stop {
    private UUID id;
    private String name;
    private UUID area;

    public Stop(UUID id, String name, UUID areaId) {
        this.id = id;
        this.name = name;
        this.area = areaId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getArea() {
        return area;
    }

    public void setArea(UUID area) {
        this.area = area;
    }
}
