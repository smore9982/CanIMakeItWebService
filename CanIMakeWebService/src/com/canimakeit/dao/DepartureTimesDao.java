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
	
	private final static String departureTimeQueryWithTransfer = "select fromStopName1,toStopName2,departureTime1,date1,stopTimeId1 "+
																	"FROM "+ 
																	"(select fromStop1.stop_name as fromStopName1,toStop1.stop_name as toStopName1,fromStopTime1.departure_time as departureTime1,toStopTime1.arrival_time as arrivalTime1, c1.date as date1,fromStopTime1.stop_time_id as stopTimeId1 "+
																	"from stop_times fromStopTime1, stop_times toStopTime1,trips trip1, calendar_dates c1, stops toStop1, stops fromStop1 "+
																	"where "+ 
																	"fromStopTime1.stop_id = ? and "+ 
																	"toStopTime1.stop_id = ? and "+
																	"fromStopTime1.stop_id = fromStop1.stop_id and "+ 
																	"toStopTime1.stop_id = toStop1.stop_id and "+
																	"fromStopTime1.trip_id = toStopTime1.trip_id and "+ 
																	"trip1.trip_id = fromStopTime1.trip_id and "+
																	"trip1.service_id = c1.service_id and "+
																	"trip1.trip_direction = ? and "+
																	"c1.date >= DATE_ADD(CURDATE(),INTERVAL - 1 DAY) "+
																	"order by c1.date,fromStopTime1.departure_time) as firstTrip, "+
																	"(select fromStop2.stop_name as fromStopName2,toStop2.stop_name as toStopName2,fromStopTime2.departure_time as departureTime2,toStopTime2.arrival_time as arrivalTime2, c2.date as date2,fromStopTime2.stop_time_id as stopTimeId2 "+
																	"from stop_times fromStopTime2, stop_times toStopTime2,trips trip2, calendar_dates c2, stops toStop2, stops fromStop2 "+
																	"where "+ 
																	"fromStopTime2.stop_id = ? and "+ 
																	"toStopTime2.stop_id= ? and "+
																	"fromStopTime2.stop_id = fromStop2.stop_id and "+ 
																	"toStopTime2.stop_id = toStop2.stop_id and "+
																	"fromStopTime2.trip_id = toStopTime2.trip_id and "+ 
																	"trip2.trip_id = fromStopTime2.trip_id and "+
																	"trip2.service_id = c2.service_id and "+
																	"trip2.trip_direction = ? and "+
																	"c2.date >= DATE_ADD(CURDATE(),INTERVAL - 1 DAY) "+
																	"order by c2.date,fromStopTime2.departure_time) as secondTrip "+
																	"WHERE "+ 
																	"date1 = date2 and "+
																	"departureTime2 - arrivalTime1 <=60*15 and "+
																	"departureTime2 - arrivalTime1 > 0; ";
	
	
	public DepartureTimesDao(DataSource datasource) {
		super(datasource);	
	}
	
	public List<DepartureTimesModel> getTime(String fromId,String toId, String transferId,String direction1, String direction2) throws Exception{
		Connection connection =  getConnection();
		List<DepartureTimesModel> departureTimeModels = new ArrayList<DepartureTimesModel>();
		if(connection!=null){
			PreparedStatement getStmt = null;
			ResultSet resultSet = null;
			try{				
				getStmt = connection.prepareStatement(departureTimeQueryWithTransfer);
				getStmt.setString(1, fromId);
				getStmt.setString(2, transferId);
				getStmt.setString(3, direction1);
				getStmt.setString(4, transferId);
				getStmt.setString(5, toId);
				getStmt.setString(6, direction2);
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
