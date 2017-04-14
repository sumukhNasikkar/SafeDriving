package com.android.mk.driving.safety.webservice;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.mk.driving.safety.location.LocationBean;
import com.android.mk.driving.safety.route.RouteBean;

public class WebServiceParser {
	private final String TAG = "com.android.mk.railwayticket.webservice.WebServiceParser";
	private Context context;
	private WebServiceCommunictor wSCommunictor;
	public WebServiceParser(Context context , String webServiceURL) {
		this.context=context;
		wSCommunictor = new WebServiceCommunictor(context ,webServiceURL);


	}
	
	public int validateUser( Map<String, String> params){
		String response=wSCommunictor.invokeMethod("validateUser", params);
		int userId = -1;
		try{
			if(response!=null)
			{
				
				JSONObject jsonObject=new JSONObject(response);
				userId = jsonObject.getInt("userId");
			}
			//return true;
		}
		catch(Exception e){
			e.printStackTrace();
			//Toast.makeText(context, "" +  e.getMessage(), Toast.LENGTH_LONG).show();
		//	return false;
		}
		return userId;
	}
	
	public int uploadData( Map<String, String> params){
		String response=wSCommunictor.invokeMethod("uploadData", params);
		try{
			if(response!=null)
				{Log.d(TAG,response);
				JSONObject jsonObject = new JSONObject(response);
				if(jsonObject!=null)
				{
					return jsonObject.getInt("status");
				}
			}
		}
		catch(Exception e){
			//e.printStackTrace();
			//Toast.makeText(context, "" +  e.getMessage(), Toast.LENGTH_LONG).show();
		//	return false;
		}
		return 0;
	}
	
	public ArrayList<LocationBean> getAllLocation( Map<String, String> params){
		String response=wSCommunictor.invokeMethod("getAllLocation", params);
		ArrayList<LocationBean> locationBeanList = new ArrayList<LocationBean>();
		try{
			if(response!=null)
				{Log.d(TAG,response);
				JSONArray jsonArray = new JSONArray(response);
				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
					if(jsonObject != null)
					{
						LocationBean locationBean = new LocationBean();
						locationBean.setLocationId(jsonObject.getInt("locationId"));
						locationBean.setFromLocation(jsonObject.getString("fromLocation"));
						locationBean.setToLocation(jsonObject.getString("toLocation"));
						locationBean.setVehicle(jsonObject.getString("vehicle"));
						
						locationBeanList.add(locationBean);
					}
				}
			}
		}
		catch(Exception e){
			//e.printStackTrace();
			//Toast.makeText(context, "" +  e.getMessage(), Toast.LENGTH_LONG).show();
		//	return false;
		}
		return locationBeanList;
	}
	
	public ArrayList<RouteBean> getAllRoute( Map<String, String> params){
		String response=wSCommunictor.invokeMethod("getAllRoute", params);
		ArrayList<RouteBean> routeBeanList = new ArrayList<RouteBean>();
		try{
			if(response!=null)
				{Log.d(TAG,response);
				JSONArray jsonArray = new JSONArray(response);
				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
					if(jsonObject != null)
					{
						RouteBean routeBean = new RouteBean();
						routeBean.setRouteId(jsonObject.getInt("routeId"));
						routeBean.setLocationId(jsonObject.getInt("locationId"));
						routeBean.setLatitude(jsonObject.getDouble("latitude"));
						routeBean.setLongitude(jsonObject.getDouble("longitude"));
						routeBean.setxAxis(jsonObject.getDouble("xAxis"));
						routeBean.setyAxis(jsonObject.getDouble("yAxis"));
						routeBean.setzAxis(jsonObject.getDouble("zAxis"));
						
						routeBeanList.add(routeBean);
					}
				}
			}
		}
		catch(Exception e){
			//e.printStackTrace();
			//Toast.makeText(context, "" +  e.getMessage(), Toast.LENGTH_LONG).show();
		//	return false;
		}
		return routeBeanList;
	}
	
}
