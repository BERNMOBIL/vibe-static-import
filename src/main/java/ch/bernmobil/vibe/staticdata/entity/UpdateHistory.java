package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Timestamp;

public class UpdateHistory {
    private int id;
    private Timestamp time;
    private String status;

    public UpdateHistory(int id, Timestamp time, String status) {
        this.id = id;
        this.time = time;
        this.status = status;
    }
    public UpdateHistory(Timestamp time, String status) {
        this.time = time;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
