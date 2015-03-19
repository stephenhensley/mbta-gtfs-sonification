inlets = 1;
outlets = 9;

function bang(){
	getting();
}
	
function getting(){
	var d = new Dict("Import");
	var keys = d.getkeys();
	post_info(d.name, keys);
	for(var i = 0;i<keys.length;i++){
		if(keys[i] == "vehicles"){
			var vehicles = d.get(keys[i]);
			var vehiclesName = vehicles.name;
			vehicles.set(d.get(keys[i]));
		}else if(keys[i] == "stops"){
			var stops = d.get(keys[i]);
			var stopsName = stops.name;
		}else if(keys[i] == "timestampOfUpdate"){
			var mostRecentUpdate = d.get(keys[i]);
			outlet(0, mostRecentUpdate);
		}	
	}
	var vehiclesCurrent = vehicles.get(mostRecentUpdate);
	post_info(vehiclesName, vehicles.getkeys());
	//post('\n'+vehicles.get("Sat Feb 07 14:12:48 EST 2015");
	//var currentKeys = vehiclesCurrent.getkeys();
	post(vehiclesCurrent.length + '\n');
	var currentV = new Object();
	currentV = currentV.set(;
	post('\n' + currentV);

}

function post_info(dictname, keys)
{
	post("Info regarding the dictionary named '", dictname, "': ");
	post();
	post("    Keys: " + keys);
	post();
}

function jsobj_to_dict(o) {
	var d = new Dict();
	for (var keyIndex in o)	{
		var value = o[keyIndex];

		if (!(typeof value === "string" || typeof value === "number")) {
			var isEmpty = true;
			for (var anything in value) {
				isEmpty = false;
				break;
			}
			if (isEmpty) {
				value = new Dict();
			}else {
				var isArray = true;
				for (var valueKeyIndex in value) {
					if (isNaN(parseInt(valueKeyIndex))) {
						isArray = false;
						break;
					}
			}
			if (!isArray) {
				value = jsobj_to_dict(value);
			}
		}
	}
	d.set(keyIndex, value);
	}
return d;
}