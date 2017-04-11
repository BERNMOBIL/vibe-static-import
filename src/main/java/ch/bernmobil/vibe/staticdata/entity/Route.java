package ch.bernmobil.vibe.staticdata.entity;

import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapper;

public class Route {

  private static long idCounter = 0;

  private Long id;
  private Integer type;

  public Route(int type, String gtfs_id) {
    this.id = ++idCounter;
    RouteMapper.addMapping(gtfs_id, id);

    this.type = type;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
