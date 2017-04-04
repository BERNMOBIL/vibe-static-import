package ch.bernmobil.vibe.staticdata.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStopTime;
import org.apache.log4j.Logger;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class StopTimeFieldSetMapper implements FieldSetMapper<GtfsStopTime> {
    private Logger logger = Logger.getLogger(StopTimeFieldSetMapper.class);

    @Override
    public GtfsStopTime mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsStopTime stopTime = new GtfsStopTime();

        stopTime.setTripId(fieldSet.readString("trip_id"));
        stopTime.setArrivalTime(fieldSet.readString("arrival_time"));
        stopTime.setDepartureTime(fieldSet.readString("departure_time"));
        stopTime.setStopId(fieldSet.readString("stop_id"));
        stopTime.setStopSequence(fieldSet.readString("stop_sequence"));
        stopTime.setStopHeadsign(fieldSet.readString("stop_headsign"));
        stopTime.setPickupType(fieldSet.readString("pickup_type"));
        stopTime.setDropOffType(fieldSet.readString("drop_off_type"));
        stopTime.setShapeDistTravelled(fieldSet.readString("shape_dist_traveled"));

        return stopTime;
    }


}
