package ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsStopContract;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStop;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Provides mapping between a single row in the "stops.txt" and {@link GtfsStop}
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class StopFieldSetMapper implements FieldSetMapper<GtfsStop> {

    @Override
    public GtfsStop mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsStop stop = new GtfsStop();
        stop.setStopId(fieldSet.readString(GtfsStopContract.STOP_ID));
        stop.setStopName(fieldSet.readString(GtfsStopContract.STOP_NAME));
        stop.setStopLat(fieldSet.readString(GtfsStopContract.STOP_LAT));
        stop.setStopLon(fieldSet.readString(GtfsStopContract.STOP_LON));
        stop.setLocationType(fieldSet.readString(GtfsStopContract.LOCATION_TYPE));
        stop.setParentStation(fieldSet.readString(GtfsStopContract.PARENT_STATION));
        return stop;
    }
}
