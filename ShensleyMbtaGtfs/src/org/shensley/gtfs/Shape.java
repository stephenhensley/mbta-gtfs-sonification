package org.shensley.gtfs;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Shape {
	
	private String id;
	private int length;
	
	private ArrayList<String> sequenceByIndex = new ArrayList<String>();
	private Map<String,double[]> posBySequence = new HashMap<String,double[]>();
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public int getLength(){
		return length;
	}
	
	public void setLength(int length){
		this.length = length;
	}
	
	public String getSequence(int index){
		return sequenceByIndex.get(index);
	}
	
	public void setSequence(int index, String sequence){
			this.sequenceByIndex.add(index, sequence);		
	}
	
	public double getLat(int index){
		return posBySequence.get(sequenceByIndex.get(index))[0];
	}
	
	public double getLon(int index){
		return posBySequence.get(sequenceByIndex.get(index))[1];
	}
	
	public void setPos(int index, String lat, String lon){
		lat = lat.replace("\"", "");
		lon = lon.replace("\"", "");
		if(this.posBySequence.get(index) == null){
			if(lat != null && lon != null){
				double[] pos = new double[2];
				pos[0] = Double.parseDouble(lat);
				pos[1] = Double.parseDouble(lon);
				if(sequenceByIndex.get(index)!= null){
					this.posBySequence.put(sequenceByIndex.get(index), pos);
				}
			}
		}
	}
	
	/* This may not work. I may simply need a setPos function to load a Double[] 
	public void setLat(int index, String lat){
		if(this.posBySequence.get(index)[0] == null){
			if(lat != null){
				this.posBySequence.add(index, Double.parseDouble(lat)[0]);
			}
		}
	}
	*/
	

	
	/* Same as above... possibly deprecated.
	public void setLon(String lon){
		
	}
	*/
	
	//THIS IS WHERE IM KINDA STUCK... GOTTA PLAN OUT HOW TO GET MULTIPLE LINES LOADED UP, ETC.
	

}
