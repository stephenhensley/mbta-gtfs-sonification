
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
	for(i = 0; i < currentVehicles.getkeys().length; i++){
		//post('\n' + currentVehicles.get(i).getkeys());
		var vehicle = currentVehicles.get(i);
		post('\n' + vehicle.get("vehicleId") + '\t' + vehicle.get("routeName") + '\t' + 
				vehicle.get("trip") + '\t' + vehicle.get("lat") + '\t' + 
					vehicle.get("lon") + '\t' + vehicle.get("bearing"));
		
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