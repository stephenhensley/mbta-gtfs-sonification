inlets = 1;
outlets = 1;

function bang(){
	var blueWonderland = new Dict("blueOut");
	var fullFile = new Dict("RealFileDict");
	var stops = fullFile.get("stops");
	//post('\n' + blueWonderland.getkeys());
	vehicles = blueWonderland.getkeys();
	for(v in vehicles){
		//post('\n' + blueWonderland.get(vehicles[v]).get("lat"));
		var lat = parseFloat(blueWonderland.get(vehicles[v]).get("lat"));
		var lon = parseFloat(blueWonderland.get(vehicles[v]).get("lon"));
		var stopArray = calculateClosestStop(stops, lat,lon);
		blueWonderland.replace(vehicles[v] + "::closestStopDist", stopArray[1]);
		blueWonderland.replace(vehicles[v] + "::closestStopName", stopArray[0]);
		//post('\n' + vehicles[v]+"::closestStopDist" + '\t' + stopArray[1]);
	}	
	outlet(0, "done");
}	

function calculateClosestStop(stops, lat, lon){
	//post(typeof lat);
	stopKeys = stops.getkeys();
	//post(stopKeys);
	var distanceToStop = null;
	var closestDistance = null;
	var nameOfStop;
	for(var i = 0; i < stopKeys.length; i++){
		stopLat = parseFloat(stops.get(i).get("lat"));
		stopLon = parseFloat(stops.get(i).get("lon"));
		distanceToStop = calculateDistance(stopLat, stopLon, lat, lon);
		if(closestDistance === null || closestDistance > distanceToStop){
			closestDistance = distanceToStop;
			nameOfStop = stops.get(i).get("name");
		}			
	}
	//post('\n' + nameOfStop + '\t' + closestDistance);
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