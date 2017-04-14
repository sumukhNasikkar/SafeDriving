package com.android.mk.driving.location;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;



public class LocationDBHelper {

	private static final String TAG = "com.android.location.LocationDBHelper";
	private LocationDBAdapter locationDBAdapter;

	public LocationDBHelper(Context mContext) {
		locationDBAdapter= new LocationDBAdapter(mContext);
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
	
	private static LocationBean getLocationBeanFromCursor(Cursor locationCursor)
	{
		LocationBean tempBean = new LocationBean();
		
		tempBean.setLocationId(locationCursor.getInt(locationCursor.getColumnIndex(LocationDBAdapter.COLUMN_LOCATION_ID)));
		tempBean.setFromLocation(locationCursor.getString(locationCursor.getColumnIndex(LocationDBAdapter.COLUMN_FROM_LOCATION)));
		tempBean.setToLocation(locationCursor.getString(locationCursor.getColumnIndex(LocationDBAdapter.COLUMN_TO_LOCATION)));
		tempBean.setVehicle(locationCursor.getString(locationCursor.getColumnIndex(LocationDBAdapter.COLUMN_VEHICLE)));

		return tempBean;
	}
	
	public boolean deleteAllLocation(){
		locationDBAdapter.open();
		boolean result = locationDBAdapter.deleteAllLocations();
		locationDBAdapter.close();
		return result;
	}
	
	/*public boolean deleteLocationById(int locationId){
		locationDBAdapter.open();
		boolean result = locationDBAdapter.deleteLocationById(locationId);
		locationDBAdapter.close();
		return result;
	}*/
	
	public ArrayList<LocationBean> getAllLocation()
	{
		locationDBAdapter.open();
		Cursor locationCursor = locationDBAdapter.fetchAllLocations();
		ArrayList<LocationBean> locationBeanList = new ArrayList<LocationBean>();
		if(locationCursor != null)
		{
			locationCursor.moveToFirst();
			while(!locationCursor.isAfterLast())
			{	
				locationBeanList.add(getLocationBeanFromCursor(locationCursor));
				locationCursor.moveToNext();
			}
			locationCursor.close();
		}
		locationDBAdapter.close();
		return locationBeanList;
	}
	
	public LocationBean getLocationByLocationId(int locationId)
	{
		locationDBAdapter.open();
		Cursor locationCursor = locationDBAdapter.fetchLocationBylocationId(locationId);
		LocationBean locationBean = null;
		if(locationCursor != null)
		{
			locationBean = new LocationBean();
			locationCursor.moveToFirst();
			if(!locationCursor.isAfterLast())
			{	
				locationBean = getLocationBeanFromCursor(locationCursor);
				locationCursor.moveToNext();
			}
			locationCursor.close();
		}
		locationDBAdapter.close();
		return locationBean;
	}
	
	public int createLocation(LocationBean locationBean)
	{
		int result = 0;
		locationDBAdapter.open();
		result = locationDBAdapter.createLocation(locationBean);
		locationDBAdapter.close();
		return result;
	}
	
}
