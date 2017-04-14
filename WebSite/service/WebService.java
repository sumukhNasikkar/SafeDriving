package com.java.mk.driving.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.java.mk.driving.location.LocationBean;
import com.java.mk.driving.location.LocationDBHelper;
import com.java.mk.driving.route.RouteBean;
import com.java.mk.driving.route.RouteDBHelper;
import com.java.mk.driving.user.UserBean;
import com.java.mk.driving.user.UserDBHelper;

@Path("webService")
public class WebService {
	//private TakeAwayBean takeAwayBean = new TakeAwayBean();
    
    // The @Context annotation allows us to have certain contextual objects
    // injected into this class.
    // UriInfo object allows us to get URI information (no kidding).
    @Context
    UriInfo uriInfo;
 
    // Another "injected" object. This allows us to use the information that's
    // part of any incoming request.
    // We could, for example, get header information, or the requestor's address.
    @Context
    //Request request;
    //Response response;
    HttpServletRequest request;
    HttpServletResponse response;
    
    
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "Web service is ready!";
    }
    
    
    
    
    
    @POST
	@Path("/validateUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject validateUser(String data){

		System.out.println("in validate user");
		System.out.println(data);

		JSONObject jsonObject = null;
		try
		{
			jsonObject = new JSONObject(data);
			String username = jsonObject.getString("username");
			String password = jsonObject.getString("password");

			UserDBHelper userDBHelper = new UserDBHelper();
			UserBean userBean = userDBHelper.validateUser(username, password);
			if(userBean != null)
			{
				jsonObject = new JSONObject();
				jsonObject.put("userId", userBean.getUserId());
			}
			else
			{
				jsonObject = new JSONObject();
				jsonObject.put("userId", 0);
			}

		}
		catch (Exception e) {
			System.out.println(e);
			try
			{
				jsonObject = new JSONObject();
				jsonObject.put("userId", -1);
			}
			catch(Exception e1)
			{
				System.out.println(e1);
			}
		}

		return jsonObject;
	}
    
    
    @POST
    @Path("/validateStudent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject validateStudent(String data){
        
        System.out.println("in validate student");
        System.out.println(data);
        
        JSONObject jsonObject = null;
        try
		{
    		jsonObject = new JSONObject(data);
			String username = jsonObject.getString("username");
			String password = jsonObject.getString("password");
			
			/*StudentDBHelper studentDBHelper = new StudentDBHelper();
			StudentBean studentBean = studentDBHelper.validateStudent(username, password);
			if(studentBean != null)
			{
				jsonObject = new JSONObject();
		        jsonObject.put("studentId", studentBean.getStudentId());
			}
			else*/
			{
				jsonObject = new JSONObject();
		        jsonObject.put("userId", 0);
			}
    		
		}
		catch (Exception e) {
			System.out.println(e);
	        try
	        {
	        	jsonObject = new JSONObject();
		        jsonObject.put("userId", -1);
	        }
	        catch(Exception e1)
	        {
	        	System.out.println(e1);
	        }
		}
 
        return jsonObject;
    }
    @POST
    @Path("/uploadData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject uploadData(String data){
        
        System.out.println("in upload data");
        System.out.println(data);
        
        JSONObject jsonObject = null;
        try
		{
        	jsonObject = new JSONObject(data);
			
        	LocationBean locationBean = new LocationBean();
			locationBean.setToLocation(jsonObject.getString("toLocation"));
			locationBean.setFromLocation(jsonObject.getString("fromLocation"));
			locationBean.setVehicle(jsonObject.getString("vehicle"));
        	locationBean.setUserId(jsonObject.getInt("userId"));
        	
			LocationDBHelper locationDBHelper = new LocationDBHelper();
			int locationId = locationDBHelper.createLocation(locationBean);
			if(locationId > 0)
			{
				JSONArray jsonArray = new JSONArray(jsonObject.getString("routeData"));
				RouteDBHelper routeDBHelper = new  RouteDBHelper();
				for(int i=0;i<jsonArray.length();i++)
				{
					RouteBean routeBean = null;
					jsonObject = new JSONObject(jsonArray.getString(i));
					if(jsonObject!=null)
					{
						routeBean = new RouteBean();
						routeBean.setLocationId(locationId);
						routeBean.setLatitude(jsonObject.getDouble("latitude"));
						routeBean.setLongitude(jsonObject.getDouble("longitude"));
						routeBean.setxAxis(jsonObject.getDouble("xAxis"));
						routeBean.setyAxis(jsonObject.getDouble("yAxis"));
						routeBean.setzAxis(jsonObject.getDouble("zAxis"));
						
						routeDBHelper.createRoute(routeBean);
					}
				}
				jsonObject = new JSONObject();
		        jsonObject.put("status", 1);
			}
			else
			{
				jsonObject = new JSONObject();
		        jsonObject.put("status", 0);
			}
		}
		catch (Exception e) {
			System.out.println(e);
	        try
	        {
	        	jsonObject = new JSONObject();
		        jsonObject.put("userId", -1);
	        }
	        catch(Exception e1)
	        {
	        	System.out.println(e1);
	        }
		}
 
        return jsonObject;
    }
    
    
    @POST
	@Path("/getAllLocation")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONArray getAllLocation(String data){

		System.out.println("in get all location");
		System.out.println(data);

		JSONObject jsonObject = null;
		JSONArray jsonArray = new JSONArray();
		try
		{
			LocationDBHelper locationDBHelper = new LocationDBHelper();
			ArrayList<LocationBean> locationBeanList = locationDBHelper.fetchAllLocation();
			
			for(LocationBean locationBean : locationBeanList)
			{
				jsonObject  = new JSONObject();
				jsonObject.put("locationId", locationBean.getLocationId());
				jsonObject.put("userId", locationBean.getUserId());
				jsonObject.put("fromLocation", locationBean.getFromLocation());
				jsonObject.put("toLocation", locationBean.getToLocation());
				jsonObject.put("vehicle", locationBean.getVehicle());
				
				jsonArray.put(jsonObject);
				System.out.println(jsonArray);
			}

		}
		catch (Exception e) {
			System.out.println(e);
			try
			{
				jsonArray = new JSONArray();
			}
			catch(Exception e1)
			{
				System.out.println(e1);
			}
		}

		return jsonArray;
	}
    
    @POST
	@Path("/getAllRoute")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONArray getAllRouteData(String data){

		System.out.println("in get all route");
		System.out.println(data);
		int locationId = 0;
		
		JSONArray jsonArray = new JSONArray();
		
		try
		{
			
			JSONObject jsonObject = new JSONObject(data);
			if(jsonObject != null)
			{
				locationId = jsonObject.getInt("locationId");
			}
			
			RouteDBHelper routeDBHelper = new RouteDBHelper();;
			ArrayList<RouteBean> routeBeanList = routeDBHelper.fetchRouteByLocationId(locationId);
			
			for(RouteBean routeBean : routeBeanList)
			{
				jsonObject  = new JSONObject();
				jsonObject.put("locationId", routeBean.getLocationId());
				jsonObject.put("routeId", routeBean.getRouteId());
				jsonObject.put("latitude", routeBean.getLatitude());
				jsonObject.put("longitude", routeBean.getLongitude());
				jsonObject.put("xAxis", routeBean.getxAxis());
				jsonObject.put("yAxis", routeBean.getyAxis());
				jsonObject.put("zAxis", routeBean.getzAxis());
				
				jsonArray.put(jsonObject);
			}
			System.out.println(jsonArray);
		}
		catch (Exception e) {
			System.out.println(e);
			try
			{
				jsonArray = new JSONArray();
			}
			catch(Exception e1)
			{
				System.out.println(e1);
			}
		}

		return jsonArray;
	}
}
