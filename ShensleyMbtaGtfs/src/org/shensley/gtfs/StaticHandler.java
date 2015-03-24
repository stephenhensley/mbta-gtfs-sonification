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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

@Singleton
public class StaticHandler {

	private static ArrayList<String> lines = new ArrayList<String>();
	private static ArrayList<String> routeIdList = new ArrayList<String>();
	private static ArrayList<RouteStatic> trainRoutes = new ArrayList<RouteStatic>();
	private static ArrayList<TripStatic> trainTrips = new ArrayList<TripStatic>();
	private static ArrayList<StopStatic> trainStops = new ArrayList<StopStatic>();
	private static ArrayList<StopStatic> trainSubStops = new ArrayList<StopStatic>();
	private static ArrayList<ShapeStatic> trainShapes = new ArrayList<ShapeStatic>();
	private static ArrayList<Calendar> trainCalendar = new ArrayList<Calendar>();

	// Constant values that will be adjusted based on the current machine.
	// Can only be run on compiler's machine until this is resolved.
	final static String PATH_NAME = "/Users/stephenhensley/MBTA_programming/MBTA_GTFS/";
	final static String[] FILE_NAME = new String[] { "routes.txt", "trips.txt",
			"stops.txt", "shapes.txt", "calendar.txt" };

	final static Charset ENCODING = StandardCharsets.UTF_8;

	// Constructor
	// Populates all lists upon creation.
	@PostConstruct
	public void start() throws IOException {

		// StaticHandler text = new StaticHandler();
		for (int i = 0; i < 5; i++) {
			StringBuilder fullPathString = new StringBuilder();

			fullPathString.append(PATH_NAME + FILE_NAME[i]);
			log(fullPathString.toString());

			this.readFile(fullPathString.toString());

			switch (fullPathString.toString()) {

			case (PATH_NAME + "routes.txt"):
				if (trainRoutes.isEmpty()) {
					trainRoutes.addAll(setRoutes(lines));
					if (routeIdList.isEmpty()) {
						for (RouteStatic r : trainRoutes) {
							routeIdList.add(r.getId());
							//log(r.getId());
						}
					}
				} else {
					log("This list was not empty before you tried to add shit(ROUTES)");
				}
				break;
			case (PATH_NAME + "trips.txt"):
				if (trainTrips.isEmpty()) {
					log("Number of lines in trips.txt: " + lines.size());
					trainTrips.addAll(setTrips(lines));
				} else {
					log("This list was not empty before you tried to add shit(TRIPS)");
				}
				break;

			case (PATH_NAME + "stops.txt"):
				if (trainStops.isEmpty()) {
					trainStops.addAll(setStops(lines));
				} else {
					log("This list was not empty before you tried to add shit(STOPS)");
				}
				break;
			case (PATH_NAME + "shapes.txt"):
				if (trainShapes.isEmpty()) {
					log("Number of lines in shapes.txt: " + lines.size());
					trainShapes.addAll(setShapes(lines));
					/*
					for(ShapeStatic sh : trainShapes){
						try {
							
							if(sh.getId().contains("810_0001")){
								log("0" + '\t' + sh.getSequence(0) + '\t' + sh.getLat(0) + '\t' + sh.getLon(0) + '\t' + sh.getLength());
								log("1" + '\t' + sh.getSequence(1) + '\t' + sh.getLat(1) + '\t' + sh.getLon(1));
							}
							
						}catch (NullPointerException ex){
							log("Error: " + ex);
						}
						
					}
					*/
				} else {
					log("This list was not empty before you tried to add shit(SHAPES)");
				}
				break;
			case (PATH_NAME + "calendar.txt"):
				if (trainCalendar.isEmpty()) {
					log("Number of lines in calendar.txt: " + lines.size());
					trainCalendar.addAll(setCalendar(lines));

				}
			default:
				break;
			}
		}
	}
	
