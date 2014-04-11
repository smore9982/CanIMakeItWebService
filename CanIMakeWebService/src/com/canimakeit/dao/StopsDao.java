package com.canimakeit.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.canimakeit.models.StopModel;

public class StopsDao extends ParentDao {
	
	public StopsDao(DataSource datasource) {
		super(datasource);	
	}

	public List<StopModel> getStops(boolean onlyTransferStations) throws Exception{
		Connection connection =  getConnection();
		List<StopModel> listStopModels = new ArrayList<StopModel>();
		if(connection!=null){
			PreparedStatement getStmt = null;
			ResultSet resultSet = null;
			try{
				System.out.println("GOT A CONNECTION");
				String getStops = "select * from stops";	
				if(onlyTransferStations){
					getStops = getStops + " where stops.stop_transfer=1";
				}		
				getStmt = connection.prepareStatement(getStops);
				resultSet = getStmt.executeQuery();
				listStopModels = parseResultSet(resultSet);
				resultSet.close();
				connection.close();
			}catch(Exception e){
				try{
					if(getStmt !=null){
						getStmt.close();
					}
				}catch(Exception e1){};
				
				try{
					if (resultSet !=null ){
						resultSet.close();
					}
				}catch(Exception e2){};
			}finally{
				connection.close();
			}
			return listStopModels;			
		
		}else{
			return listStopModels;
		}
	}
	
	public StopModel getStopWithID(String stopId) throws Exception{
		Connection connection =  getConnection();
		StopModel model =null;
		if(connection!=null){
			PreparedStatement getStmt = null;
			ResultSet resultSet = null;
			try{
				System.out.println("GOT A CONNECTION");
				String getStops = "select * from stops where stop_id = ?";			
				getStmt = connection.prepareStatement(getStops);
				getStmt.setString(1, stopId);
				resultSet = getStmt.executeQuery();
				model = parseSingleStop(resultSet);			
				resultSet.close();
				connection.close();
			}catch(Exception e){
				try{
					if(getStmt !=null){
						getStmt.close();
					}
				}catch(Exception e1){};
				
				try{
					if (resultSet !=null ){
						resultSet.close();
					}
				}catch(Exception e2){};
			}finally{
				connection.close();
			}
			return model;			
		
		}else{
			return model;
		}
	}
	
	private StopModel parseSingleStop(ResultSet rs) throws  Exception{
		while(rs.next()){
			String id = rs.getString(1);
			String name = rs.getString(2);
			String lat = rs.getString(3);
			String lon = rs.getString(4);
			boolean isTransferPoint = rs.getBoolean(5);
			StopModel model = new StopModel(id,name,lat,lon);
			model.setStopTransferPoint(isTransferPoint);
			return model;
		}
		return null;
	}
	
	private List<StopModel>  parseResultSet(ResultSet rs) throws Exception{
		List<StopModel> models = new ArrayList<StopModel>();
		while(rs.next()){
			String id = rs.getString(1);
			String name = rs.getString(2);
			String lat = rs.getString(3);
			String lon = rs.getString(4);
			boolean isTransferPoint = rs.getBoolean(5);
			StopModel model = new StopModel(id,name,lat,lon);
			model.setStopTransferPoint(isTransferPoint);
			models.add(model);
		}
		return models;		
	}
}
