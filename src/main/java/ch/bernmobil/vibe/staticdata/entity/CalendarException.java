package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Date;

public class CalendarException {
  private long id;
  private java.sql.Date date;
  private String type;
  private long calendar_date;

  public CalendarException(long id, Date date, String type, long calendar_date) {
    this.id = id;
    this.date = date;
    this.type = type;
    this.calendar_date = calendar_date;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public java.sql.Date getDate() {
    return date;
  }

  public void setDate(java.sql.Date date) {
    this.date = date;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public long getCalendar_date() {
    return calendar_date;
  }

  public void setCalendar_date(long calendar_date) {
    this.calendar_date = calendar_date;
  }
}
