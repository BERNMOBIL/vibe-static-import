package ch.bernmobil.vibe.staticdata.entity;

public class Route {
    private long id;
    private int type;
    private String line;

    public Route(long id, int type, String line) {
        this.id = id;
        this.type = type;
        this.line = line;
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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

}
