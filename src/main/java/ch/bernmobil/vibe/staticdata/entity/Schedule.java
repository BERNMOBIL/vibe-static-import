package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Time;

public class Schedule {
  private long id;
  private String platform;
  private Time plannedArrival;
  private Time plannedDeparture;
  private long stop;
  private long journey;

  public Schedule(long id, String platform, Time plannedArrival, Time plannedDeparture, long stop, long journey) {
    this.id = id;

    this.platform = platform;
    this.plannedArrival = plannedArrival;
    this.plannedDeparture = plannedDeparture;
    this.stop = stop;
    this.journey = journey;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public Time getPlannedArrival() {
    return plannedArrival;
  }

  public void setPlannedArrival(Time plannedArrival) {
    this.plannedArrival = plannedArrival;
  }

  public Time getPlannedDeparture() {
    return plannedDeparture;
  }

  public void setPlannedDeparture(Time plannedDeparture) {
    this.plannedDeparture = plannedDeparture;
  }

  public long getStop() {
    return stop;
  }

  public void setStop(long stop) {
    this.stop = stop;
  }

  public long getJourney() {
    return journey;
  }

  public void setJourney(long journey) {
    this.journey = journey;
  }

}
