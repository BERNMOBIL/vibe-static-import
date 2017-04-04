package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Date;

public class CalendarException {
  private Long id;
  private java.sql.Date date;
  private String type;
  private Long calendar_date;

  public CalendarException(Long id, Date date, String type, Long calendar_date) {
    this.id = id;
    this.date = date;
    this.type = type;
    this.calendar_date = calendar_date;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public Long getCalendar_date() {
    return calendar_date;
  }

  public void setCalendar_date(Long calendar_date) {
    this.calendar_date = calendar_date;
  }
}
