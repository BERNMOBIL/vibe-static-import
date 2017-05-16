package ch.bernmobil.vibe.staticdata.entitiy;

import java.sql.Date;
import java.util.UUID;

public class CalendarException {
  private UUID id;
  private Date date;
  private String type;
  private UUID calendarDate;

  public CalendarException(UUID id, Date date, String type, UUID calendarDate) {
    this.id = id;
    this.date = date;
    this.type = type;
    this.calendarDate = calendarDate;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public UUID getCalendarDate() {
    return calendarDate;
  }

  public void setCalendarDate(UUID calendarDate) {
    this.calendarDate = calendarDate;
  }
}
