package org.shensley.gtfs;

//import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
//import java.util.Map;
//import java.util.HashMap;





public class textInput {
	
	private static ArrayList<String> lines = new ArrayList<String>();
	private static List<String> routeIdList = new ArrayList<String>();
	private static List<Route> trainRoutes = new ArrayList<Route>();
	private static List<Trip> trainTrips = new ArrayList<Trip>();
	private static List<Stop> trainStops = new ArrayList<Stop>();
	private static List<Shape> trainShapes = new ArrayList<Shape>();
	
	final static String PATH_NAME = "/Users/stephenhensley/MBTA_programming/MBTA_GTFS/";
	final static String[] FILE_NAME = new String[]{"routes.txt", "trips.txt", "stops.txt","shapes.txt"};

	final static Charset ENCODING = StandardCharsets.UTF_8;
	//private static Map<Integer, String> _parameterByIndex = new HashMap<Integer, String>();
	//private static Map<Integer, ArrayList<String>> _valueByIndex = new HashMap<Integer, ArrayList<String>>();
	
	
	public static void main(String... aArgs) throws IOException{
		

		
		textInput text = new textInput();
		
		for(int i = 0;i<4;i++){
			StringBuilder fullPathString = new StringBuilder();
		
			fullPathString.append(PATH_NAME+FILE_NAME[i]);
			log(fullPathString.toString());
		
			text.readFile(fullPathString.toString());
			
			switch(fullPathString.toString()){
			
				case (PATH_NAME+"routes.txt"):
					if(trainRoutes.isEmpty()){
						trainRoutes.addAll(setRoutes(lines));
						if(routeIdList.isEmpty()){
							for(Route r : trainRoutes){
								routeIdList.add(r.getId());
							}
						}
					}else{log("This list was not empty before you tried to add shit(ROUTES)");}
				break;
				case (PATH_NAME+"trips.txt"):
					if(trainTrips.isEmpty()){
						log("Number of lines in trips.txt: " + lines.size());
						trainTrips.addAll(setTrips(lines));
					}else{log("This list was not empty before you tried to add shit(TRIPS)");}
				break;
					
				case (PATH_NAME+"stops.txt"):
					if(trainStops.isEmpty()){
						trainStops.addAll(setStops(lines));
					}else{log("This list was not empty before you tried to add shit(STOPS)");}
				break;
				case PATH_NAME+"shapes.txt":
					if(trainShapes.isEmpty()){
						log("Number of lines in shapes.txt: " + lines.size());
						trainShapes.addAll(setShapes(lines));
					}else{log("This list was not empty before you tried to add shit(SHAPES)");}
				break;
				default:
				break;
			}
		}
		
		
		//Testing for retrieval

		
	}
	
	
	
