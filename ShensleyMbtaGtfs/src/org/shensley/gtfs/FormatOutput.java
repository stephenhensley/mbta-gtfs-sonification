package org.shensley.gtfs;


import java.util.ArrayList;
import java.util.List;

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

	
	
	
	@Inject
	public void setDependancies(RealtimeService realtimeService, StaticHandler staticHandler) {
	  _realtimeService = realtimeService;
	  _staticHandler = staticHandler;
	}

	@PostConstruct
	public void start() {
	  _realtimeService.addListener(this);
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
	}
	
	
	
	

}
