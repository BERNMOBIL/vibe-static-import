package ch.bernmobil.vibe.staticdata.entity;

import java.util.UUID;

public class Route {
    private UUID id;
    private int type;
    private String line;

    public Route(UUID id, int type, String line) {
        this.id = id;
        this.type = type;
        this.line = line;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

}
