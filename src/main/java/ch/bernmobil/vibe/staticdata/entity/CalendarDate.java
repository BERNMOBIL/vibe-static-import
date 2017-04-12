package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Date;
import org.json.simple.JSONObject;

public class CalendarDate {
    private long id;
    private JSONObject days;
    private Date validFrom;
    private Date validUntil;
    private long journey;

    public CalendarDate(long id, Date validFrom, Date validUntil, long journey, JSONObject days) {
        this.id = id;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.journey = journey;
        this.days = days;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public JSONObject getDays() {
        return days;
    }

    public void setDays(JSONObject days) {
        this.days = days;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public long getJourney() {
        return journey;
    }

    public void setJourney(long journey) {
        this.journey = journey;
    }
}
