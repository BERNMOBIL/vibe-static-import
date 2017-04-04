package ch.bernmobil.vibe.staticdata.entity;

import java.sql.Timestamp;

public class JourneyDisruption {
  private Long id;
  private String message;
  private java.sql.Timestamp start;
  private java.sql.Timestamp plannedEnd;
  private Long journey;

  public JourneyDisruption(Long id, String message, Timestamp start, Timestamp plannedEnd, Long journey) {
    this.id = id;
    this.message = message;
    this.start = start;
    this.plannedEnd = plannedEnd;
    this.journey = journey;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public java.sql.Timestamp getStart() {
    return start;
  }

  public void setStart(java.sql.Timestamp start) {
    this.start = start;
  }

  public java.sql.Timestamp getPlannedEnd() {
    return plannedEnd;
  }

  public void setPlannedEnd(java.sql.Timestamp plannedEnd) {
    this.plannedEnd = plannedEnd;
  }

  public Long getJourney() {
    return journey;
  }

  public void setJourney(Long journey) {
    this.journey = journey;
  }
}
