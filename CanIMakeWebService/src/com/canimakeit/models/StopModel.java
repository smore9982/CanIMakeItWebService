package com.canimakeit.models;

public class StopModel {
	private String stop_id;
	private String stop_name;
	private String stop_lat;
	private String stop_lon;
	private boolean stopTransferPoint;
	
	public StopModel(String stopId, String stopName, String stopLat, String stopLon){
		this.stop_id = stopId;
		this.stop_name = stopName;
		this.stop_lat = stopLat;
		this.stop_lon = stopLon;
	}
	
	public String getStop_id() {
		return stop_id;
	}
	public void setStop_id(String stop_id) {
		this.stop_id = stop_id;
	}
	public String getStop_name() {
		return stop_name;
	}
	public void setStop_name(String stop_name) {
		this.stop_name = stop_name;
	}
	public String getStop_lat() {
		return stop_lat;
	}
	public void setStop_lat(String stop_lat) {
		this.stop_lat = stop_lat;
	}
	public String getStop_lon() {
		return stop_lon;
	}
	public void setStop_lon(String stop_lon) {
		this.stop_lon = stop_lon;
	}
	
	public String toString(){
		return "STOP: " + this.stop_id + " " + this.stop_name + " " + this.stop_lat + " " + this.stop_lon;
	}

	public boolean isStopTransferPoint() {
		return stopTransferPoint;
	}

	public void setStopTransferPoint(boolean stopTransferPoint) {
		this.stopTransferPoint = stopTransferPoint;
	}
}
