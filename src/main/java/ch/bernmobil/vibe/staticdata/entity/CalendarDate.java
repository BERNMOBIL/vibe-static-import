package ch.bernmobil.vibe.staticdata.entity;

import com.google.gson.JsonObject;
import java.sql.Date;

public class CalendarDate {
    private long id;
    private JsonObject days;
    private Date validFrom;
    private Date validUntil;
    private long journey;

    public CalendarDate(long id, Date validFrom, Date validUntil, long journey, JsonObject days) {
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

    public JsonObject getDays() {
        return days;
    }

    public void setDays(JsonObject days) {
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
