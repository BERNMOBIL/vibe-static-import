package ch.bernmobil.vibe.staticdata.entity;

public class Stop {
    private long id;
    private String name;
    private long area;

    public Stop(long id, String name, long areaId) {
        this.id = id;
        this.name = name;
        this.area = areaId;
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

    public void setName(String name) {
        this.name = name;
    }

    public long getArea() {
        return area;
    }

    public void setArea(long area) {
        this.area = area;
    }
}
