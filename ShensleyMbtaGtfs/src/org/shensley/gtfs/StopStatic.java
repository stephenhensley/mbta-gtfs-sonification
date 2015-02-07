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

	public void setId(String id) {
		id = id.replace("\"", "");
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(String lat) {
		lat = lat.replace("\"", "");
		if (!lat.isEmpty() && lat != null) {
			this.lat = Double.parseDouble(lat);
		}
	}

	public double getLon() {
		return lon;
	}

	public void setLon(String lon) {
		lon = lon.replace("\"", "");
		if (!lon.isEmpty() && lon != null) {
			this.lon = Double.parseDouble(lon);
		}
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(String type) {
		type = type.replace("\"", "");
		if (!type.isEmpty()) {
			this.type = Integer.parseInt(type);
		}
	}

	public String getParentStation() {
		return parentStation;
	}

	public void setParentStation(String parentStation) {
		this.parentStation = parentStation;
	}

}
