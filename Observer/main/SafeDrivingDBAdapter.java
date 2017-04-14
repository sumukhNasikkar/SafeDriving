package com.android.mk.driving.safety.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SafeDrivingDBAdapter  extends SQLiteOpenHelper{

	private static String DATABASE_NAME = "safeDrivingDB";
	
	public static String CONFIG_TABLE_NAME="configuration";
	public static String COLUMN_PROPERTY = "property";
	public static String COLUMN_VALUE = "value";
	public static String COLUMN_PROPERTY_TYPE = "propertyType";
	
	public static String LOCATION_TABLE_NAME="locationTable";
	public static String COLUMN_LOCATION_ID = "locationId";
	public static String COLUMN_FROM_LOCATION = "fromLocation";
	public static String COLUMN_TO_LOCATION = "toLocation";
	public static String COLUMN_VEHICLE = "vehicle";
	
	public static String ROUTE_TABLE_NAME = "routeTable";
	public static String COLUMN_ROUTE_ID = "routeId";
	//public static String COLUMN_LOCATION_ID = "locationId";
	public static String COLUMN_LONGITUDE = "longitude";
	public static String COLUMN_LATITUDE = "latitude";
	public static String COLUMN_X_AXIS = "xAxis";
	public static String COLUMN_Y_AXIS = "yAxis";
	public static String COLUMN_Z_AXIS = "zAxis";
	
	private static final String CREATE_CONFIG_TABLE_QUERY ="create table "+CONFIG_TABLE_NAME+" ("+COLUMN_PROPERTY+" text primary key not null, "+COLUMN_VALUE+" text not null, "+COLUMN_PROPERTY_TYPE+" text not null)";
	private static final String CREATE_MEETING_TABLE_QUERY ="create table "+ROUTE_TABLE_NAME+" ("+COLUMN_ROUTE_ID+" integer primary key not null,"+COLUMN_LOCATION_ID+" integer not null,"+COLUMN_LONGITUDE+" text not null, "+COLUMN_LATITUDE+" text not null, "+COLUMN_X_AXIS+" text not null, "+COLUMN_Y_AXIS+" text not null, "+COLUMN_Z_AXIS+" text not null)";
	private static final String CREATE_LOCATION_TABLE_QUERY ="create table "+LOCATION_TABLE_NAME+" ("+COLUMN_LOCATION_ID+" integer primary key not null, "+COLUMN_FROM_LOCATION+" text not null, "+COLUMN_TO_LOCATION+" text not null, "+COLUMN_VEHICLE+" text not null)";
	
	private static int DATABASE_VERSION = 2;
	
	protected SQLiteDatabase mySqliteDB;

	public SafeDrivingDBAdapter(Context context)
	{
		this(context,DATABASE_NAME,null,DATABASE_VERSION);	
	}
	
	public SafeDrivingDBAdapter(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_CONFIG_TABLE_QUERY);
		db.execSQL(CREATE_MEETING_TABLE_QUERY);
		db.execSQL(CREATE_LOCATION_TABLE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		db.execSQL(CREATE_CONFIG_TABLE_QUERY);
		db.execSQL(CREATE_MEETING_TABLE_QUERY);
		db.execSQL(CREATE_LOCATION_TABLE_QUERY);
	}
	
	public void open()
	{
		mySqliteDB = getWritableDatabase();	
	}
	

	public void close()
	{	
		mySqliteDB.close();
	}
}
