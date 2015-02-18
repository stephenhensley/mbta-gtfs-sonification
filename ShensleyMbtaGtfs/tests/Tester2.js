
inlets = 1;
outlets = 5;

/**
*Zeroith outlet will be timestamp of the most recent update.
*First-Eigth outlets will be either dicts or colls of line/direction vehicles.
* i.e. the first outlet might be orange line to forest hills, 
*		and second outlet would be orange line to oak grove.
**/

function bang(){
	getDict();
	createDicts();
}

function getDict(){
	var pathname = "Macintosh HD:/Users/stephenhensley/git/MbtaProj/ShensleyMbtaGtfs/tests/testCooked_NoArrays.json";
	var actualFile = new Dict("RealFileDict");
	actualFile.import_json(pathname);
	
	post(actualFile.getkeys());
	var currentUpdate = actualFile.get("timestampOfUpdate");
	outlet(0, currentUpdate);
	var stopsFull = actualFile.get("stops");
	var vehiclesFull = actualFile.get("vehicles");
	post('\n' + vehiclesFull.get(currentUpdate).getkeys());	
	var currentVehicles = vehiclesFull.get(currentUpdate);
	

	var vehicleKeys = currentVehicles.getkeys();
	for(i = 0; i < currentVehicles.getkeys().length; i++){

		var vehicle = currentVehicles.get(i);

	
	//add the closestStop/closestDistance properties to each vehicle.
		var lat = parseFloat(vehicle.get("lat"));
		var lon = parseFloat(vehicle.get("lon"));
		var ber = parseFloat(vehicle.get("bearing"));
		var stopArray = calculateClosestStop(stopsFull, lat,lon, ber);
		actualFile.replace("vehicles::" + currentUpdate + "::" + vehicleKeys[i]+ "::closestStopDist", stopArray[1]);
		actualFile.replace("vehicles::" + currentUpdate + "::" + vehicleKeys[i]+ "::closestStopName", stopArray[0]);
		//post(actualFile.get("vehicles").get(currentUpdate).get(i).getkeys());
	}
}

function createDicts(){
	var actualFile = new Dict("RealFileDict");
	//Declare variables to access the line/direction specific dictionaries.
	var gI = new Dict("greenIn");
	var gO = new Dict("greenOut");
	var oI = new Dict("orangeIn");
	var oO = new Dict("orangeOut");
	var rI = new Dict("redIn");
	var rO = new Dict("redOut");
	var bI = new Dict("blueIn");
	var bO = new Dict("blueOut");
	//assign index's for each line first. (just for some visual feedback)
	var gInd = 0;
	var oInd = 0;
	var bInd = 0;
	var rInd = 0;
	var currentUpdate = actualFile.get("timestampOfUpdate");
	//outlet(0, currentUpdate);
	var stopsFull = actualFile.get("stops");
	var vehiclesFull = actualFile.get("vehicles");
	//post('\n' + vehiclesFull.get(currentUpdate).getkeys());	
	var currentVehicles = vehiclesFull.get(currentUpdate);
	for( i = 0; i < currentVehicles.getkeys().length; i++){
		var vehicle = currentVehicles.get(i);
	//export each vehicle to a dictionary specific to the line/direction it belongs to.
		var routeName = vehicle.get("routeName");
		var trip = vehicle.get("trip");
		var outString = null;
		switch(routeName){
			case "\"Green Line\"":
				if(trip.search("_0") != -1){
					//guessing outbound for now
					gO.set(vehicle.get("vehicleId"), vehicle);
				}else if(trip.search("_1") != -1){
					//guessing inbound for now(based entirely on average bearing)
					gI.set(vehicle.get("vehicleId"), vehicle);
				}
				gInd++;
				break;
			case "\"Orange Line\"":
				if(trip === "\"Oak Grove\""){
					oI.set(vehicle.get("vehicleId"), vehicle);
				}else if(trip === "\"Forest Hills\""){
					oO.set(vehicle.get("vehicleId"), vehicle);
				}else{
					post("The orange line trip contains an error: " + trip);
				}
				oInd++;
				break;
			case "\"Red Line\"":
				if(trip === "\"Alewife\""){
					rI.set(vehicle.get("vehicleId"), vehicle);
				}else if(trip === "\"Ashmont\""){
					rO.set(vehicle.get("vehicleId"), vehicle);
				}else if(trip==="\"Braintree\""){
					rO.set(vehicle.get("vehicleId"), vehicle);
				}else{
					post("The orange line trip contains an error: " + trip);
				}
				rInd++;
				break;
			case "\"Blue Line\"":
				if(trip === "\"Wonderland\""){
					bO.set(vehicle.get("vehicleId"), vehicle);
				}else if(trip === "\"Bowdoin\""){
					bI.set(vehicle.get("vehicleId"), vehicle);
				}else{
					post("The orange line trip contains an error: " + trip);
				}
				bInd++;	
				break;
			default:
				break;
		}	
					
		
	}
	outlet(1, "green line: " + gInd);
	outlet(2, "Orange line: " + oInd);
	outlet(3, "Red line: " + rInd);
	outlet(4, "Blue line: " + bInd);
	
}


function calculateClosestStop(stops, lat, lon, ber){
	//post(typeof lat);
	stopKeys = stops.getkeys();
	//post(stopKeys);
	var distanceToStop = null;
	var closestDistance = null;
	var closestBearing = null;
	var nameOfStop;
	for(var i = 0; i < stopKeys.length; i++){
		stopLat = parseFloat(stops.get(i).get("lat"));
		stopLon = parseFloat(stops.get(i).get("lon"));
		distanceToStop = calculateDistance(stopLat, stopLon, lat, lon);
		if(closestDistance === null || closestDistance > distanceToStop){
			closestDistance = distanceToStop;
			nameOfStop = stops.get(i).get("name");
			/*
			closestBearing = calculateFinalBearing(lat, lon, stopLat, stopLon);
			*/
		}			
	}
	//post('\n' + nameOfStop + '\t' + closestDistance + '\t' + closestBearing);
	return [nameOfStop, closestDistance];
}

function calculateDistance(lat1, lon1, lat2, lon2){
	var R = 6371000.0; //mean radius of earth in metres.
	var dLat = (lat2 - lat1) * Math.PI / 180;  // deg2rad below
  	var dLon = (lon2 - lon1) * Math.PI / 180;
  	var a = 
     	0.5 - Math.cos(dLat)/2 + 
     	Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * 
     	(1 - Math.cos(dLon))/2;

  	return R * 2 * Math.asin(Math.sqrt(a));
}

function calculateFinalBearing(vLat, vLon, sLat, sLon){
	var vLatR = vLat*Math.PI/180;
	var vLonR = vLon*Math.PI/180;
	var sLatR = vLat*Math.PI/180;
	var sLonR = vLat*Math.PI/180;
	
	var y = Math.sin(sLonR * vLonR) * Math.cos(sLatR);
	var x = Math.cos(vLatR) * Math.sin(sLatR) * Math.sin(vLatR) * Math.cos(sLatR) *Math.cos(sLonR - vLonR);
	return (Math.atan2(y,x)*180)/Math.PI;
}	