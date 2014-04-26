package com.canimakeit.models;

public class AdvisoryModel {
	private int advisoryId;
	private String routeId;
	private String agencyId;
	private String advisoryText;
	private String createdDate;
	private String expiredDate;
	public AdvisoryModel(int advisoryId, String routeId, String agencyId,String advisoryText, String createdDate, String expiredDate ){
		this.advisoryId = advisoryId;
		this.routeId = routeId;
		this.agencyId = agencyId;
		this.advisoryText = advisoryText;
		this.createdDate = createdDate;
		this.expiredDate = expiredDate;
	}
	
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public int getAdvisoryId() {
		return advisoryId;
	}
	public void setAdvisoryId(int advisoryId) {
		this.advisoryId = advisoryId;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public String getAdvisoryText() {
		return advisoryText;
	}
	public void setAdvisoryText(String advisoryText) {
		this.advisoryText = advisoryText;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}
	
	public String toString(){
		return this.advisoryText + ":" + this.advisoryId + ":" + this.routeId + ":" + this.createdDate + ":" + this.expiredDate;
		
	}
}
