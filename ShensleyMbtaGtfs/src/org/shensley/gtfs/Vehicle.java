/**
 * Copyright (C) 2015 Stephen Hensley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.shensley.gtfs;

public class Vehicle {
	

	  private String id;

	  private double lat;

	  private double lon;

	  private String route; 

	  private String tripId;
	  
	  private String stopId;

	  private double bearing;
	  
	  private long lastUpdate;
	  
	  private int stopSeq;
	  
	  //try here first current-nextStopName, Distance
	  private String closestStopName;
	  private String nextStopName;
	  private double closestStopDist;
	  private double nextStopDist;
	  private double percentToEnd;
	  private String currentStatus;



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
	  
	  public String getStopId(){
		  return stopId;
	  }
	  
	  public void setStopId(String stopId){
		  this.stopId = stopId;
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
	  
	  public String getClosestStopName(){
		  return closestStopName;
	  }
	  
	  public void setClosestStopName(String stopName){
		  this.closestStopName = stopName;
	  }
	  
	  public String getNextStopName(){
		  return nextStopName;
	  }
	  
	  public void setNextStopName(String stopName){
		  this.nextStopName = stopName;
	  }
	  
	  public double getClosestStopDist(){
		  return closestStopDist;
	  }
	  
	  public void setClosestStopDist(double stopDist){
		  this.closestStopDist = stopDist;
	  }
	  
	  public double getNextStopDist(){
		  return nextStopDist;
	  }
	  
	  public void setNextStopDist(double stopDist){
		  this.nextStopDist = stopDist;
	  }
	  
	  public double getPercentToEnd(){
		  return percentToEnd;
	  }
	  
	  public void setPercentToEnd(double percentToEnd){
		  this.percentToEnd = percentToEnd;
	  }
	  
	  public int getStopSeq(){
		  return stopSeq;
	  }
	  
	  public void setStopSeq(int stopSeq){
		  this.stopSeq = stopSeq;
	  }
	  
	  public String getCurrentStatus(){
		  return currentStatus;
	  }
	  
	  public void setCurrentStatus(String currentStatus){
		  this.currentStatus = currentStatus;
	  }
	  


}
