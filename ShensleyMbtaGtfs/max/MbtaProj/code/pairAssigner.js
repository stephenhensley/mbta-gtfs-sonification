inlets = 1;
outlets = 20;

function getkeys(){
	if(jsarguments.length>1){
		var inboundName = jsarguments[1];
	}else var inboundName = "wrongIn";
	if(jsarguments.length>2){
		var outboundName = jsarguments[2];
	}else var outboundName = "wrongOut";
	var inboundDict = new Dict(inboundName);
	var outboundDict = new Dict(outboundName);
	if(inboundDict.getkeys() != null){
		var numIn = inboundDict.getkeys().length;
		}else numIn = 0;
	if(outboundDict.getkeys() != null){
		var numOut = outboundDict.getkeys().length;
		}else numOut = 0;
	var dictNameIn = inboundDict.getkeys();
	var dictNameOut = outboundDict.getkeys();
	
	post('\n' + numIn + '\t' + numOut);
	
	if(numIn == numOut){
		for(var i = 0; i < numIn; i++){
			var tempOut = outboundDict.get(dictNameOut[i]);
			var tempIn = inboundDict.get(dictNameIn[i]);
			outlet(i, tempOut.get("routeId"), tempOut.get("closestStopName"), tempOut.get("closestStopDist"), tempOut.get("percentToEnd"), tempOut.get("currentStatus"), tempIn.get("routeId"), tempIn.get("closestStopName"), tempIn.get("closestStopDist"), tempIn.get("percentToEnd"), tempIn.get("currentStatus"));
		}
		for(var i = numIn; i<outlets; i++){
			outlet(i, "000_", "noTrain", -1.0, -1.0, "NO_TRAIN", "000_", "noTrain", -1.0, -1.0, "NO_TRAIN");
		}	
	}
	if(numOut > numIn){
		for( var i = 0; i < numIn; i++){
			var tempOut = outboundDict.get(dictNameOut[i]);
			var tempIn = inboundDict.get(dictNameIn[i]);
			outlet(i, tempOut.get("routeId"), tempOut.get("closestStopName"), tempOut.get("closestStopDist"), tempOut.get("percentToEnd"), tempOut.get("currentStatus"), tempIn.get("routeId"), tempIn.get("closestStopName"), tempIn.get("closestStopDist"), tempIn.get("percentToEnd"), tempIn.get("currentStatus"));
		}
		for( var i = numIn; i < numOut; i++){
			var tempOut = outboundDict.get(dictNameOut[i]);
			var tempIn = inboundDict.get(dictNameIn[i]);
			outlet(i, tempOut.get("routeId"), tempOut.get("closestStopName"), tempOut.get("closestStopDist"), tempOut.get("percentToEnd"), tempOut.get("currentStatus"), "000_", "Default", -1.0, -1.0, "NO_TRAIN");
		}
		for(var i = numOut; i < outlets; i++){
			outlet(i, "000_", "noTrain", -1.0, -1.0, "NO_TRAIN", "000_", "noTrain", -1.0, -1.0, "NO_TRAIN");
		}	
	}
	if(numIn > numOut){
		for( var i = 0; i < numOut; i++){
			var tempOut = outboundDict.get(dictNameOut[i]);
			var tempIn = inboundDict.get(dictNameIn[i]);
			//post('\n'+ i);
			outlet(i, tempOut.get("routeId"), tempOut.get("closestStopName"), tempOut.get("closestStopDist"), tempOut.get("percentToEnd"), tempOut.get("currentStatus"), tempIn.get("routeId"), tempIn.get("closestStopName"), tempIn.get("closestStopDist"), tempIn.get("percentToEnd"), tempIn.get("currentStatus"));
		}
		for(var i = numOut; i < numIn; i++){
			var tempIn = inboundDict.get(dictNameIn[i]);
			outlet(i, "000_", "Default", -1.0, tempIn.get("routeId"), tempIn.get("closestStopName"), tempIn.get("closestStopDist"), tempIn.get("percentToEnd"), tempIn.get("currentStatus"));
		}
		for(var i = numIn; i < outlets; i++){
			outlet(i, "000_", "noTrain", -1.0, -1.0, "NO_TRAIN", "000_", "noTrain", -1.0, -1.0, "NO_TRAIN");
		}	
	}
}