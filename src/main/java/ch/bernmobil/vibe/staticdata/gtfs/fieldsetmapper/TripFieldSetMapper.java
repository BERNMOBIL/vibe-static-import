package ch.bernmobil.vibe.staticdata.gtfs.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfs.contract.GtfsTripContract;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsTrip;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Provides mapping between a single row in the "trips.txt" and {@link GtfsTrip}
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class TripFieldSetMapper implements FieldSetMapper<GtfsTrip> {

   @Override
    public GtfsTrip mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsTrip trip = new GtfsTrip();
        trip.setServiceId(fieldSet.readString(GtfsTripContract.SERVICE_ID));
        trip.setRouteId(fieldSet.readString(GtfsTripContract.ROUTE_ID));
        trip.setBlockId(fieldSet.readString(GtfsTripContract.BLOCK_ID));
        trip.setDirectionId(fieldSet.readString(GtfsTripContract.DIRECTION_ID));
        trip.setShapeId(fieldSet.readString(GtfsTripContract.SHAPE_ID));
        trip.setTripHeadsign(fieldSet.readString(GtfsTripContract.TRIP_HEADSIGN));
        trip.setTripId(fieldSet.readString(GtfsTripContract.TRIP_ID));
        trip.setTripShortName(fieldSet.readString(GtfsTripContract.TRIP_SHORT_NAME));
        return trip;
    }
}
