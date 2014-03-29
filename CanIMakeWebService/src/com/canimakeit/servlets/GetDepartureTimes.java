package com.canimakeit.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.canimakeit.dao.DepartureTimesDao;
import com.canimakeit.dao.StopsDao;
import com.canimakeit.models.DepartureTimesModel;
import com.canimakeit.models.StopModel;


public class GetDepartureTimes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetDepartureTimes() {
        super();
    }
    
    private DataSource dataSource;     
    public void init() throws ServletException {
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            dataSource = (DataSource)envContext.lookup("jdbc/CanIMakeItDB");
 
             
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DepartureTimesDao timesDao = new DepartureTimesDao(dataSource);
		StopsDao stopDao = new StopsDao(dataSource);
		String fromId = request.getParameter("fromStationID");
		String toId = request.getParameter("toStationID");
		try{
			StopModel fromStop = stopDao.getStopWithID(fromId);
			StopModel toStop = stopDao.getStopWithID(toId);
			
			String direction = findBearing(fromStop,toStop);
					
			List<DepartureTimesModel> models = timesDao.getTimes(fromId, toId, direction);
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<models.size();i++){
				DepartureTimesModel model = models.get(i);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("departureStopId",model.getFromStopId());
				jsonObj.put("destinationStopId",model.getToStopId());
				jsonObj.put("departureDate",model.getServiceDate());
				jsonObj.put("departureTimes",model.getDepartureTimes());
				jsonArray.add(jsonObj);
				
			}	
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonArray.toString());
			out.flush();
		}catch(Exception e){
			System.out.println("An error occured trying to get trip times" + e.getMessage());
			e.printStackTrace();
		}		
	}

	private String findBearing(StopModel fromStop, StopModel toStop) {
		  double longitude1 = Double.parseDouble(fromStop.getStop_lon());
		  double longitude2 = Double.parseDouble(toStop.getStop_lon());
		  double latitude1 = Math.toRadians(Double.parseDouble(fromStop.getStop_lat()));
		  double latitude2 = Math.toRadians(Double.parseDouble(toStop.getStop_lat()));
		  double longDiff= Math.toRadians(longitude2-longitude1);
		  double y= Math.sin(longDiff)*Math.cos(latitude2);
		  double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
		  double degrees = (Math.toDegrees(Math.atan2(y, x))+360)%360;
		  
		  if(degrees > 0 && degrees < 180){
			  return "0";
		  }else{
			  return "1";
		  }
	}
}