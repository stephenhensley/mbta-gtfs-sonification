package org.shensley.gtfs;


//import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FormatOutput implements VehicleListener {
	
	
	
	private RealtimeService _realtimeService;
	private StaticHandler _staticHandler;
/**
 * 
 * @param realtimeService
 * @param staticHandler
 * 
 * make maps for:
 * 		routeNameByRouteID,
 * 		tripNameByTripID,
 * 		stopByPosition,
 * 		stopSequenceByShapeId,
 * 	etc.
 * 
 */
	private HashMap<String, String> routeNameByRouteId = new HashMap<String, String>();
	private HashMap<String, String> tripHeadsignByTripId = new HashMap<String, String>();
	private HashMap<String, StopStatic> stopByPosition = new HashMap<String, StopStatic>();
	//private HashMap<List<StopStatic>, String> stopSequenceByShapeId = new HashMap<List<StopStatic>, String>();
	

	
	
	
	@Inject
	public void setDependancies(RealtimeService realtimeService, StaticHandler staticHandler) {
	  _realtimeService = realtimeService;
	  _staticHandler = staticHandler;
	}

	@PostConstruct
	public void start() {
	  _realtimeService.addListener(this);
	  setMaps(_staticHandler);
	}

	@PreDestroy
	public void stop() {
	  _realtimeService.removeListener(this);
	}
	/**
	 * This is where I will implement the handle vehicles called by RealtimeService.
	 * It will combine the incoming realtime data and the parsed static data to
	 *  create a useful JSON object that will be useful to the Max/MSP side of the program.
	 *  
	 *  things to output:
	 *  	VehicleId(or Index of Train), tripLongName, routeName, latitude, longitude, bearing, 
	 *  	previousStop, nextStop, percentOfTrip, 
	 */
	@Override
	public void handleVehicles(List<Vehicle> vehicles){
		System.out.println("handling");
		printVehiclesCooked(vehicles);
		//printVehiclesRaw(vehicles);
	}
	
	@SuppressWarnings("unused")
	private void printVehiclesRaw(List<Vehicle> vehicles){
		StringBuilder b = new StringBuilder();
		b.append("\n");
		b.append("Information: \nIndex,\tVehicle Id,\tRouteId,\tTripId,\tLat,\tLon,\tBearing\n");
		int z = 0;

		for(Vehicle v : vehicles){
			b.append(Integer.toString(z) + '\t' + v.getId() + '\t' + v.getRouteId() + '\t' + v.getTripId() + '\t' + v.getLat() + '\t' + v.getLon() + '\t' + v.getBearing() + '\n');
			z++;
		}
		b.append("=======================================================================\n");
		
		System.out.print(b.toString());
	}
	
	private void printVehiclesCooked(List<Vehicle> vehicles){
		StringBuilder b = new StringBuilder();
		b.append("\nInformation: \n");
		b.append("Index,\tVehicleId\tRouteName\tPosition\n");
		int z = 0;
		for(Vehicle v : vehicles){
			b.append(Integer.toString(z) + '\t' + v.getId() + '\t' + this.routeNameByRouteId.get(v.getRouteId())+ 
					'\t' + this.tripHeadsignByTripId.get(v.getTripId()) + '\t' + v.getTripId() + '\t' + v.getLat() + '\t'+ v.getLon() + '\t' +  v.getBearing() + '\n');
			z++;
		}
		b.append("==================================================================\n");
		System.out.println(b.toString());
		
	}
	
	private void setMaps(StaticHandler handler){
		setRouteNameByRouteId(handler);
		setTripHeadsignByTripId(handler);
		setStopByPosition(handler);
		setStopSequenceByShapeId(handler);
	}
	
	private void setRouteNameByRouteId(StaticHandler handler){
		for(RouteStatic r : handler.getTrainRoutes()){
			this.routeNameByRouteId.put(r.getId(), r.getLongName());
		}
	}
	
	
	//GreenLine tripId is not cooperative. for some reason: VehicleId+'_'+direction(presumedValue);
	private void setTripHeadsignByTripId(StaticHandler handler){
		for(TripStatic t : handler.getTrainTrips()){
			String tripId = t.getTripId().replace("\"", "");
			this.tripHeadsignByTripId.put(tripId, t.getTripHeadsign());
		}
		
	}
	
	private void setStopByPosition(StaticHandler handler){

		StringBuilder b = new StringBuilder();
		for(StopStatic s : handler.getTrainStops()){
			b.setLength(0);
			b.append(s.getLat() + ',' + s.getLon());
			this.stopByPosition.put(b.toString(), s);
		}
		
	}
	
	
	
	private void setStopSequenceByShapeId(StaticHandler handler){
		/*
		for(ShapeStatic sh : handler.getTrainShapes()){
			for(StopStatic st : handler.getTrainStops()){
				
			}
		}
		*/
	}
}

/**
 * Movie called "Frank" worth watching for the creative process, etc.
 * Daniel Suarez Books (Daemon)
 * Watch Corraline
 */