	@PreDestroy
	public void close(){
		//log("StaticHandler Destroyed.");
	}

	public ArrayList<RouteStatic> getTrainRoutes() {
		return trainRoutes;
	}

	public ArrayList<StopStatic> getTrainStops() {
		return trainStops;
	}

	public ArrayList<TripStatic> getTrainTrips() {
		return trainTrips;
	}

	public ArrayList<ShapeStatic> getTrainShapes() {
		return trainShapes;
	}

	public ArrayList<Calendar> getTrainCalendar() {
		return trainCalendar;
	}
	
	public ArrayList<StopStatic> getTrainSubStops(){
		return trainSubStops;
	}

	private void readFile(String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
		lines.clear();
		try (Scanner scanner = new Scanner(path, ENCODING.name())) {
			scanner.useDelimiter(",");
			while (scanner.hasNextLine()) {
				lines.add(scanner.nextLine());
			}
			scanner.close();
		}
	}

	private static void log(Object aMsg) {
		System.out.println(String.valueOf(aMsg));
	}

	public static void dumpRoute(RouteStatic r) {
		StringBuilder s = new StringBuilder();
		s.append(r.getId() + '\t' + r.getType() + '\t' + r.getColor() + '\t'
				+ r.getLongName());
		log(s);
	}

	public static void dumpTrip(TripStatic t) {

		StringBuilder s = new StringBuilder();
		s.append(t.getRouteId() + '\t' + t.getTripId() + '\t'
				+ t.getTripHeadsign() + '\t' + t.getDirId() + '\t'
				+ t.getShapeId());
		log(s);
	}

	public static void dumpStop(StopStatic s) {
		StringBuilder sb = new StringBuilder();
		sb.append(s.getId() + '\t' + s.getLat() + '\t' + s.getLon() + '\t'
				+ s.getName());
		log(sb);
	}

	private static List<RouteStatic> setRoutes(ArrayList<String> lines) {
		ArrayList<RouteStatic> routes = new ArrayList<RouteStatic>();
		for (int i = 0; i < lines.size(); i++) {
			int j = 0;
			if (i != 0) {
				RouteStatic r = new RouteStatic();
				try (Scanner s = new Scanner(lines.get(i))) {
					s.useDelimiter(",");
					while (s.hasNext()) {
						String temp = s.next();

						switch (j) {
						case 0:
							r.setId(temp);
							break;
						case 1:
							r.setA_id(temp);
							break;
						case 2:
							r.setShortName(temp);
							break;
						case 3:
							r.setLongName(temp);
							break;
						case 4:
							r.setDesc(temp);
							break;
						case 5:
							r.setType(temp);
							break;
						case 6:
							r.setUrl(temp);
							break;
						case 7:
							r.setColor(temp);
							break;
						case 8:
							r.setTextColor(temp);
							break;
						default:
							break;
						}
						j++;
					}
					if (r.getType() == 0 || r.getType() == 1) {
						routes.add(r);
					}
				}
			}

		}

		return routes;
	}

	private static ArrayList<TripStatic> setTrips(ArrayList<String> lines) {
		ArrayList<TripStatic> trips = new ArrayList<TripStatic>();
		for (int i = 0; i < lines.size(); i++) {
			int j = 0;
			if (i != 0) {
				TripStatic t = new TripStatic();
				try (Scanner s = new Scanner(lines.get(i))) {
					s.useDelimiter(",");
					while (s.hasNext()) {
						String temp = s.next();
						switch (j) {
						case 0:
							t.setRouteId(temp);
							break;
						case 1:
							t.setServiceId(temp);
							break;
						case 2:
							t.setTripId(temp);
							break;
						case 3:
							t.setTripHeadsign(temp);
							break;
						case 4:
							t.setTripShortName(temp);
						case 5:
							t.setDirId(temp);
							break;
						case 6:
							t.setBlockId(temp);
							break;
						case 7:
							t.setShapeId(temp);
							break;
						default:
							break;
						}
						j++;
					}
					if (t.getRouteId().contains("Red") || t.getRouteId().contains("Blue") || t.getRouteId().contains("Green") || t.getRouteId().contains("Orange") || t.getRouteId().contains("Mattapan")) {
						trips.add(t);
					}
				}
			}
		}
		if (trips.get(0).getRouteId() == null) {
			trips.remove(0);
		}
		return trips;
	}

