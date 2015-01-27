package org.shensley.gtfs;

public class StopStatic {
	
	private String id;
	private String code;
	private String name;
	private String desc;
	private double lat;
	private double lon;
	private String zoneId;
	private String url;
	private int type;
	private String parentStation;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id){
		id = id.replace("\"", "");
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc){
		this.desc = desc;
	}
	
	public double getLat(){
		return lat;
	}
	
	public void setLat(String lat){
		lat = lat.replace("\"","");
		if(!lat.isEmpty() && lat != null){
			this.lat = Double.parseDouble(lat);
		}
	}
	
	public double getLon(){
		return lon;
	}
	
	public void setLon(String lon){
		lon = lon.replace("\"","");
		if(!lon.isEmpty() && lon != null){
			this.lon = Double.parseDouble(lon);
		}
	}
	
	public String getZoneId(){
		return zoneId;
	}
	
	public void setZoneId(String zoneId){
		this.zoneId = zoneId;
	}
	
	public String getUrl(){
		return url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public int getType(){
		return type;
	}
	
	public void setType(String type){
		type = type.replace("\"", "");
		if(!type.isEmpty()){
			this.type = Integer.parseInt(type);
		}
	}
	
	public String getParentStation(){
		return parentStation;
	}
	
	public void setParentStation(String parentStation){
		this.parentStation = parentStation;
	}
	
	
	
	
	
	
	

}
