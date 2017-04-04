package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Time;

public class ScheduleUpdate {
  private Long id;
  private java.sql.Time actualArrival;
  private java.sql.Time actualDeparture;
  private Long schedule;

  public ScheduleUpdate(Long id, Time actualArrival, Time actualDeparture,
          Long schedule) {
    this.id = id;
    this.actualArrival = actualArrival;
    this.actualDeparture = actualDeparture;
    this.schedule = schedule;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public java.sql.Time getActualArrival() {
    return actualArrival;
  }

  public void setActualArrival(java.sql.Time actualArrival) {
    this.actualArrival = actualArrival;
  }

  public java.sql.Time getActualDeparture() {
    return actualDeparture;
  }

  public void setActualDeparture(java.sql.Time actualDeparture) {
    this.actualDeparture = actualDeparture;
  }

  public Long getSchedule() {
    return schedule;
  }

  public void setSchedule(Long schedule) {
    this.schedule = schedule;
  }
}