	private static ArrayList<StopStatic> setStops(ArrayList<String> lines) {
		ArrayList<StopStatic> stops = new ArrayList<StopStatic>();
		for (int i = 0; i < lines.size(); i++) {
			int j = 0;
			if (i != 0) {
				StopStatic s = new StopStatic();
				try (Scanner scan = new Scanner(lines.get(i))) {
					scan.useDelimiter(",");
					while (scan.hasNext()) {
						String temp = scan.next();
						switch (j) {
						case 0:
							s.setId(temp);
							break;
						case 1:
							s.setCode(temp);
							break;
						case 2:
							s.setName(temp);
							break;
						case 3:
							s.setDesc(temp);
							break;
						case 4:
							s.setLat(temp);
							break;
						case 5:
							s.setLon(temp);
							break;
						case 6:
							s.setZoneId(temp);
							break;
						case 7:
							s.setUrl(temp);
							break;
						case 8:
							s.setType(temp);

							break;
						case 9:
							s.setParentStation(temp);
							if(s.getParentStation() == null){
								s.setParent(false);
							}else s.setParent(true);
							break;
						default:
							break;
						}
						j++;
					}
					if (s.getType() == 1) {
						stops.add(s);
						//System.out.println(s.getName());
					}else if(s.getType() == 0 && s.getId().contains("70")){
						if(s.getName().contains("Inbound") || s.getName().contains("Outbound")){
							stops.add(s);
						}
					}
				}
			}
		}
		if (!stops.isEmpty() && stops.get(0).getId() == null) {
			stops.remove(0);
		}
		return stops;
	}

