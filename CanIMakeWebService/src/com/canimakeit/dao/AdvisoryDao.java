package com.canimakeit.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.canimakeit.models.AdvisoryModel;

public class AdvisoryDao extends ParentDao {
	private final static String getAdvisoryQueryPrefix = "select * from advisory_table where routeId in (";
	private final static String getAdvisoryCountPrefix = "select count(*) from advisory_table where routeId in (";
	private final static String getRouteIdsQuery = "select r.route_id from trips t, stop_times st, stops s, routes r where st.stop_id = s.stop_id and " +
																"st.trip_id = t.trip_id and " +
																"t.route_id = r.route_id and s.stop_id in (------) " +
																"group by r.route_id " +
																"order by s.stop_id";
	
	public AdvisoryDao(DataSource datasource) {
		super(datasource);
	}
	
	public List<AdvisoryModel>  getAdvisoriesForRouteIds(List<String> routeId) throws SQLException{
		Connection connection =  getConnection();
		List<AdvisoryModel> models = new ArrayList<AdvisoryModel>();
		if(connection!=null){
			PreparedStatement getStmt = null;
			ResultSet resultSet = null;
			try{
				String advisoryQuery = getAdvisoryQueryPrefix + "";
				for(int i=0;i<routeId.size();i++){
					advisoryQuery = advisoryQuery + "?";
					if(i != routeId.size() - 1){
						advisoryQuery = advisoryQuery + ",";
					}else{
						advisoryQuery = advisoryQuery + ")";
					}
				}
				getStmt = connection.prepareStatement(advisoryQuery);
				for(int i =0; i< routeId.size(); i++){
					System.out.println(routeId.get(i));
					getStmt.setString(i+1, routeId.get(i));
				}
				resultSet = getStmt.executeQuery();
				models = parseAdvisoryResultSet(resultSet);
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
			return models;			
		
		}else{
			return models;
		}
	}
	
	public int  getAdvisoriesCountForRouteIds(List<String> routeId) throws SQLException{
		Connection connection =  getConnection();
		int count = 0;
		if(connection!=null){
			PreparedStatement getStmt = null;
			ResultSet resultSet = null;
			try{
				String advisoryQuery = getAdvisoryCountPrefix + "";
				for(int i=0;i<routeId.size();i++){
					advisoryQuery = advisoryQuery + "?";
					if(i != routeId.size() - 1){
						advisoryQuery = advisoryQuery + ",";
					}else{
						advisoryQuery = advisoryQuery + ")";
					}
				}
				getStmt = connection.prepareStatement(advisoryQuery);
				for(int i =0; i< routeId.size(); i++){
					getStmt.setString(i+1, routeId.get(i));
				}
				resultSet = getStmt.executeQuery();
				while(resultSet.next()){
					count = resultSet.getInt(1);
				}
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
			return count;			
		
		}else{
			return count;
		}
	}
	
	public List<String>  getRouteIds(List<String> stopIds) throws SQLException{
		Connection connection =  getConnection();
		List<String> routes = new ArrayList<String>();
		if(connection!=null){
			PreparedStatement getStmt = null;
			ResultSet resultSet = null;
			try{
				String stopIdStr = "";
				for (int i=0;i<stopIds.size();i++){
					stopIdStr = stopIdStr + "\'" + stopIds.get(i) + "\'";
					if(i+1 < stopIds.size()){
						stopIdStr = stopIdStr + ",";
					}
				}
				
				String routeQuery = getRouteIdsQuery.replaceAll("------", stopIdStr);
				getStmt = connection.prepareStatement(routeQuery);
				resultSet = getStmt.executeQuery();
				routes = parseRouteResultSet(resultSet);
				
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
			return routes;			
		
		}else{
			return routes;
		}
	}

	private List<AdvisoryModel> parseAdvisoryResultSet(ResultSet rs) throws SQLException {
		List<AdvisoryModel> advisoryModels = new ArrayList<AdvisoryModel>();
		while(rs.next()){
			int advisoryId = rs.getInt(1);
			String routeId = rs.getString(2);
			String advisoryAgency = rs.getString(3);
			String advisoryText = rs.getString(4);
			Date createdDate = rs.getDate(5);
			Date expiredDate= rs.getDate(6);	
			String createdDateStr = "";
			String expiredDateStr = "";
			if(createdDate != null){
				createdDateStr = createdDate.toString();
			}
			if(expiredDate != null){
				expiredDateStr = expiredDate.toString();
			}
			
			AdvisoryModel model = new AdvisoryModel(advisoryId,routeId,advisoryAgency,advisoryText,createdDateStr,expiredDateStr);
			advisoryModels.add(model);
		}
		return advisoryModels;
	}
	
	private List<String> parseRouteResultSet(ResultSet rs) throws SQLException {
		List<String> routes = new ArrayList<String>();
		while(rs.next()){
			String routeString = rs.getString(1);
			routes.add(routeString);
		}
		return routes;
	}
}
