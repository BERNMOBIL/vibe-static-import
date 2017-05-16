package ch.bernmobil.vibe.staticdata.entitiy;

import java.sql.Timestamp;
import java.util.UUID;

public class JourneyDisruption {
  private UUID id;
  private String message;
  private Timestamp start;
  private Timestamp plannedEnd;
  private UUID journey;

  public JourneyDisruption(UUID id, String message, Timestamp start, Timestamp plannedEnd, UUID journey) {
    this.id = id;
    this.message = message;
    this.start = start;
    this.plannedEnd = plannedEnd;
    this.journey = journey;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
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

  public UUID getJourney() {
    return journey;
  }

  public void setJourney(UUID journey) {
    this.journey = journey;
  }
}