	void readFile(String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
		lines.clear();
		try (Scanner scanner = new Scanner(path, ENCODING.name())){
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){
				lines.add(scanner.nextLine());
			}
			scanner.close();
		}
	}
	
	private static void log(Object aMsg){
		System.out.println(String.valueOf(aMsg));
	}
	
	public static void dumpRoute(Route r){
		StringBuilder s = new StringBuilder();
		s.append(r.getId() + '\t' + r.getType() + '\t' + r.getColor() + '\t' + r.getLongName());
		log(s);
	}

	public static void dumpTrip(Trip t){

		StringBuilder s = new StringBuilder();
		s.append(t.getRouteId() + '\t' + t.getTripId() + '\t' + t.getTripHeadsign() + '\t' + t.getDirId() + '\t' + t.getShapeId());
		log(s);
	}

	public static void dumpStop(Stop s){
		StringBuilder sb = new StringBuilder();
		sb.append(s.getId() + '\t' + s.getLat() + '\t' + s.getLon() + '\t' + s.getName());
		log(sb);
	}
	
	public static List<Route> setRoutes(ArrayList<String> lines){
		List<Route> routes = new ArrayList<Route>();
		for(int i = 0; i<lines.size(); i++){
			int j = 0;
			if(i != 0){
				Route r = new Route();
				try(Scanner s = new Scanner(lines.get(i))){
					s.useDelimiter(",");
					while(s.hasNext()){
						String temp = s.next();
			
						
						switch(j){
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
					if(r.getType() == 0 || r.getType() == 1){
						routes.add(r);
					}
				}
			}
					
		}
		
		return routes;
	}

	public static List<Trip> setTrips(ArrayList<String> lines){
		List<Trip> trips = new ArrayList<Trip>();
		for(int i = 0; i<lines.size(); i++){
			int j = 0;
			if(i != 0){
				Trip t = new Trip();
				try(Scanner s = new Scanner(lines.get(i))){
					s.useDelimiter(",");
					while(s.hasNext()){
						String temp = s.next();	
						switch(j){
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
					if(t.getRouteId().contains("_")){
						trips.add(t);
					}
				}
			}					
		}
		if(trips.get(0).getRouteId()==null){
			trips.remove(0);
		}
		return trips;
	}

	public static List<Stop> setStops(ArrayList<String> lines){
		List<Stop> stops = new ArrayList<Stop>();
		for(int i = 0; i < lines.size(); i++){
			int j = 0;
			if(i != 0){
				Stop s = new Stop();
				try(Scanner scan = new Scanner(lines.get(i))){
					scan.useDelimiter(",");
					while(scan.hasNext()){
						String temp = scan.next();
						switch(j){
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
					if(s.getType() == 1){
						stops.add(s);
					}
				}
			}	
		}	
		if(!stops.isEmpty() && stops.get(0).getId() == null){
			stops.remove(0);
		}
		return stops;
	}
	
	public static List<Shape> setShapes(ArrayList<String> lines){
		List<Shape> shapes = new ArrayList<Shape>();
		String shapeId = null;
		String tempLat = null; 
		String tempLon = null;
		int indexPerShape = 0;
		int numberOfShapes = 0;
		
		for(int i = 0; i < lines.size(); i++){
			int j = 0;
			if(i != 0){
				if(shapes.isEmpty()){
					Shape s = new Shape();
					try(Scanner scan = new Scanner(lines.get(i))){
						scan.useDelimiter(",");
						while(scan.hasNext()){
							String temp = scan.next();
							
							switch(j){
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
					for(String rId : routeIdList){
						rId = rId.replace("_", "");
						if(shapeId.contains(rId)){
							if(tempLat != null && tempLon != null){
								s.setPos(indexPerShape, tempLat, tempLon);
							}
							shapes.add(s);
							
							numberOfShapes++;
							indexPerShape++;
						}
					}
				}else{

					try(Scanner scan = new Scanner(lines.get(i))){
						scan.useDelimiter(",");
						Shape s = new Shape();
						String newShapeId = null;
						while(scan.hasNext()){
							String temp = scan.next();


							
							switch(j){
							case 0:
								if(shapeId.matches(temp)){
									newShapeId = shapeId;
									break;
								}else{
									shapeId = temp;
									s.setId(shapeId);
									shapes.get(numberOfShapes-1).setLength(indexPerShape);
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
								if(newShapeId != null && newShapeId.matches(shapeId)){
									shapes.get(numberOfShapes-1).setSequence(indexPerShape, temp);
								}else{
									s.setSequence(indexPerShape, temp);
								}
								break;
							case 4:
								break;
							}
							j++;
						}

						if(newShapeId != null && newShapeId.matches(shapeId)){
							indexPerShape++;
						}else{
							for(String rId : routeIdList){
								rId = rId.replace("_", "");
								if(shapeId.contains(rId)){
									if(tempLat != null && tempLon != null){
										if(newShapeId != null && newShapeId.matches(shapeId)){
											shapes.get(numberOfShapes-1).setPos(indexPerShape, tempLat, tempLon);
										}else{
											s.setPos(indexPerShape, tempLat, tempLon);
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
}
