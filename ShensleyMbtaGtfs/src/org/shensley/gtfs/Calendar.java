package org.shensley.gtfs;

public class Calendar {
	private String serviceId;
	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private boolean sunday;
	private String startDate;
	private String endDate;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public boolean getMonday() {
		return monday;
	}

	public void setMonday(String monday) {
		monday = monday.replace("\"", "");
		this.monday = Boolean.parseBoolean(monday);
	}

	public boolean getTuesday() {
		return tuesday;
	}

	public void setTuesday(String tuesday) {
		tuesday = tuesday.replace("\"", "");
		this.tuesday = Boolean.parseBoolean(tuesday);
	}

	public boolean getWednesday() {
		return wednesday;
	}

	public void setWednesday(String wednesday) {
		wednesday = wednesday.replace("\"", "");
		this.wednesday = Boolean.parseBoolean(wednesday);
	}

	public boolean getThursday() {
		return thursday;
	}

	public void setThursday(String thursday) {
		thursday = thursday.replace("\"", "");
		this.thursday = Boolean.parseBoolean(thursday);
	}

	public boolean getFriday() {
		return friday;
	}

	public void setFriday(String friday) {
		friday = friday.replace("\"", "");
		this.friday = Boolean.parseBoolean(friday);
	}

	public boolean getSaturday() {
		return saturday;
	}

	public void setSaturday(String saturday) {
		saturday = saturday.replace("\"", "");
		this.saturday = Boolean.parseBoolean(saturday);
	}

	public boolean getSunday() {
		return sunday;
	}

	public void setSunday(String sunday) {
		sunday = sunday.replace("\"", "");
		this.sunday = Boolean.parseBoolean(sunday);
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
