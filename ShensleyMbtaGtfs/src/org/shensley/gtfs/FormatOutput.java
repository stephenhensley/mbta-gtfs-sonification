/**
 * Copyright (C) 2015 Stephen Hensley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.shensley.gtfs;


//import java.util.ArrayList;
import java.util.List;
//import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FormatOutput implements VehicleListener {
	
	//private static String OUTPUT_PATHNAME_FULL = "/Users/stephenhensley/git/MbtaProj/ShensleyMbtaGtfs/tests/testCooked_withStops.json";
	private static String OUTPUT_PATHNAME_CURRENT = "/Users/stephenhensley/git/MbtaProj/ShensleyMbtaGtfs/tests/currentUpdate.json";
	
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
	private HashMap<String, StopStatic> stopByStopName = new HashMap<String, StopStatic>();
	private HashMap<String, StopStatic> stopByPosition = new HashMap<String, StopStatic>();
	private HashMap<String, ArrayList<StopStatic>> stopSequenceByShapeId = new HashMap<String, ArrayList<StopStatic>>();
	private HashMap<String, ArrayList<StopStatic>> stopsByRouteId = new HashMap<String, ArrayList<StopStatic>>();
	

	
	
	
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
	public void handleVehicles(List<Vehicle> vehicles)throws IOException{
		System.out.println("handling");
		setStopsForVehicles(vehicles);
		printVehiclesCooked(vehicles);
		//printVehiclesRaw(vehicles);

		
		try{
			String timestamp = new Date().toString();
			JSONObject obj = formatJsonFile(_staticHandler, vehicles, timestamp);
			outputJsonFile(obj, timestamp);
		}catch(IOException|JSONException ex){
			System.out.println("Could not wrtie JSON file");
			ex.printStackTrace();
		}


		
		

	}
	
	@SuppressWarnings("unused")
	private void printVehiclesRaw(List<Vehicle> vehicles){
		StringBuilder b = new StringBuilder();
		b.append("\n");
		b.append("Information: \nIndex,\tVehicle Id,\tRouteId,\tTripId,\tLat,\tLon,\tBearing\n");
		int z = 0;

		for(Vehicle v : vehicles){
			b.append(Integer.toString(z) + '\t' + v.getId() + '\t' + v.getRouteId() + '\t' + v.getTripId() + 
					'\t' + tripHeadsignByTripId.get(v.getTripId()) + '\t' + v.getLat() + '\t' + v.getLon() + 
					'\t' + v.getBearing()  +'\t'+ v.getPercentToEnd() + '\n');
			z++;
		}
		b.append("=======================================================================\n");
		
		System.out.print(b.toString());
	}
	
	private void printVehiclesCooked(List<Vehicle> vehicles){
		StringBuilder b = new StringBuilder();
		StringBuilder pos = new StringBuilder();
		b.append("\nInformation: \n");
		b.append("Index,\tVehicleId\tRouteName\tPosition\tNextStops and Distances\n");
		int z = 0;
		for(Vehicle v : vehicles){
			pos.setLength(0);
			pos.append(v.getLat() + ',' + v.getLon());
			if(this.stopByPosition.containsKey(pos.toString())){
				b.append("This vehicle is at " + this.stopByPosition.get(pos.toString()).getName() + ":  " );
			}else{
				//This vehicle is currently closest to ___ stop...  now implement.
			}
			b.append(Integer.toString(z) + '\t' + v.getId() + '\t' + this.routeNameByRouteId.get(v.getRouteId())+ 
					'\t' + v.getRouteId() + '\t' + v.getTripId() + '\t' + this.tripHeadsignByTripId.get(v.getTripId()) +
					'\t' + v.getLat() + '\t'+ v.getLon() + '\t' +  v.getBearing() + '\t' + v.getClosestStopName() + '\t' +
					v.getClosestStopDist() + '\t'+ v.getNextStopName() + '\t' + v.getNextStopDist() + '\t' + v.getPercentToEnd() +  '\n');
			z++;
		}
		b.append("==================================================================\n");
		System.out.println(b.toString());
		
	}
	
	//uses stopsByRouteId to correlate vehicle position to vehicles closestStopDist/Name
	private void setStopsForVehicles(List<Vehicle> vehicles){


		for(Vehicle v : vehicles){
			double closestDist = -1;
			String closestName = null;
			int indexOfStop = -1;
			double lat = v.getLat();
			double lon = v.getLon();
			String id = v.getRouteId();
			//System.out.println(id +'\n');
			//System.out.println(id);
			ArrayList<StopStatic> listOfStops = stopsByRouteId.get(id);
			//for(StopStatic s : listOfStops){
			for(int i = 0; i < listOfStops.size(); i++){
				//System.out.println(i);
				double sLat = listOfStops.get(i).getLat();
				double sLon = listOfStops.get(i).getLon();
				double dist = calcDistance(lat, lon, sLat, sLon);
				if(closestDist == -1 || dist < closestDist){
					closestDist = dist;
					closestName = listOfStops.get(i).getName();
					//indexOfStop = listOfStops.indexOf(s);
					indexOfStop = i;
				}
			}
			v.setClosestStopDist(closestDist);
			v.setClosestStopName(closestName);
			if(tripHeadsignByTripId.get(v.getTripId()) == null && v.getTripId().contains(v.getId() + "_1")){
				v.setPercentToEnd(100.0 - (((double)indexOfStop / (double)listOfStops.size()) * 100.0));
				if(indexOfStop - 1 > 0 && indexOfStop -1 < listOfStops.size()){
					v.setNextStopName(listOfStops.get(indexOfStop-1).getName());
					v.setNextStopDist(calcDistance(lat, lon, listOfStops.get(indexOfStop-1).getLat(), listOfStops.get(indexOfStop-1).getLon()));
				}else{
					v.setNextStopName(listOfStops.get(indexOfStop+1).getName());
					v.setNextStopDist(calcDistance(lat, lon, listOfStops.get(indexOfStop+1).getLat(), listOfStops.get(indexOfStop+1).getLon()));
					//System.out.println("This train is near its final stop.");
				}
			}else if(tripHeadsignByTripId.get(v.getTripId()) == null && v.getTripId().contains(v.getId()+ "_0")){
				v.setPercentToEnd(100.0 - (((double)indexOfStop / (double)listOfStops.size()) * 100.0));
				if(indexOfStop + 1 > 0 && indexOfStop + 1 < listOfStops.size()){
					v.setNextStopName(listOfStops.get(indexOfStop+1).getName());
					v.setNextStopDist(calcDistance(lat, lon, listOfStops.get(indexOfStop+1).getLat(), listOfStops.get(indexOfStop+1).getLon()));
				}else{
					v.setNextStopName(listOfStops.get(indexOfStop-1).getName());
					v.setNextStopDist(calcDistance(lat, lon, listOfStops.get(indexOfStop-1).getLat(), listOfStops.get(indexOfStop-1).getLon()));
					//System.out.println("This train is near its final stop.");
				}
			}else{
				String headsign = tripHeadsignByTripId.get(v.getTripId().replace("\"",""));
				//System.out.println(headsign + '\t'+ v.getTripId());
				if(headsign != null){
					if(headsign.contains("Alewife") || headsign.contains("Oak Grove")|| headsign.contains("Wonderland")){
						v.setPercentToEnd(100.0 - (((double)indexOfStop / (double)listOfStops.size()) * 100.0));
						if(indexOfStop - 1 > 0 && indexOfStop -1 < listOfStops.size()){
							v.setNextStopName(listOfStops.get(indexOfStop-1).getName());
							v.setNextStopDist(calcDistance(lat, lon, listOfStops.get(indexOfStop-1).getLat(), listOfStops.get(indexOfStop-1).getLon()));
						}else{
							v.setNextStopName(listOfStops.get(indexOfStop+1).getName());
							v.setNextStopDist(calcDistance(lat, lon, listOfStops.get(indexOfStop+1).getLat(), listOfStops.get(indexOfStop+1).getLon()));
							//System.out.println("This train is near its final stop.");
						}
					}else{
						v.setPercentToEnd(((double)indexOfStop / (double)listOfStops.size()) * 100.0);
						if(indexOfStop + 1 > 0 && indexOfStop + 1 < listOfStops.size()){
							v.setNextStopName(listOfStops.get(indexOfStop+1).getName());
							v.setNextStopDist(calcDistance(lat, lon, listOfStops.get(indexOfStop+1).getLat(), listOfStops.get(indexOfStop+1).getLon()));
						}else{
							v.setNextStopName(listOfStops.get(indexOfStop-1).getName());
							v.setNextStopDist(calcDistance(lat, lon, listOfStops.get(indexOfStop-1).getLat(), listOfStops.get(indexOfStop-1).getLon()));
							//System.out.println("This train is near its final stop.");
						}
					}			
				}
			}			
		}
		
	}
	private double calcDistance(double vlat, double vlon,double slat,double slon){
		double R = 6371000.0;  // Earth's radius in metres.
		double dLat = (slat - vlat) * Math.PI / 180;
		double dLon = (slon - vlon) * Math.PI / 180;
		double a = 0.5 - Math.cos(dLat)/2 + Math.cos(vlat * Math.PI / 180) * Math.cos(slat * Math.PI / 180) * (1 - Math.cos(dLon))/2;
			
		return R * 2 * Math.asin(Math.sqrt(a));
	}
	
	//THESE NEED FIXING. CAN CONFIRM STOPSEQUENCEBYSHAPEID IS BROKEN
	private void setMaps(StaticHandler handler){
		setRouteNameByRouteId(handler);
		setStopByStopName(handler);
		setTripHeadsignByTripId(handler);
		setStopByPosition(handler);
		setStopSequenceByShapeId(handler);
		setStopsByRouteIdOld(handler);
		//setStopsByRouteIdNew(handler);
		
	}
	
	private void setRouteNameByRouteId(StaticHandler handler){
		for(RouteStatic r : handler.getTrainRoutes()){
			this.routeNameByRouteId.put(r.getId(), r.getLongName());
		}
	}
	
	private void setStopByStopName(StaticHandler handler){
		for(StopStatic s : handler.getTrainStops()){
			this.stopByStopName.put(s.getName().replace("\"",""), s);
		}
	}
	
	
	//GreenLine tripId is not cooperative. for some reason: VehicleId+'_'+direction(presumedValue);
	private void setTripHeadsignByTripId(StaticHandler handler){
		for(TripStatic t : handler.getTrainTrips()){
			String tripId = t.getTripId().replace("\"", "");
			this.tripHeadsignByTripId.put(tripId, t.getTripHeadsign());
			//System.out.println(tripId + '\t' + t.getTripHeadsign());
		}
		
		
	}
	
	
	//Position is a comma seperated value that can be generated relatively easy: Lat,Lon
	//doesn't implement correctly because of the accuracy of train pos vs stop pos
	private void setStopByPosition(StaticHandler handler){

		StringBuilder b = new StringBuilder();
		for(StopStatic s : handler.getTrainStops()){
			b.setLength(0);
			b.append(s.getLat() + ',' + s.getLon());
			this.stopByPosition.put(b.toString(), s);
		}
		
	}
	
	
	//shits broke.
	private void setStopSequenceByShapeId(StaticHandler handler){

		for(ShapeStatic sh : handler.getTrainShapes()){
			for(int i = 0;i<sh.getLength();i++){
				for(StopStatic st : handler.getTrainStops()){
					if(st.getLat() == sh.getLat(i) && st.getLon() == sh.getLon(i)){
						if(!this.stopSequenceByShapeId.containsKey(sh.getId())){
							ArrayList<StopStatic> newStopList = new ArrayList<StopStatic>();
							newStopList.add(st);
							this.stopSequenceByShapeId.put(sh.getId(), newStopList);
							
						}else{
							ArrayList<StopStatic> oldStopList = this.stopSequenceByShapeId.get(sh.getId());
							this.stopSequenceByShapeId.remove(sh.getId());
							oldStopList.add(st);
							this.stopSequenceByShapeId.put(sh.getId(), oldStopList);
						}
					}
				}
			}
		}
	}
	//while most of the program does things relatively intelligently I don't have the time currently to make this adaptable to changes.
	//Therefore, I will hard code this to be accurate. May come back to fix later.
	//Green Line, Blue Line, Red Line, Orange Line stops
	//I may need to handle green line and redline differently as they each have multiple paths.
	private void setStopsByRouteIdOld(StaticHandler handler){
		for(RouteStatic r : handler.getTrainRoutes()){
			String id = r.getId();
			ArrayList<StopStatic> stopSequence = new ArrayList<StopStatic>();
			//greenline
			
			if(id.contains("810_") || id.contains("813_") || id.contains("823_") || id.contains("830_") || 
					id.contains("831_") || id.contains("840_") || id.contains("842_") || id.contains("851_") || 
					id.contains("852_") || id.contains("880_") || id.contains("882_")){
			
			//if(id.contains("Green")){
				//System.out.println("Adding Green Line.");
				//if(id.contains("810_") || id.contains("830_") || id.contains("840_") || id.contains("880_")){
					stopSequence.add(stopByStopName.get("Lechmere"));
					stopSequence.add(stopByStopName.get("Science Park"));
				//}
				//if(id.contains("831_") || id.contains("851_")){
					stopSequence.add(stopByStopName.get("North Station"));
				//}
				//if(!stopSequence.isEmpty()){
					stopSequence.add(stopByStopName.get("Haymarket"));
					stopSequence.add(stopByStopName.get("Government Center"));
			//	}
				stopSequence.add(stopByStopName.get("Park Street"));
				stopSequence.add(stopByStopName.get("Boylston"));
				stopSequence.add(stopByStopName.get("Arlington"));
				stopSequence.add(stopByStopName.get("Copley"));
				if(id.contains("880_") || id.contains("882_")){
				//if(id.contains("Green-E")){
					//System.out.println("E LINE");
					stopSequence.add(stopByStopName.get("Prudential"));
					stopSequence.add(stopByStopName.get("Symphony"));
					stopSequence.add(stopByStopName.get("Northeastern University"));
					stopSequence.add(stopByStopName.get("Museum of Fine Arts"));
					stopSequence.add(stopByStopName.get("Longwood Medical Area"));
					stopSequence.add(stopByStopName.get("Brigham Circle"));
					stopSequence.add(stopByStopName.get("Fenwood Road"));
					stopSequence.add(stopByStopName.get("Mission Park"));
					stopSequence.add(stopByStopName.get("Riverway"));
					stopSequence.add(stopByStopName.get("Back of the Hill"));
					stopSequence.add(stopByStopName.get("Heath Street"));
					stopsByRouteId.put(id, stopSequence);
					
					
				}else{
					stopSequence.add(stopByStopName.get("Hynes Convention Center"));
					stopSequence.add(stopByStopName.get("Kenmore"));
					if(id.contains("810_") || id.contains("813_") || id.contains("823_")){
					//if(id.contains("Green-B")){
						//System.out.println("B LINE");
						stopSequence.add(stopByStopName.get("Blandford Street"));
						stopSequence.add(stopByStopName.get("Boston Univ. East"));
						stopSequence.add(stopByStopName.get("Boston Univ. Central"));
						stopSequence.add(stopByStopName.get("Boston Univ. West"));
						stopSequence.add(stopByStopName.get("Saint Paul Street"));
						stopSequence.add(stopByStopName.get("Pleasant Street"));
						stopSequence.add(stopByStopName.get("Babcock Street"));
						stopSequence.add(stopByStopName.get("Packards Corner"));
						stopSequence.add(stopByStopName.get("Harvard Ave."));
						stopSequence.add(stopByStopName.get("Griggs Street"));
						stopSequence.add(stopByStopName.get("Allston Street"));
						stopSequence.add(stopByStopName.get("Warren Street"));
						stopSequence.add(stopByStopName.get("Washington Street"));
						stopSequence.add(stopByStopName.get("Sutherland Road"));
						stopSequence.add(stopByStopName.get("Chiswick Road"));
						stopSequence.add(stopByStopName.get("Chestnut Hill Ave."));
						stopSequence.add(stopByStopName.get("South Street"));
						stopSequence.add(stopByStopName.get("Boston College"));
						
						stopsByRouteId.put(id, stopSequence);
					}else if(id.contains("830_") || id.contains("831_")){
					//}else if(id.contains("Green-C")){	
						//System.out.println("C LINE");
						stopSequence.add(stopByStopName.get("Saint Mary Street"));
						stopSequence.add(stopByStopName.get("Hawes Street"));
						stopSequence.add(stopByStopName.get("Kent Street"));
						stopSequence.add(stopByStopName.get("Saint Paul Street"));
						stopSequence.add(stopByStopName.get("Coolidge Corner"));
						stopSequence.add(stopByStopName.get("Summit Ave."));
						stopSequence.add(stopByStopName.get("Brandon Hall"));
						stopSequence.add(stopByStopName.get("Fairbanks Street"));
						stopSequence.add(stopByStopName.get("Washington Street"));
						stopSequence.add(stopByStopName.get("Tappan Street"));
						stopSequence.add(stopByStopName.get("Dean Road"));
						stopSequence.add(stopByStopName.get("Englewood Ave."));
						stopSequence.add(stopByStopName.get("Cleveland Circle"));
						stopsByRouteId.put(id, stopSequence);
						
		
					}else if(id.contains("840_") || id.contains("842_") || id.contains("851_") || id.contains("852_")){
					//}else if(id.contains("Green-D")){	
						//System.out.println("D LINE");
						stopSequence.add(stopByStopName.get("Fenway"));
						stopSequence.add(stopByStopName.get("Longwood"));
						stopSequence.add(stopByStopName.get("Brookline Village"));
						stopSequence.add(stopByStopName.get("Brookline Hills"));
						stopSequence.add(stopByStopName.get("Beaconsfield"));
						stopSequence.add(stopByStopName.get("Reservoir"));
						stopSequence.add(stopByStopName.get("Chestnut Hill"));
						stopSequence.add(stopByStopName.get("Newton Centre"));
						stopSequence.add(stopByStopName.get("Newton Highlands"));
						stopSequence.add(stopByStopName.get("Eliot"));
						stopSequence.add(stopByStopName.get("Woodland"));
						stopSequence.add(stopByStopName.get("Riverside"));
						stopsByRouteId.put(id, stopSequence);
					}
				}
				//System.out.print("done with green lines");
			}else if(id.contains("903_") || id.contains("913_")){
			//}else if(id.contains("Orange")){
				//System.out.println("Adding Orange Line");
				stopSequence.add(stopByStopName.get("Oak Grove"));
				stopSequence.add(stopByStopName.get("Malden Center"));
				stopSequence.add(stopByStopName.get("Wellington"));
				stopSequence.add(stopByStopName.get("Sullivan Square"));
				stopSequence.add(stopByStopName.get("Community College"));
				stopSequence.add(stopByStopName.get("North Station"));
				stopSequence.add(stopByStopName.get("Haymarket"));
				stopSequence.add(stopByStopName.get("State Street"));
				stopSequence.add(stopByStopName.get("Downtown Crossing"));
				stopSequence.add(stopByStopName.get("Chinatown"));
				stopSequence.add(stopByStopName.get("Tufts Medical Center"));
				stopSequence.add(stopByStopName.get("Back Bay"));
				stopSequence.add(stopByStopName.get("Massachusetts Ave."));
				stopSequence.add(stopByStopName.get("Ruggles"));
				stopSequence.add(stopByStopName.get("Roxbury Crossing"));
				stopSequence.add(stopByStopName.get("Jackson Square"));
				stopSequence.add(stopByStopName.get("Stony Brook"));
				stopSequence.add(stopByStopName.get("Green Street"));
				stopSequence.add(stopByStopName.get("Forest Hills"));
				stopsByRouteId.put(id, stopSequence);

			//Red
			}else if(id.contains("931_") || id.contains("933_")){
			//}else if(id.contains("Red")){
				//System.out.println("Adding Red Line.");
				stopSequence.add(stopByStopName.get("Alewife"));
				stopSequence.add(stopByStopName.get("Davis"));
				stopSequence.add(stopByStopName.get("Porter"));
				stopSequence.add(stopByStopName.get("Harvard"));
				stopSequence.add(stopByStopName.get("Central"));
				stopSequence.add(stopByStopName.get("Kendall/MIT"));
				stopSequence.add(stopByStopName.get("Charles/MGH"));
				stopSequence.add(stopByStopName.get("Park Street"));
				stopSequence.add(stopByStopName.get("Downtown Crossing"));
				stopSequence.add(stopByStopName.get("South Station"));
				stopSequence.add(stopByStopName.get("Broadway"));
				stopSequence.add(stopByStopName.get("Andrew"));
				stopSequence.add(stopByStopName.get("JFK/Umass"));
				//if(id.contains("931_")){
					//System.out.println("Ashmont");
					stopSequence.add(stopByStopName.get("Savin Hill"));
					stopSequence.add(stopByStopName.get("Fields Corner"));
					stopSequence.add(stopByStopName.get("Shawmut"));
					stopSequence.add(stopByStopName.get("Ashmont"));
				//}else if(id.contains("933_")){
					//System.out.println("Braintree");
					stopSequence.add(stopByStopName.get("North Quincy"));
					stopSequence.add(stopByStopName.get("Wollaston"));
					stopSequence.add(stopByStopName.get("Quincy Center"));
					stopSequence.add(stopByStopName.get("Quincy Adams"));
					stopSequence.add(stopByStopName.get("Braintree"));
				//}
				stopsByRouteId.put(id, stopSequence);
				
			//Blue
			}else if(id.contains("946_") || id.contains("948_")){
			//}else if(id.contains("Blue")){
				//System.out.println("Adding Blue Line");
				stopSequence.add(stopByStopName.get("Wonderland"));
				stopSequence.add(stopByStopName.get("Revere Beach"));
				stopSequence.add(stopByStopName.get("Beachmont"));
				stopSequence.add(stopByStopName.get("Suffolk Downs"));
				stopSequence.add(stopByStopName.get("Orient Heights"));
				stopSequence.add(stopByStopName.get("Wood Island"));
				stopSequence.add(stopByStopName.get("Airport"));
				stopSequence.add(stopByStopName.get("Maverick"));
				stopSequence.add(stopByStopName.get("Aquarium"));
				stopSequence.add(stopByStopName.get("State Street"));
				stopSequence.add(stopByStopName.get("Government Center"));
				stopSequence.add(stopByStopName.get("Bowdoin"));
				stopsByRouteId.put(id, stopSequence);
				
			
			}else if(id.contains("899")){
			//}else if(id.contains("Mattapan")){
				//System.out.println("Mattapan Trolley");
				stopSequence.add(stopByStopName.get("Ashmont"));
				stopSequence.add(stopByStopName.get("Cedar Grove"));
				stopSequence.add(stopByStopName.get("Butler"));
				stopSequence.add(stopByStopName.get("Milton"));
				stopSequence.add(stopByStopName.get("Central Ave."));
				stopSequence.add(stopByStopName.get("Valley Road"));
				stopSequence.add(stopByStopName.get("Capen Street"));
				stopSequence.add(stopByStopName.get("Mattapan"));
				stopsByRouteId.put(id, stopSequence);	
				
			}else{
				System.out.println("RouteId does not match " + id);
			}		
		}		
	}
	
	@SuppressWarnings("unused")
	private void setStopsByRouteIdNew(StaticHandler handler){
		for(RouteStatic r : handler.getTrainRoutes()){
			String id = r.getId();
			ArrayList<StopStatic> stopSequence = new ArrayList<StopStatic>();
			//greenline
			/*
			if(id.contains("810_") || id.contains("813_") || id.contains("823_") || id.contains("830_") || 
					id.contains("831_") || id.contains("840_") || id.contains("842_") || id.contains("851_") || 
					id.contains("852_") || id.contains("880_") || id.contains("882_")){
			*/
			if(id.contains("Green")){
				//System.out.println("Adding Green Line.");
				//if(id.contains("810_") || id.contains("830_") || id.contains("840_") || id.contains("880_")){
					stopSequence.add(stopByStopName.get("Lechmere"));
					stopSequence.add(stopByStopName.get("Science Park"));
				//}
				//if(id.contains("831_") || id.contains("851_")){
					stopSequence.add(stopByStopName.get("North Station"));
				//}
				//if(!stopSequence.isEmpty()){
					stopSequence.add(stopByStopName.get("Haymarket"));
					stopSequence.add(stopByStopName.get("Government Center"));
			//	}
				stopSequence.add(stopByStopName.get("Park Street"));
				stopSequence.add(stopByStopName.get("Boylston"));
				stopSequence.add(stopByStopName.get("Arlington"));
				stopSequence.add(stopByStopName.get("Copley"));
				//if(id.contains("880_") || id.contains("882_")){
				if(id.contains("Green-E")){
					//System.out.println("E LINE");
					stopSequence.add(stopByStopName.get("Prudential"));
					stopSequence.add(stopByStopName.get("Symphony"));
					stopSequence.add(stopByStopName.get("Northeastern University"));
					stopSequence.add(stopByStopName.get("Museum of Fine Arts"));
					stopSequence.add(stopByStopName.get("Longwood Medical Area"));
					stopSequence.add(stopByStopName.get("Brigham Circle"));
					stopSequence.add(stopByStopName.get("Fenwood Road"));
					stopSequence.add(stopByStopName.get("Mission Park"));
					stopSequence.add(stopByStopName.get("Riverway"));
					stopSequence.add(stopByStopName.get("Back of the Hill"));
					stopSequence.add(stopByStopName.get("Heath Street"));
					stopsByRouteId.put(id, stopSequence);
					
					
				}else{
					stopSequence.add(stopByStopName.get("Hynes Convention Center"));
					stopSequence.add(stopByStopName.get("Kenmore"));
					//if(id.contains("810_") || id.contains("813_") || id.contains("823_")){
					if(id.contains("Green-B")){
						//System.out.println("B LINE");
						stopSequence.add(stopByStopName.get("Blandford Street"));
						stopSequence.add(stopByStopName.get("Boston Univ. East"));
						stopSequence.add(stopByStopName.get("Boston Univ. Central"));
						stopSequence.add(stopByStopName.get("Boston Univ. West"));
						stopSequence.add(stopByStopName.get("Saint Paul Street"));
						stopSequence.add(stopByStopName.get("Pleasant Street"));
						stopSequence.add(stopByStopName.get("Babcock Street"));
						stopSequence.add(stopByStopName.get("Packards Corner"));
						stopSequence.add(stopByStopName.get("Harvard Ave."));
						stopSequence.add(stopByStopName.get("Griggs Street"));
						stopSequence.add(stopByStopName.get("Allston Street"));
						stopSequence.add(stopByStopName.get("Warren Street"));
						stopSequence.add(stopByStopName.get("Washington Street"));
						stopSequence.add(stopByStopName.get("Sutherland Road"));
						stopSequence.add(stopByStopName.get("Chiswick Road"));
						stopSequence.add(stopByStopName.get("Chestnut Hill Ave."));
						stopSequence.add(stopByStopName.get("South Street"));
						stopSequence.add(stopByStopName.get("Boston College"));
						
						stopsByRouteId.put(id, stopSequence);
					//}else if(id.contains("830_") || id.contains("831_")){
					}else if(id.contains("Green-C")){	
						//System.out.println("C LINE");
						stopSequence.add(stopByStopName.get("Saint Mary Street"));
						stopSequence.add(stopByStopName.get("Hawes Street"));
						stopSequence.add(stopByStopName.get("Kent Street"));
						stopSequence.add(stopByStopName.get("Saint Paul Street"));
						stopSequence.add(stopByStopName.get("Coolidge Corner"));
						stopSequence.add(stopByStopName.get("Summit Ave."));
						stopSequence.add(stopByStopName.get("Brandon Hall"));
						stopSequence.add(stopByStopName.get("Fairbanks Street"));
						stopSequence.add(stopByStopName.get("Washington Street"));
						stopSequence.add(stopByStopName.get("Tappan Street"));
						stopSequence.add(stopByStopName.get("Dean Road"));
						stopSequence.add(stopByStopName.get("Englewood Ave."));
						stopSequence.add(stopByStopName.get("Cleveland Circle"));
						stopsByRouteId.put(id, stopSequence);
						
		
					//}else if(id.contains("840_") || id.contains("842_") || id.contains("851_") || id.contains("852_")){
					}else if(id.contains("Green-D")){	
						//System.out.println("D LINE");
						stopSequence.add(stopByStopName.get("Fenway"));
						stopSequence.add(stopByStopName.get("Longwood"));
						stopSequence.add(stopByStopName.get("Brookline Village"));
						stopSequence.add(stopByStopName.get("Brookline Hills"));
						stopSequence.add(stopByStopName.get("Beaconsfield"));
						stopSequence.add(stopByStopName.get("Reservoir"));
						stopSequence.add(stopByStopName.get("Chestnut Hill"));
						stopSequence.add(stopByStopName.get("Newton Centre"));
						stopSequence.add(stopByStopName.get("Newton Highlands"));
						stopSequence.add(stopByStopName.get("Eliot"));
						stopSequence.add(stopByStopName.get("Woodland"));
						stopSequence.add(stopByStopName.get("Riverside"));
						stopsByRouteId.put(id, stopSequence);
					}
				}
				//System.out.print("done with green lines");
			//}else if(id.contains("903_") || id.contains("913_")){
			}else if(id.contains("Orange")){
				//System.out.println("Adding Orange Line");
				stopSequence.add(stopByStopName.get("Oak Grove"));
				stopSequence.add(stopByStopName.get("Malden Center"));
				stopSequence.add(stopByStopName.get("Wellington"));
				stopSequence.add(stopByStopName.get("Sullivan Square"));
				stopSequence.add(stopByStopName.get("Community College"));
				stopSequence.add(stopByStopName.get("North Station"));
				stopSequence.add(stopByStopName.get("Haymarket"));
				stopSequence.add(stopByStopName.get("State Street"));
				stopSequence.add(stopByStopName.get("Downtown Crossing"));
				stopSequence.add(stopByStopName.get("Chinatown"));
				stopSequence.add(stopByStopName.get("Tufts Medical Center"));
				stopSequence.add(stopByStopName.get("Back Bay"));
				stopSequence.add(stopByStopName.get("Massachusetts Ave."));
				stopSequence.add(stopByStopName.get("Ruggles"));
				stopSequence.add(stopByStopName.get("Roxbury Crossing"));
				stopSequence.add(stopByStopName.get("Jackson Square"));
				stopSequence.add(stopByStopName.get("Stony Brook"));
				stopSequence.add(stopByStopName.get("Green Street"));
				stopSequence.add(stopByStopName.get("Forest Hills"));
				stopsByRouteId.put(id, stopSequence);

			//Red
			//}else if(id.contains("931_") || id.contains("933_")){
			}else if(id.contains("Red")){
				//System.out.println("Adding Red Line.");
				stopSequence.add(stopByStopName.get("Alewife"));
				stopSequence.add(stopByStopName.get("Davis"));
				stopSequence.add(stopByStopName.get("Porter"));
				stopSequence.add(stopByStopName.get("Harvard"));
				stopSequence.add(stopByStopName.get("Central"));
				stopSequence.add(stopByStopName.get("Kendall/MIT"));
				stopSequence.add(stopByStopName.get("Charles/MGH"));
				stopSequence.add(stopByStopName.get("Park Street"));
				stopSequence.add(stopByStopName.get("Downtown Crossing"));
				stopSequence.add(stopByStopName.get("South Station"));
				stopSequence.add(stopByStopName.get("Broadway"));
				stopSequence.add(stopByStopName.get("Andrew"));
				stopSequence.add(stopByStopName.get("JFK/Umass"));
				//if(id.contains("931_")){
					//System.out.println("Ashmont");
					stopSequence.add(stopByStopName.get("Savin Hill"));
					stopSequence.add(stopByStopName.get("Fields Corner"));
					stopSequence.add(stopByStopName.get("Shawmut"));
					stopSequence.add(stopByStopName.get("Ashmont"));
				//}else if(id.contains("933_")){
					//System.out.println("Braintree");
					stopSequence.add(stopByStopName.get("North Quincy"));
					stopSequence.add(stopByStopName.get("Wollaston"));
					stopSequence.add(stopByStopName.get("Quincy Center"));
					stopSequence.add(stopByStopName.get("Quincy Adams"));
					stopSequence.add(stopByStopName.get("Braintree"));
				//}
				stopsByRouteId.put(id, stopSequence);
				
			//Blue
			//}else if(id.contains("946_") || id.contains("948_")){
			}else if(id.contains("Blue")){
				//System.out.println("Adding Blue Line");
				stopSequence.add(stopByStopName.get("Wonderland"));
				stopSequence.add(stopByStopName.get("Revere Beach"));
				stopSequence.add(stopByStopName.get("Beachmont"));
				stopSequence.add(stopByStopName.get("Suffolk Downs"));
				stopSequence.add(stopByStopName.get("Orient Heights"));
				stopSequence.add(stopByStopName.get("Wood Island"));
				stopSequence.add(stopByStopName.get("Airport"));
				stopSequence.add(stopByStopName.get("Maverick"));
				stopSequence.add(stopByStopName.get("Aquarium"));
				stopSequence.add(stopByStopName.get("State Street"));
				stopSequence.add(stopByStopName.get("Government Center"));
				stopSequence.add(stopByStopName.get("Bowdoin"));
				stopsByRouteId.put(id, stopSequence);
				
			
			//}else if(id.contains("899")){
			}else if(id.contains("Mattapan")){
				//System.out.println("Mattapan Trolley");
				stopSequence.add(stopByStopName.get("Ashmont"));
				stopSequence.add(stopByStopName.get("Cedar Grove"));
				stopSequence.add(stopByStopName.get("Butler"));
				stopSequence.add(stopByStopName.get("Milton"));
				stopSequence.add(stopByStopName.get("Central Ave."));
				stopSequence.add(stopByStopName.get("Valley Road"));
				stopSequence.add(stopByStopName.get("Capen Street"));
				stopSequence.add(stopByStopName.get("Mattapan"));
				stopsByRouteId.put(id, stopSequence);	
				
			}else{
				System.out.println("RouteId does not match " + id);
			}		
		}		
	}

	
	
	/*
	 * info to pack and order
	 * for each vehicle create a json object containing Id, lat, lon, bearing, routeId, tripName{else if greenline{tripId
	 * for each stop create a json object with stopId, name , lat, lon
	 * make an array of all vehicles by index
	 * make an array of all stops by index
	 * make an object of vehiclesArray and stopsArray
	 */
	private JSONObject formatJsonFile(StaticHandler handler, List<Vehicle> vehicles, String timestamp){
		try{
			JSONObject myFinalObj = new JSONObject();
			JSONObject vehiclesArray = new JSONObject();
			JSONObject stopsArray = new JSONObject();

			myFinalObj.put("timestampOfUpdate", timestamp);
			
			//load vehicles
			for(int i = 0; i < vehicles.size(); i++){
				JSONObject tempVehicleObj = new JSONObject();
				tempVehicleObj.put("vehicleId", vehicles.get(i).getId());
				tempVehicleObj.put("lat", Double.toString(vehicles.get(i).getLat()));
				tempVehicleObj.put("lon", Double.toString(vehicles.get(i).getLon()));
				tempVehicleObj.put("bearing", Double.toString(vehicles.get(i).getBearing()));
				tempVehicleObj.put("routeId", vehicles.get(i).getRouteId());
				tempVehicleObj.put("routeName", this.routeNameByRouteId.get(vehicles.get(i).getRouteId()));
				if(this.tripHeadsignByTripId.get(vehicles.get(i).getTripId()) != null){
					tempVehicleObj.put("trip", this.tripHeadsignByTripId.get(vehicles.get(i).getTripId()));
				}else{
					tempVehicleObj.put("trip", vehicles.get(i).getTripId());
				}
				tempVehicleObj.put("closestStopName", vehicles.get(i).getClosestStopName());
				tempVehicleObj.put("closestStopDist", vehicles.get(i).getClosestStopDist());
				tempVehicleObj.put("nextStopName", vehicles.get(i).getNextStopName());
				tempVehicleObj.put("nextStopDist", vehicles.get(i).getNextStopDist());
				tempVehicleObj.put("percentToEnd", vehicles.get(i).getPercentToEnd());
				vehiclesArray.put(Integer.toString(i),tempVehicleObj);
			}
			JSONObject vehiclesArrayByTimeStamp = new JSONObject().put(timestamp,vehiclesArray);
			//load stops and size of stops
			int index = 0;
			for(StopStatic stop : handler.getTrainStops()){
				JSONObject tempStopObj = new JSONObject();
				tempStopObj.put("stopId", stop.getId());
				tempStopObj.put("name", stop.getName());
				tempStopObj.put("lat", Double.toString(stop.getLat()));
				tempStopObj.put("lon", Double.toString(stop.getLon()));
				stopsArray.put(Integer.toString(index), tempStopObj);
				index++;
			}

			
			
			myFinalObj.put("vehicles", vehiclesArrayByTimeStamp);
			myFinalObj.put("stops", stopsArray);
			
			return myFinalObj;
					
		}catch (JSONException ex){
			throw new IllegalStateException(ex);
		}		
	}
	
	private void outputJsonFile(JSONObject obj, String timestamp)throws IOException, JSONException{
		//String pathname = "/Users/stephenhensley/Desktop/test.json";
		/*
		if(!new File(OUTPUT_PATHNAME_FULL).isFile()){
			FileWriter file = new FileWriter(OUTPUT_PATHNAME_FULL);
			try{
				file.write(obj.toString(2));
				System.out.println("Successfully wrote " + OUTPUT_PATHNAME_FULL);
			}catch(IOException ex){
				ex.printStackTrace();
			}finally{
				file.flush();
				file.close();
			}
		}else{
		
			String previousFileAsString = readExistingFile(OUTPUT_PATHNAME_FULL);
			JSONObject previous = new JSONObject(previousFileAsString);

			JSONObject newVehiclesArrayByTimestamp = obj.getJSONObject("vehicles").getJSONObject(timestamp);
			JSONObject oldVehicles = previous.getJSONObject("vehicles");
			oldVehicles.put(timestamp, newVehiclesArrayByTimestamp);
			previous.put("vehicles", oldVehicles);
			//myFinalObj.put("timestampOfUpdate", timestamp);
			previous.remove("timestampOfUpdate");
			previous.put("timestampOfUpdate", timestamp);
			
			FileWriter file = new FileWriter(OUTPUT_PATHNAME_FULL);
			try{
				//If I want the file to be printed in a human-readable way
				// then add the arguement 2 to previous.toString(); 
				//  otherwise remove it for a singleline file
				file.write(previous.toString(2));
				System.out.println("Successfully updated " + OUTPUT_PATHNAME_FULL);
			}catch(IOException ex){
				ex.printStackTrace();
			}finally{
				file.flush();
				file.close();
			}	
		}
		*/
		
		File f = new File(OUTPUT_PATHNAME_CURRENT);
		if(f.exists()){
			f.delete();
		}
		FileWriter file = new FileWriter(OUTPUT_PATHNAME_CURRENT);
		try{
			file.write(obj.toString(2));
			System.out.println("Successfully wrote " + OUTPUT_PATHNAME_CURRENT);
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			file.flush();
			file.close();
		}
	}
	
	@SuppressWarnings("unused")
	private String readExistingFile(String path){
		try{
			FileReader readFile = new FileReader(path);
			BufferedReader br = new BufferedReader(readFile);
			StringBuilder s = new StringBuilder();
			String temp = null;
			while((temp = br.readLine()) != null){
				s.append(temp +'\n');
			}
			br.close();
			readFile.close();
			return s.toString();
		}catch(Exception ex){
			System.out.println("Could not read file from given pathname");
			return null;
		}
	}
}

/**
 * Movie called "Frank" worth watching for the creative process, etc.
 * Daniel Suarez Books (Daemon)
 * Watch Corraline
 */
