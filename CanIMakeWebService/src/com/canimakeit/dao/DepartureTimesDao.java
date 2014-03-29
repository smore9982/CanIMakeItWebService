package com.canimakeit.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.sf.json.JSONArray;

import com.canimakeit.models.DepartureTimesModel;
import com.canimakeit.models.StopModel;

public class DepartureTimesDao extends ParentDao {
	private final static String departureTimeQuery = "select fromStop.stop_id,toStop.stop_id,fromStop.departure_time,c.date " +
													 "from stop_times fromStop, stop_times toStop,trips t, calendar_dates c, stops toS, stops fromS " +
													 "where " +
													 "fromStop.stop_id =? and toStop.stop_id=? and " + 
													 "fromStop.stop_id = fromS.stop_id and toStop.stop_id = toS.stop_id and " +
													 "fromStop.trip_id = toStop.trip_id and " + 
													 "t.trip_id = fromStop.trip_id and " +
													 "t.service_id = c.service_id and " +
													 "t.trip_direction = ? and " +
													 "c.date >= DATE_ADD(CURDATE(),INTERVAL -1 DAY)" +
													 "order by c.date,fromStop.departure_time";
	
	
	public DepartureTimesDao(DataSource datasource) {
		super(datasource);	
	}

	public List<DepartureTimesModel> getTimes(String fromId, String toId, String direction) throws Exception{
		Connection connection =  getConnection();
		List<DepartureTimesModel> departureTimeModels = new ArrayList<DepartureTimesModel>();
		if(connection!=null){
			PreparedStatement getStmt = null;
			ResultSet resultSet = null;
			try{				
				getStmt = connection.prepareStatement(departureTimeQuery);
				getStmt.setString(1, fromId);
				getStmt.setString(2, toId);
				getStmt.setString(3, direction);
				resultSet = getStmt.executeQuery();
				departureTimeModels = parseResultSet(resultSet);
				resultSet.close();
				connection.close();
			}catch(Exception e){
				e.printStackTrace();
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
			return departureTimeModels;			
		
		}else{
			return departureTimeModels;
		}
	}
	
	private List<DepartureTimesModel>  parseResultSet(ResultSet rs) throws Exception{
		List<DepartureTimesModel> models = new ArrayList<DepartureTimesModel>();
		Date prevDepartureDate = null;
		DepartureTimesModel currentDeparture = new DepartureTimesModel();
		while(rs.next()){
			String fromStationId = rs.getString(1);
			String toStationId = rs.getString(2);
			Time departureTime = rs.getTime(3);
			Date departureDate = rs.getDate(4);
			if(prevDepartureDate == null){
				prevDepartureDate = departureDate;
			}			
			if(prevDepartureDate.equals(departureDate)){				
				currentDeparture.setFromStopId(fromStationId);
				currentDeparture.setToStopId(toStationId);
				currentDeparture.setServiceDate(departureDate.toString());
				currentDeparture.addTime(departureTime.toString());			
			}else{
				models.add(currentDeparture);
				prevDepartureDate = departureDate;
				currentDeparture = new DepartureTimesModel();
				currentDeparture.setFromStopId(fromStationId);
				currentDeparture.setToStopId(toStationId);
				currentDeparture.setServiceDate(departureDate.toString());
				currentDeparture.addTime(departureTime.toString());			
			}
			if(rs.isLast()){
				models.add(currentDeparture);
			}
		}			
		return models;		
	}
}
