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

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class ShapeStatic {

	private String id;
	private int length;

	private ArrayList<String> sequenceByIndex = new ArrayList<String>();
	private Map<String, double[]> posBySequence = new HashMap<String, double[]>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getSequence(int index) {
		return sequenceByIndex.get(index);
	}

	public void setSequence(int index, String sequence) {
		this.sequenceByIndex.add(index, sequence);
	}

	public double getLat(int index) {
		return posBySequence.get(sequenceByIndex.get(index))[0];
	}

	public double getLon(int index) {
		return posBySequence.get(sequenceByIndex.get(index))[1];
	}

	public void setPos(int index, String lat, String lon) {
		lat = lat.replace("\"", "");
		lon = lon.replace("\"", "");
		if (this.posBySequence.get(index) == null) {
			if (lat != null && lon != null) {
				double[] pos = new double[2];
				pos[0] = Double.parseDouble(lat);
				pos[1] = Double.parseDouble(lon);
				if (sequenceByIndex.get(index) != null) {
					this.posBySequence.put(sequenceByIndex.get(index), pos);
				}
			}
		}
	}

}
