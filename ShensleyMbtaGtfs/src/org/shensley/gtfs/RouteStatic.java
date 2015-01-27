package org.shensley.gtfs;


public class RouteStatic {
	
	private String id;
	
	private String A_id;
	
	private String shortName;
			
	private String longName;
	
	private String desc;
	
	private int type;
	
	private String url;
	
	private String color;
	
	private String textColor;
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		if(!id.isEmpty()){
			id = id.replace("\"", "");
			this.id = id;
		}
	}
	
	public String getA_id(){
		return A_id;
	}
	
	public void setA_id(String A_id){
		this.A_id = A_id;
	}
	
	public String getShortName(){
		return shortName;
	}
	
	public void setShortName(String shortName){
		this.shortName = shortName;
	}
	
	public String getLongName(){
		return longName;
	}
	
	public void setLongName(String longName){
		this.longName = longName;
	}
	
	public String getDesc(){
		return desc;
	}
	
	public void setDesc(String desc){
		this.desc = desc;
	}
	
	public int getType(){
		return type;
	}
	
	public void setType(String type){
		if(type != null){
			this.type = Integer.parseInt(type);
		}else{
			this.type = -1;
			System.out.println("There is no Type!");
		}
	}
	
	public String getUrl(){
		return url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getColor(){
		return color;
	}
	
	public void setColor(String color){
		this.color = color;
	}
	
	public String getTextColor(){
		return textColor;
	}
	
	public void setTextColor(String textColor){
		this.textColor = textColor;
	}
	
}
