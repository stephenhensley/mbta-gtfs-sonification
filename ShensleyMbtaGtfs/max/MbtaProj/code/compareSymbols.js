inlets = 1;
outlets = 4;

var current = "foo";
var previous = "oof";
function anything(val){
	if(current === "foo"){
		current = messagename.replace(" ", "");
	}else if(messagename.replace(" ", "") != current){
		previous = current;
		current = messagename.replace(" ", "");
		outlet(0, 0);
		outlet(1, "Current: " + current + "Previous: " + previous);
		outlet(2, current);
	}else{
		outlet(1, "symbols are equal");
		outlet(3, current.replace(" ", ""));
		//post(current);
	}	
		
	
		
}	