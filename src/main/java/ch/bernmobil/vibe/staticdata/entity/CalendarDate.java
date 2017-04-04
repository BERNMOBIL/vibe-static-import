package ch.bernmobil.vibe.staticdata.entity;

import ch.bernmobil.vibe.staticdata.entity.sync.CalendarDateMapper;
import java.sql.Date;

public class CalendarDate {
  private static long idCounter = 0;
  private Long id;
  //private String days; //TODO: ADD days
  private java.sql.Date validFrom;
  private java.sql.Date validUntil;
  private Long journey;

  public CalendarDate(Date validFrom, Date validUntil, Long journey, Long gtfs_id) {
    this.id = ++idCounter;
    CalendarDateMapper.addMapping(gtfs_id, id);

    this.validFrom = validFrom;
    this.validUntil = validUntil;
    this.journey = journey;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public java.sql.Date getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(java.sql.Date validFrom) {
    this.validFrom = validFrom;
  }

  public java.sql.Date getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(java.sql.Date validUntil) {
    this.validUntil = validUntil;
  }

  public Long getJourney() {
    return journey;
  }

  public void setJourney(Long journey) {
    this.journey = journey;
  }
}
