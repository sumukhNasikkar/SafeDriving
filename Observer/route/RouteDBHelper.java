package com.android.mk.driving.safety.route;

import java.sql.ResultSet;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;



public class RouteDBHelper {

	private static final String TAG = "com.android.route.RouteDBHelper";
	private RouteDBAdapter routeDBAdapter;

	public RouteDBHelper(Context mContext) {
		routeDBAdapter= new RouteDBAdapter(mContext);
	}
	
	/*public boolean updateConfiguration(ConfigBean configBean){
		configDBAdapter.open();
		boolean result = configDBAdapter.updateConfig(configBean);
		configDBAdapter.close();
		if(result)
			return true;
		else 
			return false;
	}*/
	
	private RouteBean getRouteBeanFromCursor(Cursor routeCursor)
	{
		RouteBean tempBean = new RouteBean();
		
		tempBean.setRouteId(routeCursor.getInt(routeCursor.getColumnIndex(RouteDBAdapter.COLUMN_ROUTE_ID)));
		tempBean.setLocationId(routeCursor.getInt(routeCursor.getColumnIndex(RouteDBAdapter.COLUMN_LOCATION_ID)));
		tempBean.setLatitude(Double.parseDouble(routeCursor.getString(routeCursor.getColumnIndex(RouteDBAdapter.COLUMN_LATITUDE))));
		tempBean.setLongitude(Double.parseDouble(routeCursor.getString(routeCursor.getColumnIndex(RouteDBAdapter.COLUMN_LONGITUDE))));
		tempBean.setxAxis(Double.parseDouble(routeCursor.getString(routeCursor.getColumnIndex(RouteDBAdapter.COLUMN_X_AXIS))));
		tempBean.setyAxis(Double.parseDouble(routeCursor.getString(routeCursor.getColumnIndex(RouteDBAdapter.COLUMN_Y_AXIS))));
		tempBean.setzAxis(Double.parseDouble(routeCursor.getString(routeCursor.getColumnIndex(RouteDBAdapter.COLUMN_Z_AXIS))));

		Log.d(TAG, "route id "+tempBean.getRouteId());
		Log.d(TAG, "location id "+tempBean.getLocationId());
		Log.d(TAG, "latitude "+tempBean.getLatitude());
		Log.d(TAG, "x "+tempBean.getxAxis());
		Log.d(TAG, "y "+tempBean.getyAxis());
		Log.d(TAG, "z "+tempBean.getzAxis());
		
		return tempBean;
	}
	
	public boolean deleteAllRoute(){
		routeDBAdapter.open();
		boolean result = routeDBAdapter.deleteAllRoutes();
		routeDBAdapter.close();
		return result;
	}
	
	/*public boolean deleteRouteById(int routeId){
		routeDBAdapter.open();
		boolean result = routeDBAdapter.deleteRouteById(routeId);
		routeDBAdapter.close();
		return result;
	}*/
	
	public ArrayList<RouteBean> getAllRoute()
	{
		routeDBAdapter.open();
		Cursor routeCursor = routeDBAdapter.fetchAllRoutes();
		ArrayList<RouteBean> routeBeanList = null;
		if(routeCursor != null)
		{
			routeBeanList = new ArrayList<RouteBean>();
			routeCursor.moveToFirst();
			while(!routeCursor.isAfterLast())
			{	
				routeBeanList.add(getRouteBeanFromCursor(routeCursor));
				routeCursor.moveToNext();
			}
			routeCursor.close();
		}
		routeDBAdapter.close();
		return routeBeanList;
	}
	
	public ArrayList<RouteBean> getAllRouteByLocationId(int locationId)
	{
		routeDBAdapter.open();
		Cursor routeCursor = routeDBAdapter.fetchAllRoutesByLocationId(locationId);
		ArrayList<RouteBean> routeBeanList = null;
		if(routeCursor != null)
		{
			routeBeanList = new ArrayList<RouteBean>();
			routeCursor.moveToFirst();
			while(!routeCursor.isAfterLast())
			{	
				routeBeanList.add(getRouteBeanFromCursor(routeCursor));
				routeCursor.moveToNext();
			}
			routeCursor.close();
		}
		routeDBAdapter.close();
		return routeBeanList;
	}
	
	public int createRoute(RouteBean routeBean)
	{
		int result = 0;
		routeDBAdapter.open();
		result = routeDBAdapter.createRoute(routeBean);
		routeDBAdapter.close();
		return result;
	}
	
	public ArrayList<RouteBean> fetchDistortedUniqueRouteByLocationId(int locationId, String axis, int routeId)
	{
		routeDBAdapter.open();
		Cursor routeCursor = routeDBAdapter.fetchDisportedUniqueRouteByLocationId(locationId, axis, routeId);
		ArrayList<RouteBean> routeBeanList = new ArrayList<RouteBean>();
		if(routeCursor != null)
		{
			routeBeanList = new ArrayList<RouteBean>();
			routeCursor.moveToFirst();
			while(!routeCursor.isAfterLast())
			{	
				routeBeanList.add(getRouteBeanFromCursor(routeCursor));
				routeCursor.moveToNext();
			}
			routeCursor.close();
		}
		routeDBAdapter.close();
		return routeBeanList;
	}
	
	
}
