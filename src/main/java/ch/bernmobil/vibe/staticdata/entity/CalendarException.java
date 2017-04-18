package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Date;

public class CalendarException {
  private long id;
  private Date date;
  private String type;
  private long calendarDate;

  public CalendarException(long id, Date date, String type, long calendarDate) {
    this.id = id;
    this.date = date;
    this.type = type;
    this.calendarDate = calendarDate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
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

  public long getCalendarDate() {
    return calendarDate;
  }

  public void setCalendarDate(long calendarDate) {
    this.calendarDate = calendarDate;
  }
}
