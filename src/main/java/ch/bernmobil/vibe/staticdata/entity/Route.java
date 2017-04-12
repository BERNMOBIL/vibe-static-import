package ch.bernmobil.vibe.staticdata.entity;

public class Route {
    private long id;
    private Integer type;

    public Route(long id, int type) {
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
