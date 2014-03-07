package com.canimakeit.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.canimakeit.models.StopModel;

public class StopsDao extends ParentDao {
	
	public List<StopModel> getStops() throws Exception{
		Connection connection =  getConnection();
		List<StopModel> listStopModels = new ArrayList<StopModel>();
		if(connection!=null){
			try{
				System.out.println("GOT A CONNECTION");
				String getStops = "select * from stops";			
				PreparedStatement getStmt = connection.prepareStatement(getStops);
				ResultSet resultSet = getStmt.executeQuery();
				listStopModels = parseResultSet(resultSet);
				resultSet.close();
				connection.close();
			}catch(Exception e){
				
			}finally{
				connection.close();
			}
			return listStopModels;			
		}else{
			return listStopModels;
		}
	}
	
	private List<StopModel>  parseResultSet(ResultSet rs) throws Exception{
		List<StopModel> models = new ArrayList<StopModel>();
		while(rs.next()){
			String id = rs.getString(1);
			String name = rs.getString(2);
			String lat = rs.getString(3);
			String lon = rs.getString(4);
			StopModel model = new StopModel(id,name,lat,lon);
			models.add(model);
		}
		return models;		
	}
}
