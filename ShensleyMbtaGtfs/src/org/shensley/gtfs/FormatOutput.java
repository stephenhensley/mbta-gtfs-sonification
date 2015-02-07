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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FormatOutput implements VehicleListener {
	
	private static String OUTPUT_PATHNAME = "/Users/stephenhensley/git/MbtaProj/ShensleyMbtaGtfs/tests/test.json"; 
	
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
	private HashMap<String, ArrayList<StopStatic>> stopSequenceByShapeId = new HashMap<String, ArrayList<StopStatic>>();
	

	
	
	
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
			b.append(Integer.toString(z) + '\t' + v.getId() + '\t' + v.getRouteId() + '\t' + v.getTripId() + '\t' + v.getLat() + '\t' + v.getLon() + '\t' + v.getBearing() + '\n');
			z++;
		}
		b.append("=======================================================================\n");
		
		System.out.print(b.toString());
	}
	
	private void printVehiclesCooked(List<Vehicle> vehicles){
		StringBuilder b = new StringBuilder();
		StringBuilder pos = new StringBuilder();
		b.append("\nInformation: \n");
		b.append("Index,\tVehicleId\tRouteName\tPosition\n");
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
	
	
	//Position is a comma seperated value that can be generated relatively easy: Lat,Lon
	private void setStopByPosition(StaticHandler handler){

		StringBuilder b = new StringBuilder();
		for(StopStatic s : handler.getTrainStops()){
			b.setLength(0);
			b.append(s.getLat() + ',' + s.getLon());
			this.stopByPosition.put(b.toString(), s);
		}
		
	}
	
	
	
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
			JSONArray vehiclesArray = new JSONArray();
			JSONArray stopsArray = new JSONArray();

			myFinalObj.put("timestampOfUpdate", timestamp);
			
			//load vehicles
			for(int i = 0; i < vehicles.size(); i++){
				JSONObject tempVehicleObj = new JSONObject();
				tempVehicleObj.put("vehicleId", vehicles.get(i).getId());
				tempVehicleObj.put("lat", Double.toString(vehicles.get(i).getLat()));
				tempVehicleObj.put("lon", Double.toString(vehicles.get(i).getLon()));
				tempVehicleObj.put("bearing", Double.toString(vehicles.get(i).getBearing()));
				tempVehicleObj.put("routeId", vehicles.get(i).getRouteId());
				tempVehicleObj.put("tripId", vehicles.get(i).getTripId());
				vehiclesArray.put(i,tempVehicleObj);
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
				stopsArray.put(index, tempStopObj);
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
		if(!new File(OUTPUT_PATHNAME).isFile()){
			FileWriter file = new FileWriter(OUTPUT_PATHNAME);
			try{
				file.write(obj.toString(2));
				System.out.println("Successfully wrote test.json");
			}catch(IOException ex){
				ex.printStackTrace();
			}finally{
				file.flush();
				file.close();
			}
		}else{
			String previousFileAsString = readExistingFile(OUTPUT_PATHNAME);
			JSONObject previous = new JSONObject(previousFileAsString);

			JSONArray newVehiclesArrayByTimestamp = obj.getJSONObject("vehicles").getJSONArray(timestamp);
			JSONObject oldVehicles = previous.getJSONObject("vehicles");
			oldVehicles.put(timestamp, newVehiclesArrayByTimestamp);
			previous.put("vehicles", oldVehicles);
			FileWriter file = new FileWriter(OUTPUT_PATHNAME);
			try{
				//If I want the file to be printed in a human-readable way
				// then add the arguement 2 to previous.toString(); 
				//  otherwise remove it for a singleline file
				file.write(previous.toString(2));
				System.out.println("Successfully updated test.json");
			}catch(IOException ex){
				ex.printStackTrace();
			}finally{
				file.flush();
				file.close();
			}	
		}
	}
	
	
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
