package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Time;
import java.util.UUID;

public class Schedule {
  private UUID id;
  private String platform;
  private Time plannedArrival;
  private Time plannedDeparture;
  private UUID stop;
  private UUID journey;

  public Schedule(UUID id, String platform, Time plannedArrival, Time plannedDeparture, UUID stop, UUID journey) {
    this.id = id;

    this.platform = platform;
    this.plannedArrival = plannedArrival;
    this.plannedDeparture = plannedDeparture;
    this.stop = stop;
    this.journey = journey;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
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

  public UUID getStop() {
    return stop;
  }

  public void setStop(UUID stop) {
    this.stop = stop;
  }

  public UUID getJourney() {
    return journey;
  }

  public void setJourney(UUID journey) {
    this.journey = journey;
  }

}
