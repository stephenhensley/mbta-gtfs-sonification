package org.shensley.gtfs;

public class Vehicle {
	

	  private String id;

	  private double lat;

	  private double lon;

	  private String route; 

	  private String tripId;

	  private double bearing;
	  
	  private long lastUpdate;  


	  public String getId() {
	    return id;
	  }

	  public void setId(String id) {
	    this.id = id;
	  }

	  public double getLat() {
	    return lat;
	  }

	  public void setLat(double lat) {
	    this.lat = lat;
	  }

	  public double getLon() {
	    return lon;
	  }

	  public void setLon(double lon) {
	    this.lon = lon;
	  }

	  public String getRouteId(){
	    return route;
	  }

	  public void setRouteId(String route){
	    this.route = route;
	  }


	//These two are not tested.
	  public String getTripId(){
	    return tripId;
	  }

	  public void setTrip(String trip){
	    this.tripId = trip;
	  }

	  public long getLastUpdate() {
	    return lastUpdate;
	  }

	  public void setLastUpdate(long lastUpdate) {
	    this.lastUpdate = lastUpdate;
	  }

	  public double getBearing() {
	    return bearing;
	  }

	  public void setBearing(double bearing) {
	    this.bearing = bearing;
	  }


}
