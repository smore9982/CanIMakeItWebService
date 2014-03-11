package com.canimakeit.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

public class ParentDao {
	DataSource datasource;
	public ParentDao(DataSource datasource){
		this.datasource = datasource;
	}
	
	protected Connection getConnection(){
		try{			
			return this.datasource.getConnection();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
