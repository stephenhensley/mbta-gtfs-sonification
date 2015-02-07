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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (!id.isEmpty()) {
			id = id.replace("\"", "");
			this.id = id;
		}
	}

	public String getA_id() {
		return A_id;
	}

	public void setA_id(String A_id) {
		this.A_id = A_id;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public void setType(String type) {
		if (type != null) {
			this.type = Integer.parseInt(type);
		} else {
			this.type = -1;
			System.out.println("There is no Type!");
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

}
