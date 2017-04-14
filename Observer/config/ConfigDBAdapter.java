package com.android.mk.driving.safety.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.android.mk.driving.safety.main.SafeDrivingDBAdapter;

public class ConfigDBAdapter extends SafeDrivingDBAdapter{

	private static final String TAG = "com.android.mk.driving.config.ConfigDBAdapter"; 

	public ConfigDBAdapter(Context context) {
		super(context);
	}

	public ConfigDBAdapter(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public int createConfig(ConfigBean config) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(COLUMN_PROPERTY, config.getProperty());
		initialValues.put(COLUMN_VALUE, config.getValue());
		initialValues.put(COLUMN_PROPERTY_TYPE, config.getPropertyType());

		Log.d(TAG, "property : " + config.getProperty());
		Log.d(TAG, "value : "+ config.getValue());
		Log.d(TAG, "propertyType : " + config.getPropertyType());

		return (int) mySqliteDB.insert(CONFIG_TABLE_NAME, null, initialValues);
	}


	/**
	 * Delete the property with the given propname
	 * 
	 * @param property to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteConfigByProperty(String property) {
		return mySqliteDB.delete(CONFIG_TABLE_NAME, COLUMN_PROPERTY+ "='" + property + "'", null) > 0;
	}



	/**
	 * Return a Cursor over the list of all properties in the table
	 * 
	 * @return Cursor over all properties
	 */
	public Cursor fetchAllConfig() {

		return mySqliteDB.query(CONFIG_TABLE_NAME, new String[] {COLUMN_PROPERTY, COLUMN_VALUE, COLUMN_PROPERTY_TYPE}, null, null, null, null, null);

	}


	/**
	 * Return a Cursor positioned at the property  that matches the given property
	 * 
	 * @param property to retrieve
	 * @return Cursor positioned to matching property, if found
	 * @throws SQLException if property could not be found/retrieved
	 */
	public Cursor fetchConfigByProperty(String property){

		Cursor mCursor = mySqliteDB.query(true, CONFIG_TABLE_NAME, new String[] {COLUMN_PROPERTY, COLUMN_VALUE , COLUMN_PROPERTY_TYPE}, COLUMN_PROPERTY + "='" + property +"'", null,
						null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}


	/**
	 * Return a Cursor positioned at the property  that matches the given propertyType
	 * 
	 * @param propertyType to retrieve
	 * @return Cursor positioned to matching property, if found
	 * @throws SQLException if property could not be found/retrieved
	 */
	public Cursor fetchConfigByPropertyType(String propertyType) throws SQLException {

		Cursor mCursor = mySqliteDB.query(true, CONFIG_TABLE_NAME, new String[] {COLUMN_PROPERTY, COLUMN_VALUE , COLUMN_PROPERTY_TYPE}, COLUMN_PROPERTY_TYPE + "='" + propertyType +"'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * Update the property using the value provided. 
	 * If the property is not present in the DB, then it is created and the value is stored.
	 * @param configBean to update
	 */
	public boolean updateConfig(ConfigBean config) {
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
	}

}
