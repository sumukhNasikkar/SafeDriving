package com.android.mk.driving.location;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.android.mk.driving.main.SafeDrivingDBAdapter;

public class LocationDBAdapter extends SafeDrivingDBAdapter{

	private static final String TAG = "com.android.mk.driving.location.LocationDBAdapter"; 

	public LocationDBAdapter(Context context) {
		super(context);
	}

	public LocationDBAdapter(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public int createLocation(LocationBean locationBean) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(COLUMN_FROM_LOCATION, locationBean.getFromLocation());
		initialValues.put(COLUMN_TO_LOCATION, locationBean.getToLocation());
		initialValues.put(COLUMN_VEHICLE, locationBean.getVehicle());

		Log.d(TAG, "location id : " + locationBean.getLocationId());

		return (int) mySqliteDB.insert(LOCATION_TABLE_NAME, null, initialValues);
	}


	/**
	 * Delete the property with the given propname
	 * 
	 * @param property to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteAllLocations() {
		return mySqliteDB.delete(LOCATION_TABLE_NAME, null, null) > 0;
	}

	
	/**
	 * Return a Cursor over the list of all properties in the table
	 * 
	 * @return Cursor over all properties
	 */
	public Cursor fetchAllLocations() {

		return mySqliteDB.query(LOCATION_TABLE_NAME, new String[] {COLUMN_LOCATION_ID, COLUMN_FROM_LOCATION, COLUMN_TO_LOCATION, COLUMN_VEHICLE}, null, null, null, null, null);
	}
	
	public Cursor fetchLocationBylocationId(int locationId) {

		return mySqliteDB.query(LOCATION_TABLE_NAME, new String[] {COLUMN_LOCATION_ID, COLUMN_FROM_LOCATION, COLUMN_TO_LOCATION, COLUMN_VEHICLE}, COLUMN_LOCATION_ID+" = "+locationId, null, null, null, null);
	}
	
	/*public boolean deleteLocationById(int locationId) {
		return mySqliteDB.delete(LOCATION_TABLE_NAME, COLUMN_LOC_ID+ " = " + locationId, null) > 0;
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
