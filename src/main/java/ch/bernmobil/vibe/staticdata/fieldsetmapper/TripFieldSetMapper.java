package ch.bernmobil.vibe.staticdata.fieldsetmapper;

import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsTrip;
import org.apache.log4j.Logger;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class TripFieldSetMapper implements FieldSetMapper<GtfsTrip> {

    private static Logger logger = Logger.getLogger(TripFieldSetMapper.class);

    @Override
    public GtfsTrip mapFieldSet(FieldSet fieldSet) throws BindException {
        GtfsTrip trip = new GtfsTrip();

        trip.setServiceId(fieldSet.readString("service_id"));
        trip.setRouteId(fieldSet.readString("route_id"));
        trip.setBlockId(fieldSet.readString("block_id"));
        trip.setDirectionId(fieldSet.readString("direction_id"));
        trip.setShapeId(fieldSet.readString("shape_id"));
        trip.setTripHeadsign(fieldSet.readString("trip_headsign"));
        trip.setTripId(fieldSet.readString("trip_id"));
        trip.setTripShortName(fieldSet.readString("trip_short_name"));

        return trip;
    }
}
