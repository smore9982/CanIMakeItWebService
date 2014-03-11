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
		String fromId = request.getParameter("fromStationID");
		String toId = request.getParameter("toStationID");
		String direction = "0";
		System.out.println("Params = " + fromId + toId + direction);
		try{
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
			System.out.println("An error occured trying to get stops" + e.getMessage());
		}		
	}

}