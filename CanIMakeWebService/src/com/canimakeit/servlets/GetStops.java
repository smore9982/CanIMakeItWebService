package com.canimakeit.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import com.canimakeit.dao.StopsDao;
import com.canimakeit.models.StopModel;


public class GetStops extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetStops() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StopsDao stopDao = new StopsDao();
		try{
			List<StopModel> models = stopDao.getStops();
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<models.size();i++){
				StopModel model = models.get(i);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id", model.getStop_id());
				jsonObj.put("name", model.getStop_name());
				jsonObj.put("lat", model.getStop_lat());
				jsonObj.put("lon", model.getStop_lon());
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
