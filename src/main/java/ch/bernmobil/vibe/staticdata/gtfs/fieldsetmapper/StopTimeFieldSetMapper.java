package ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsStopTimeContract;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStopTime;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Provides mapping between a single row in the "stop_times.txt" and {@link GtfsStopTime}
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class StopTimeFieldSetMapper implements FieldSetMapper<GtfsStopTime> {

    @Override
    public GtfsStopTime mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsStopTime stopTime = new GtfsStopTime();

        stopTime.setTripId(fieldSet.readString(GtfsStopTimeContract.TRIP_ID));
        stopTime.setArrivalTime(fieldSet.readString(GtfsStopTimeContract.ARRIVAL_TIME));
        stopTime.setDepartureTime(fieldSet.readString(GtfsStopTimeContract.DEPARTURE_TIME));
        stopTime.setStopId(fieldSet.readString(GtfsStopTimeContract.STOP_ID));
        stopTime.setStopSequence(fieldSet.readString(GtfsStopTimeContract.STOP_SEQUENCE));
        stopTime.setStopHeadsign(fieldSet.readString(GtfsStopTimeContract.STOP_HEADSIGN));
        stopTime.setPickupType(fieldSet.readString(GtfsStopTimeContract.PICKUP_TYPE));
        stopTime.setDropOffType(fieldSet.readString(GtfsStopTimeContract.DROP_OFF_TYPE));
        stopTime.setShapeDistTravelled(fieldSet.readString(GtfsStopTimeContract.SHAPE_DIST_TRAVELED));

        return stopTime;
    }


}
