package ch.bernmobil.vibe.staticdata.testenvironment;

import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStop;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsStopTime;
import ch.bernmobil.vibe.staticdata.gtfs.entity.GtfsTrip;

public class GtfsEntitiyBuilder {
    private GtfsEntitiyBuilder() {}

    public static GtfsStop buildStop(String stopName, String stopId, String parentStation){
        GtfsStop stop = new GtfsStop();
        stop.setStopName(stopName);
        stop.setStopId(stopId);
        stop.setParentStation(parentStation);
        return stop;
    }

    public static GtfsCalendarDate buildCalendarDate(String serviceId, String date) {
        GtfsCalendarDate calendarDate = new GtfsCalendarDate();
        calendarDate.setDate(date);
        calendarDate.setServiceId(serviceId);
        return calendarDate;
    }

    public static GtfsTrip buildTrip(String headsign, String tripId, String routeId, String serviceId) {
        GtfsTrip trip = new GtfsTrip();
        trip.setTripHeadsign(headsign);
        trip.setTripId(tripId);
        trip.setRouteId(routeId);
        trip.setServiceId(serviceId);
        return trip;
    }

    public static GtfsStopTime buildStopTime(String stopId, String tripId, String arrival, String departure) {
        GtfsStopTime st = new GtfsStopTime();
        st.setStopId(stopId);
        st.setTripId(tripId);
        st.setArrivalTime(arrival);
        st.setDepartureTime(departure);
        return st;
    }
}
