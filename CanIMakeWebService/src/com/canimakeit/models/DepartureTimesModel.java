package com.canimakeit.models;

import net.sf.json.JSONArray;

public class DepartureTimesModel {
	private String fromStopId;
	private String toStopId;
	private JSONArray departureTimes;
	private String serviceDate;
	
	public String getFromStopId() {
		return fromStopId;
	}
	public void setFromStopId(String fromStopId) {
		this.fromStopId = fromStopId;
	}
	public String getToStopId() {
		return toStopId;
	}
	public void setToStopId(String toStopId) {
		this.toStopId = toStopId;
	}
	public JSONArray getDepartureTimes() {
		return departureTimes;
	}
	public void setDepartureTimes(JSONArray departureTimes) {
		this.departureTimes = departureTimes;
	}
	public String getServiceDate() {
		return serviceDate;
	}
	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}
	
	public void addTime(String time){
		if(this.departureTimes == null){
			this.departureTimes = new JSONArray();
		}
		this.departureTimes.add(time);
	}
}