	private static ArrayList<ShapeStatic> setShapes(ArrayList<String> lines) {
		ArrayList<ShapeStatic> shapes = new ArrayList<ShapeStatic>();
		String shapeId = null;
		String tempLat = null;
		String tempLon = null;
		int indexPerShape = 0;
		int numberOfShapes = 0;

		for (int i = 0; i < lines.size(); i++) {
			int j = 0;
			if (i != 0) {
				if (shapes.isEmpty()) {
					ShapeStatic s = new ShapeStatic();
					try (Scanner scan = new Scanner(lines.get(i))) {
						scan.useDelimiter(",");
						while (scan.hasNext()) {
							String temp = scan.next();

							switch (j) {
							case 0:
								shapeId = temp;
								s.setId(shapeId);
								break;
							case 1:
								tempLat = temp;
								break;
							case 2:
								tempLon = temp;
								break;
							case 3:
								s.setSequence(indexPerShape, temp);
								break;
							case 4:
								break;
							default:
								break;
							}

							j++;
						}

					}
					for (String rId : routeIdList) {
						String rIdk = rId.replace("_", "k");
						String rIdt = rId.replace("_", "t");
						if (shapeId.contains(rId) || shapeId.contains(rIdk)||shapeId.contains(rIdt)) {
							if (tempLat != null && tempLon != null) {
								s.setPos(indexPerShape, tempLat, tempLon);
							}else{
								s.setPos(indexPerShape, "-1", "-1");
							}
							shapes.add(s);

							numberOfShapes++;
							indexPerShape++;
							//log("FIRST OBJECT DONE: " + '\t' + "number of shapes: "+ numberOfShapes + '\t'+ "indexPerShape: " + indexPerShape);
						}
					}
				} else {

					try (Scanner scan = new Scanner(lines.get(i))) {
						scan.useDelimiter(",");
						ShapeStatic s = new ShapeStatic();
						String newShapeId = null;
						while (scan.hasNext()) {
							String temp = scan.next();

							switch (j) {
							case 0:
								if (shapeId.contains(temp)) {
									newShapeId = shapeId;						
									break;
								} else {
									shapeId = temp;
									s.setId(shapeId);
									ShapeStatic tempShape = shapes.get(numberOfShapes - 1);
									shapes.remove(numberOfShapes - 1);
									tempShape.setLength(indexPerShape);
									shapes.add(tempShape);
									
									indexPerShape = 0;
									break;
								}

							case 1:
								tempLat = temp;
								break;
							case 2:
								tempLon = temp;
								break;
							case 3:
								//just took out && newShapeId.contains(shapeId)
								if (newShapeId != null) {
									shapes.get(numberOfShapes - 1).setSequence(indexPerShape, temp);
								} else {
									s.setSequence(indexPerShape, temp);
								}
								break;
							case 4:
								break;
							default:
								break;
							}
							j++;
						}

						if (newShapeId != null && newShapeId.matches(shapeId)) {
							
							ShapeStatic tempShape = shapes.get(numberOfShapes -1);
							shapes.remove(numberOfShapes -1);
							tempShape.setPos(indexPerShape,tempLat, tempLon);
							shapes.add(numberOfShapes -1, tempShape);
							indexPerShape++;
						} else {
							for (String rId : routeIdList) {
								String rIdk = rId.replace("_", "k");
								String rIdt = rId.replace("_", "t");

								if (shapeId.contains(rId) || shapeId.contains(rIdk)||shapeId.contains(rIdt)) {
									if (tempLat != null && tempLon != null) {
										//log("are we getting here ever? pre");
											s.setPos(indexPerShape, tempLat, tempLon);
									}else{
										log("Templat and tempLon are null");
									}
									try {
										//log("yeah");
										//log(shapes.get(numberOfShapes - 1).getId() + '\t' + shapes.get(numberOfShapes - 1).getSequence(indexPerShape)
										//		+ '\t' + shapes.get(numberOfShapes -1 ).getLat(indexPerShape));
									} catch (NullPointerException ex){
										//log("didn't work");
									}
									shapes.add(s);
									//log("added new shape: "+ shapes.size());
									numberOfShapes++;
									indexPerShape++;
								}
							}

						}
					}
				}
			}
		}

		return shapes;
	}

	private static List<Calendar> setCalendar(ArrayList<String> lines) {
		List<Calendar> calendar = new ArrayList<Calendar>();

		for (int i = 0; i < lines.size(); i++) {
			int j = 0;
			if (i != 0) {
				Calendar c = new Calendar();
				try (Scanner s = new Scanner(lines.get(i))) {
					s.useDelimiter(",");

					while (s.hasNext()) {

						String temp = s.next();
						temp = temp.replace("\"", "");

						switch (j) {
						case 0:
							if (temp.contains("LRV") || temp.contains("RTL")) {
								c.setServiceId(temp);
								break;
							} else {
								s.close();
								IllegalStateException scanClosed = new IllegalStateException();
								throw (scanClosed);

							}
						case 1:
							c.setMonday(temp);
							break;
						case 2:
							c.setTuesday(temp);
							break;
						case 3:
							c.setWednesday(temp);
							break;
						case 4:
							c.setThursday(temp);
							break;
						case 5:
							c.setFriday(temp);
							break;
						case 6:
							c.setSaturday(temp);
							break;
						case 7:
							c.setSunday(temp);
							break;
						case 8:
							c.setStartDate(temp);
							break;
						case 9:
							c.setEndDate(temp);
							calendar.add(c);
							break;
						default:
							break;
						}
						j++;

					}
				} catch (IllegalStateException scanClosed) {

				}

			}

		}

		return calendar;
	}

}
