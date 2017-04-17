package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Time;

public class ScheduleUpdate {
  private long id;
  private Time actualArrival;
  private Time actualDeparture;
  private long schedule;

  public ScheduleUpdate(long id, Time actualArrival, Time actualDeparture,
          long schedule) {
    this.id = id;
    this.actualArrival = actualArrival;
    this.actualDeparture = actualDeparture;
    this.schedule = schedule;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Time getActualArrival() {
    return actualArrival;
  }

  public void setActualArrival(Time actualArrival) {
    this.actualArrival = actualArrival;
  }

  public Time getActualDeparture() {
    return actualDeparture;
  }

  public void setActualDeparture(Time actualDeparture) {
    this.actualDeparture = actualDeparture;
  }

  public long getSchedule() {
    return schedule;
  }

  public void setSchedule(long schedule) {
    this.schedule = schedule;
  }
}
