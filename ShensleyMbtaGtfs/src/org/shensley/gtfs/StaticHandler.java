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
	private static List<String> routeIdList = new ArrayList<String>();
	private static List<RouteStatic> trainRoutes = new ArrayList<RouteStatic>();
	private static List<TripStatic> trainTrips = new ArrayList<TripStatic>();
	private static List<StopStatic> trainStops = new ArrayList<StopStatic>();
	private static List<ShapeStatic> trainShapes = new ArrayList<ShapeStatic>();
	private static List<Calendar> trainCalendar = new ArrayList<Calendar>();

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
		log("StaticHandler Destroyed.");
	}

	public List<RouteStatic> getTrainRoutes() {
		return trainRoutes;
	}

	public List<StopStatic> getTrainStops() {
		return trainStops;
	}

	public List<TripStatic> getTrainTrips() {
		return trainTrips;
	}

	public List<ShapeStatic> getTrainShapes() {
		return trainShapes;
	}

	public List<Calendar> getTrainCalendar() {
		return trainCalendar;
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
		List<RouteStatic> routes = new ArrayList<RouteStatic>();
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

	private static List<TripStatic> setTrips(ArrayList<String> lines) {
		List<TripStatic> trips = new ArrayList<TripStatic>();
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
					if (t.getRouteId().contains("_")) {
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

	private static List<StopStatic> setStops(ArrayList<String> lines) {
		List<StopStatic> stops = new ArrayList<StopStatic>();
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
							break;
						default:
							break;
						}
						j++;
					}
					if (s.getType() == 1) {
						stops.add(s);
					}
				}
			}
		}
		if (!stops.isEmpty() && stops.get(0).getId() == null) {
			stops.remove(0);
		}
		return stops;
	}

	private static List<ShapeStatic> setShapes(ArrayList<String> lines) {
		List<ShapeStatic> shapes = new ArrayList<ShapeStatic>();
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
						rId = rId.replace("_", "");
						if (shapeId.contains(rId)) {
							if (tempLat != null && tempLon != null) {
								s.setPos(indexPerShape, tempLat, tempLon);
							}
							shapes.add(s);

							numberOfShapes++;
							indexPerShape++;
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
								if (shapeId.matches(temp)) {
									newShapeId = shapeId;
									break;
								} else {
									shapeId = temp;
									s.setId(shapeId);
									shapes.get(numberOfShapes - 1).setLength(
											indexPerShape);
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
								if (newShapeId != null
										&& newShapeId.matches(shapeId)) {
									shapes.get(numberOfShapes - 1).setSequence(
											indexPerShape, temp);
								} else {
									s.setSequence(indexPerShape, temp);
								}
								break;
							case 4:
								break;
							}
							j++;
						}

						if (newShapeId != null && newShapeId.matches(shapeId)) {
							indexPerShape++;
						} else {
							for (String rId : routeIdList) {
								rId = rId.replace("_", "");
								if (shapeId.contains(rId)) {
									if (tempLat != null && tempLon != null) {
										if (newShapeId != null
												&& newShapeId.matches(shapeId)) {
											shapes.get(numberOfShapes - 1)
													.setPos(indexPerShape,
															tempLat, tempLon);
										} else {
											s.setPos(indexPerShape, tempLat,
													tempLon);
										}
									}
									shapes.add(s);
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
