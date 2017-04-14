package com.java.mk.driving.route;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class RouteDBHelper {
	private static final String TAG= "com.java.mk.driving.route.RouteDBHelper";
	private Logger logger = Logger.getLogger(TAG);
	private RouteDBAdapter routeDBAdapter;
	public RouteDBHelper() {
		routeDBAdapter = new RouteDBAdapter();
	}
	
	public ArrayList<RouteBean> fetchAllRoute(){
		routeDBAdapter.open();
		ResultSet resultSet = routeDBAdapter.fetchAll();
		ArrayList<RouteBean> routeBeanList = new ArrayList<RouteBean>();
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				while(resultSet.next()){
					//routeBeanList.add(fetchRouteFromResultSet(resultSet));
				}
				resultSet.close();
			}
			catch (Exception e) {
				logger.error(e);
			}
			
		}
		routeDBAdapter.close();
		return routeBeanList; 
	}
		
	private RouteBean fetchRouteFromResultSet(ResultSet resultSet){ 
		RouteBean routeBean = new RouteBean();
		try
		{
			routeBean.setRouteId(resultSet.getInt(RouteDBAdapter.COLUMN_ROUTE_ID));
			routeBean.setLocationId(resultSet.getInt(RouteDBAdapter.COLUMN_LOCATION_ID));
			routeBean.setLongitude(Double.valueOf(resultSet.getString(RouteDBAdapter.COLUMN_LONGITUDE)));
			routeBean.setLatitude(Double.parseDouble(resultSet.getString(RouteDBAdapter.COLUMN_LATITUDE)));
			routeBean.setxAxis(Double.parseDouble(resultSet.getString(RouteDBAdapter.COLUMN_X_AXIS)));
			routeBean.setyAxis(Double.parseDouble(resultSet.getString(RouteDBAdapter.COLUMN_Y_AXIS)));
			routeBean.setzAxis(Double.parseDouble(resultSet.getString(RouteDBAdapter.COLUMN_Z_AXIS)));
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		return routeBean;
	}
	
	
	public int createRoute(RouteBean routeBean){
		int result = 0;
		routeDBAdapter.open();
		result = routeDBAdapter.insert(routeBean);
		routeDBAdapter.close();
		return result;
	}
	
	public RouteBean getLastRoute()
	{
		routeDBAdapter.open();
		ResultSet resultSet = routeDBAdapter.fetchLastInsertedRow();
		RouteBean routeBean = new RouteBean();
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				if(resultSet.next()){
					//routeBean = (fetchRouteFromResultSet(resultSet));
				}
				resultSet.close();
			}
			catch (Exception e) {
				logger.error(e);
			}
			
		}
		routeDBAdapter.close();
		return routeBean;
	}
	
	public ArrayList<RouteBean> fetchUniqueRouteByLocationId(int locationId)
	{
		routeDBAdapter.open();
		ResultSet resultSet = routeDBAdapter.fetchUniqueRouteByLocationId(locationId);
		ArrayList<RouteBean> routeBeanList = new ArrayList<RouteBean>();
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				while(resultSet.next()){
					routeBeanList.add(fetchRouteFromResultSet(resultSet));
				}
				resultSet.close();
			}
			catch (Exception e) {
				logger.error(e);
			}
		}
		routeDBAdapter.close();
		return routeBeanList;
	}
	
	public ArrayList<RouteBean> fetchDisportedUniqueRouteByLocationId(int locationId, String axis)
	{
		routeDBAdapter.open();
		ResultSet resultSet = routeDBAdapter.fetchDisportedUniqueRouteByLocationId(locationId, axis);
		ArrayList<RouteBean> routeBeanList = new ArrayList<RouteBean>();
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				while(resultSet.next()){
					routeBeanList.add(fetchRouteFromResultSet(resultSet));
				}
				resultSet.close();
			}
			catch (Exception e) {
				logger.error(e);
			}
		}
		routeDBAdapter.close();
		return routeBeanList;
	}
	
	public ArrayList<RouteBean> fetchRouteByLocationId(int locationId)
	{
		routeDBAdapter.open();
		ResultSet resultSet = routeDBAdapter.fetchRouteByLocationId(locationId);
		ArrayList<RouteBean> routeBeanList = new ArrayList<RouteBean>();
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				while(resultSet.next()){
					routeBeanList.add(fetchRouteFromResultSet(resultSet));
				}
				resultSet.close();
			}
			catch (Exception e) {
				logger.error(e);
			}
		}
		routeDBAdapter.close();
		return routeBeanList;
	}
		
}
