package org.shensley.gtfs;

public class TripStatic {
	
	private String routeId;
	
	private String serviceId;
	
	private String tripId;
	
	private String tripHeadsign;
	
	private String tripShortName;
	
	private String dirId;
	
	private String blockId;
	
	private String shapeId;
	
	public String getRouteId(){
		return routeId;
	}
	
	public void setRouteId(String routeId){
		this.routeId = routeId;
	}
	
	public String getServiceId(){
		return serviceId;
	}
	
	public void setServiceId(String serviceId){
		this.serviceId = serviceId;
	}
	
	public String getTripId(){
		return tripId;
	}
	
	public void setTripId(String tripId){
		this.tripId = tripId;
	}
	
	public String getTripHeadsign(){
		return tripHeadsign;
	}
	
	public void setTripHeadsign(String tripHeadsign){
		this.tripHeadsign = tripHeadsign;
	}
	
	public String getTripShortName(){
		return tripShortName;
	}
	
	public void setTripShortName(String tripShortName){
		this.tripShortName = tripShortName;
	}
	
	public String getDirId(){
		return dirId;
	}
	
	public void setDirId(String dirId){
		this.dirId = dirId;
	}
	
	public String getBlockId(){
		return blockId;
	}
	
	public void setBlockId(String blockId){
		this.blockId = blockId;
	}
	
	public String getShapeId(){
		return shapeId;
	}
	
	public void setShapeId(String shapeId){
		this.shapeId = shapeId;
	}

}
