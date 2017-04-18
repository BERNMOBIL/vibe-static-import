package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Timestamp;

public class JourneyDisruption {
  private long id;
  private String message;
  private Timestamp start;
  private Timestamp plannedEnd;
  private long journey;

  public JourneyDisruption(long id, String message, Timestamp start, Timestamp plannedEnd, long journey) {
    this.id = id;
    this.message = message;
    this.start = start;
    this.plannedEnd = plannedEnd;
    this.journey = journey;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Timestamp getStart() {
    return start;
  }

  public void setStart(Timestamp start) {
    this.start = start;
  }

  public Timestamp getPlannedEnd() {
    return plannedEnd;
  }

  public void setPlannedEnd(Timestamp plannedEnd) {
    this.plannedEnd = plannedEnd;
  }

  public long getJourney() {
    return journey;
  }

  public void setJourney(long journey) {
    this.journey = journey;
  }
}
