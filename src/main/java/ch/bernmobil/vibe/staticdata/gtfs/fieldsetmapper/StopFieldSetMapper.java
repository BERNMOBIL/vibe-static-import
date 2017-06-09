package ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStop;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class StopFieldSetMapper implements FieldSetMapper<GtfsStop> {

    @Override
    public GtfsStop mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsStop stop = new GtfsStop();
        stop.setStopId(fieldSet.readString("stop_id"));
        stop.setStopCode(fieldSet.readString("stop_code"));
        stop.setStopName(fieldSet.readString("stop_name"));
        stop.setStopDesc(fieldSet.readString("stop_desc"));
        stop.setStopLat(fieldSet.readString("stop_lat"));
        stop.setStopLon(fieldSet.readString("stop_lon"));
        stop.setStopUrl(fieldSet.readString("stop_url"));
        stop.setLocationType(fieldSet.readString("location_type"));
        stop.setParentStation(fieldSet.readString("parent_station"));
        return stop;
    }
}
