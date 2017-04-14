package com.java.mk.driving.location;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class LocationDBHelper {
	private static final String TAG= "com.java.mk.driving.location.LocationDBHelper";
	private Logger logger = Logger.getLogger(TAG);
	private LocationDBAdapter locationDBAdapter;
	public LocationDBHelper() {
		locationDBAdapter = new LocationDBAdapter();
	}
	
	
	public ArrayList<LocationBean> fetchAllLocation(){
		locationDBAdapter.open();
		ResultSet resultSet = locationDBAdapter.fetchAll();
		ArrayList<LocationBean> locationBeanList = new ArrayList<LocationBean>();
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				while(resultSet.next()){
					locationBeanList.add(fetchLocationFromResultSet(resultSet));
				}
				resultSet.close();
			}
			catch (Exception e) {
				logger.error(e);
			}
			
		}
		locationDBAdapter.close();
		return locationBeanList; 
	}
	
	public String fetchLocationById(int id){
		locationDBAdapter.open();
		ResultSet resultSet = locationDBAdapter.fetchLocationById(id);
		String vel = "Car";
		
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				if(resultSet.next()){
					vel = resultSet.getString(LocationDBAdapter.COLUMN_VEHICLE);
				}
				resultSet.close();
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
			
		}
		locationDBAdapter.close();
		return vel; 
	}
		
	private LocationBean fetchLocationFromResultSet(ResultSet resultSet){ 
		LocationBean locationBean = new LocationBean();
		try
		{
			locationBean.setLocationId(resultSet.getInt(LocationDBAdapter.COLUMN_LOCATION_ID));
			locationBean.setFromLocation(resultSet.getString(LocationDBAdapter.COLUMN_FROM_LOCATION));
			locationBean.setToLocation(resultSet.getString(LocationDBAdapter.COLUMN_TO_LOCATION));
			locationBean.setVehicle(resultSet.getString(LocationDBAdapter.COLUMN_VEHICLE));
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		return locationBean;
	}
	
	
	public int createLocation(LocationBean locationBean){
		int result = 0;
		locationDBAdapter.open();
		result = locationDBAdapter.insert(locationBean);
		locationDBAdapter.close();
		return result;
	}
	
	public LocationBean getLastLocation()
	{
		locationDBAdapter.open();
		ResultSet resultSet = locationDBAdapter.fetchLastInsertedRow();
		LocationBean locationBean = new LocationBean();
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				if(resultSet.next()){
					//locationBean = (fetchLocationFromResultSet(resultSet));
				}
				resultSet.close();
			}
			catch (Exception e) {
				logger.error(e);
			}
			
		}
		locationDBAdapter.close();
		return locationBean;
	}
	
	
	
	
		
}
