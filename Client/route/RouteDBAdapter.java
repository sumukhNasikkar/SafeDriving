package com.android.mk.driving.route;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.android.mk.driving.main.SafeDrivingDBAdapter;

public class RouteDBAdapter extends SafeDrivingDBAdapter{

	private static final String TAG = "com.android.mk.driving.route.RouteDBAdapter"; 

	public RouteDBAdapter(Context context) {
		super(context);
	}

	public RouteDBAdapter(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public int createRoute(RouteBean routeBean) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(COLUMN_LOCATION_ID, routeBean.getLocationId());
		initialValues.put(COLUMN_LATITUDE, routeBean.getLatitude());
		initialValues.put(COLUMN_LONGITUDE, routeBean.getLongitude());
		initialValues.put(COLUMN_X_AXIS, routeBean.getxAxis());
		initialValues.put(COLUMN_Y_AXIS, routeBean.getyAxis());
		initialValues.put(COLUMN_Z_AXIS, routeBean.getzAxis());

		Log.d(TAG, " : " + routeBean.getRouteId());
		Log.d(TAG, "location id : " + routeBean.getLocationId());
		Log.d(TAG, "longitude : " + routeBean.getLongitude());
		Log.d(TAG, "latitude : " + routeBean.getLatitude());
		Log.d(TAG, "xaxis : " + routeBean.getxAxis());
		Log.d(TAG, "yaxis : " + routeBean.getyAxis());
		Log.d(TAG, "zaxis : " + routeBean.getzAxis());

		return (int) mySqliteDB.insert(ROUTE_TABLE_NAME, null, initialValues);
	}


	/**
	 * Delete the property with the given propname
	 * 
	 * @param property to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteAllRoutes() {
		return mySqliteDB.delete(ROUTE_TABLE_NAME, null, null) > 0;
	}

	
	/**
	 * Return a Cursor over the list of all properties in the table
	 * 
	 * @return Cursor over all properties
	 */
	public Cursor fetchAllRoutes() {

		return mySqliteDB.query(ROUTE_TABLE_NAME, new String[] {COLUMN_ROUTE_ID, COLUMN_LOCATION_ID, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_X_AXIS, COLUMN_Y_AXIS, COLUMN_Z_AXIS}, null, null, null, null, null);
	}
	
	public Cursor fetchAllRoutesByLocationId(int locationId) {
		return mySqliteDB.query(ROUTE_TABLE_NAME, new String[] {COLUMN_ROUTE_ID, COLUMN_LOCATION_ID, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_X_AXIS, COLUMN_Y_AXIS, COLUMN_Z_AXIS}, "locationId = "+locationId, null, null, null, null);
	}
	
	/*public boolean deleteRouteById(int routeId) {
		return mySqliteDB.delete(ROUTE_TABLE_NAME, COLUMN_LOC_ID+ " = " + routeId, null) > 0;
	}*/
	
	/*public boolean updateConfig(ConfigBean config) {
		ContentValues args = new ContentValues();

		args.put(COLUMN_PROPERTY, config.getProperty());
		args.put(COLUMN_VALUE, config.getValue());
		args.put(COLUMN_PROPERTY_TYPE, config.getPropertyType());
		int numberofRowsaffected = mySqliteDB.update(CONFIG_TABLE_NAME, args, COLUMN_PROPERTY + "='" + config.getProperty() +"'", null);
		if(  numberofRowsaffected == 0) // no row was found so we need to create one 
		{
			mySqliteDB.insert(CONFIG_TABLE_NAME, null, args);
			return true;
		}
		else if(  numberofRowsaffected  != 1)  // more than/less than 1 row was found so we are in trouble :P   ( this already takes care of 0 by the else if)
		{
			return false;
		}

		return true;  // all is well  .. only 1 row was updated
	}*/

}
