package com.canimakeit.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.canimakeit.dao.AdvisoryDao;
import com.canimakeit.models.AdvisoryModel;


public class GetAdvisories extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetAdvisories() {
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
		AdvisoryDao dao = new AdvisoryDao(dataSource);
		String stopIds = request.getParameter("stopIds");		
		
		try{
			List<String> routeList = getRouteIdsFromStopIds(stopIds,dao);  
			String requestType = request.getParameter("requestType");
			if(requestType.equals("count")){
				int count = dao.getAdvisoriesCountForRouteIds(routeList);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("advisoryCount", count);
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				out.print(jsonObj.toString());
				out.flush();
			}else{
				List<AdvisoryModel> models = dao.getAdvisoriesForRouteIds(routeList);
				JSONArray jsonArray = new JSONArray();
				for(int i=0;i<models.size();i++){
					AdvisoryModel model = models.get(i);
					JSONObject jsonObj = new JSONObject();					
					jsonObj.put("advisoryText",model.getAdvisoryText());
					jsonObj.put("advisoryAgencyId",model.getAgencyId());
					jsonObj.put("advisoryRouteId", model.getRouteId());
					jsonObj.put("advisoryCreatedDate",model.getCreatedDate());
					jsonObj.put("advisoryExpiredDate",model.getExpiredDate());
					jsonArray.add(jsonObj);					
				}	
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				out.print(jsonArray.toString());
				out.flush();
			}
		}
		catch(Exception e){
			System.out.println("An error occured trying to get trip times" + e.getMessage());
			e.printStackTrace();
		}
	}
	

	private List<String> getRouteIdsFromStopIds(String stopIds, AdvisoryDao dao) throws SQLException {
		String[] stopsArray = stopIds.split(",");
		List<String> stopList = new ArrayList<String>();
		for(int i=0;i<stopsArray.length;i++){
			stopList.add(stopsArray[i]);
		}		
		return dao.getRouteIds(stopList);
	}
}