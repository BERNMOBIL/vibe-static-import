package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Time;

public class Schedule {
  private static long idCounter = 0;

  private Long id;

  private String platform;
  private java.sql.Time plannedArrival;
  private java.sql.Time plannedDeparture;
  private Long stop;
  private Long journey;
  private Long scheduleUpdate;

  public Schedule(String platform, Time plannedArrival, Time plannedDeparture, Long stop, Long journey) {
    this.id = ++idCounter;

    this.platform = platform;
    this.plannedArrival = plannedArrival;
    this.plannedDeparture = plannedDeparture;
    this.stop = stop;
    this.journey = journey;
  }

  public static long getIdCounter() {
    return idCounter;
  }

  public static void setIdCounter(long idCounter) {
    Schedule.idCounter = idCounter;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public Long getStop() {
    return stop;
  }

  public void setStop(Long stop) {
    this.stop = stop;
  }

  public Long getJourney() {
    return journey;
  }

  public void setJourney(Long journey) {
    this.journey = journey;
  }

  public Long getScheduleUpdate() {
    return scheduleUpdate;
  }

  public void setScheduleUpdate(Long scheduleUpdate) {
    this.scheduleUpdate = scheduleUpdate;
  }
}
