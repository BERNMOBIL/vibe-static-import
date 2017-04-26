package ch.bernmobil.vibe.staticdata.entity;

public class Area {
    private long id;
    private String name;

    public Area(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
