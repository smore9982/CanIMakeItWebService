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

import com.canimakeit.dao.AgencyDao;
import com.canimakeit.models.AgencyModel;

public class GetLines extends HttpServlet{
	private static final long serialVersionUID = 1L;
	public GetLines() {
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
		AgencyDao agencyDao = new AgencyDao(dataSource);
		try{
			List<AgencyModel> models = agencyDao.getLines();
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<models.size();i++){
				AgencyModel model = models.get(i);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id", model.getAgency_id());
				jsonObj.put("name", model.getAgency_name());
				jsonArray.add(jsonObj);
				
			}	
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonArray.toString());
			out.flush();
		}catch(Exception e){
			System.out.println("An error occured trying to get stops" + e.getMessage());
			e.printStackTrace();
		}		
	}
}
