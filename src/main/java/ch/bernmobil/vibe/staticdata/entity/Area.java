package ch.bernmobil.vibe.staticdata.entity;

import java.util.UUID;

public class Area {
    private UUID id;
    private String name;

    public Area(UUID id, String name) {
        this.id = id;
        this.name = name;
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
}
