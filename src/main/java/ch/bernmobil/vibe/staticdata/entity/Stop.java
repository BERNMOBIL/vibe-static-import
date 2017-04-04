package ch.bernmobil.vibe.staticdata.entity;

import ch.bernmobil.vibe.staticdata.entity.sync.AreaMapper;
import ch.bernmobil.vibe.staticdata.entity.sync.StopMapper;

public class Stop {
    private static long idCounter = 0;
    private Long id;
    private String name;
    private Long area;

    public Stop(String name, String areaId, String gtfs_id) {
        id = ++idCounter;
        StopMapper.addMapping(gtfs_id, idCounter);

        this.name = name;
        this.area = AreaMapper.getMappingByStopId(areaId).getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getArea() {
        return area;
    }

    public void setArea(Long area) {
        this.area = area;
    }
}
