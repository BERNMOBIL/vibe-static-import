package ch.bernmobil.vibe.staticdata.entitiy;

import com.google.gson.JsonObject;
import java.sql.Date;
import java.util.UUID;

public class CalendarDate {
    private UUID id;
    private JsonObject days;
    private Date validFrom;
    private Date validUntil;
    private UUID journey;

    public CalendarDate(UUID id, Date validFrom, Date validUntil, UUID journey, JsonObject days) {
        this.id = id;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.journey = journey;
        this.days = days;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public UUID getJourney() {
        return journey;
    }

    public void setJourney(UUID journey) {
        this.journey = journey;
    }
}

