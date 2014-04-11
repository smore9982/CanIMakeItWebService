package com.canimakeit.models;

public class AgencyModel {
	private String agency_id;
	private String agency_name;
	private String agency_url;
	private String agency_timezone;
	private String agency_phone;
	private String agency_shortname;
	
	
	public AgencyModel(String id,String name,String url,String timeZone,String phone){
		this.agency_id = id;
		this.agency_name = name;
		this.agency_url = url;
		this.agency_timezone = timeZone;
		this.agency_phone = phone;
	}

	public String getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(String agency_id) {
		this.agency_id = agency_id;
	}
	public String getAgency_name() {
		return agency_name;
	}
	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}
	public String getAgency_url() {
		return agency_url;
	}
	public void setAgency_url(String agency_url) {
		this.agency_url = agency_url;
	}
	public String getAgency_timezone() {
		return agency_timezone;
	}
	public void setAgency_timezone(String agency_timezone) {
		this.agency_timezone = agency_timezone;
	}
	public String getAgency_phone() {
		return agency_phone;
	}
	public void setAgency_phone(String agency_phone) {
		this.agency_phone = agency_phone;
	}

	public String getAgency_shortname() {
		return agency_shortname;
	}

	public void setAgency_shortname(String agency_shortname) {
		this.agency_shortname = agency_shortname;
	}
	
	
}
