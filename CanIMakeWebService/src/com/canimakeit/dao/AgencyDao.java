package com.canimakeit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.canimakeit.models.AgencyModel;

public class AgencyDao extends ParentDao {

	public AgencyDao(DataSource datasource) {
		super(datasource);
	}
	
	public List<AgencyModel>  getLines() throws SQLException{
		Connection connection =  getConnection();
		List<AgencyModel> models = new ArrayList<AgencyModel>();
		if(connection!=null){
			PreparedStatement getStmt = null;
			ResultSet resultSet = null;
			try{
				System.out.println("GOT A CONNECTION");
				String getStops = "select * from agency";		
				getStmt = connection.prepareStatement(getStops);
				resultSet = getStmt.executeQuery();
				models = parseResultSet(resultSet);
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
			return models;			
		
		}else{
			return models;
		}
	}
	
	public List<AgencyModel> parseResultSet(ResultSet rs) throws SQLException{
		List<AgencyModel> models = new ArrayList<AgencyModel>();
		while(rs.next()){
			String agencyId = rs.getString(1);
			String agencyName = rs.getString(2);
			String agencyUrl = rs.getString(3);
			String agencyPhone = rs.getString(4);			
			AgencyModel model = new AgencyModel(agencyId,agencyName,agencyUrl,"",agencyPhone);
			models.add(model);
		}
		return models;
		
	}
	
}
