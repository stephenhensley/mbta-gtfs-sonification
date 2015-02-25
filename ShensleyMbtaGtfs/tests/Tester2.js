
inlets = 1;
outlets = 6;

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
	var pathname = "Macintosh HD:/Users/stephenhensley/git/MbtaProj/ShensleyMbtaGtfs/tests/testCooked_withStops.json";
	var actualFile = new Dict("RealFileDict");
	actualFile.import_json(pathname);	
	var currentUpdate = actualFile.get("timestampOfUpdate");
	outlet(0, currentUpdate);
	var stopsFull = actualFile.get("stops");
	var vehiclesFull = actualFile.get("vehicles");
	var currentVehicles = vehiclesFull.get(currentUpdate);
	outlet(1, currentVehicles.getkeys().length);
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

				break;
			case "\"Orange Line\"":
				if(trip === "\"Oak Grove\""){
					oI.set(vehicle.get("vehicleId"), vehicle);
				}else if(trip === "\"Forest Hills\""){
					oO.set(vehicle.get("vehicleId"), vehicle);
				}else{
					post("The orange line trip contains an error: " + trip);
				}

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
				break;
			case "\"Blue Line\"":
				if(trip === "\"Wonderland\""){
					bO.set(vehicle.get("vehicleId"), vehicle);
				}else if(trip === "\"Bowdoin\""){
					bI.set(vehicle.get("vehicleId"), vehicle);
				}else{
					post("The orange line trip contains an error: " + trip);
				}
				break;
			default:
				break;
		}		
	}

	
	var greenOutput = "greenlineIn " + getSize(gI) + " greenlineOut " + getSize(gO);
	var orangeOutput = "orangelineIn " + getSize(oI) + " orangelineOut " + getSize(oO);
	var redOutput = "redlineIn " + getSize(rI) + " redlineOut " + getSize(rO);
	var blueOutput = "bluelineIn " + getSize(bI) + " bluelineOut " + getSize(bO);
	
	outlet(2, greenOutput);
	outlet(3, orangeOutput);
	outlet(4, redOutput);
	outlet(5, blueOutput);
	post("done\n");
}

function getSize(dict){
	var output;
	if(dict.getkeys() === null){
		output = 0;
	}else{
		 output = dict.getkeys().length;
	}
	return output;
}