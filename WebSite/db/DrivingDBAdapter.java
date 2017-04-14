package com.java.mk.driving.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


public class DrivingDBAdapter {

	public static String ROUTE_TABLE_NAME = "routetable";
	public static String COLUMN_ROUTE_ID = "routeId";
	public static String COLUMN_LONGITUDE = "longitude";
	public static String COLUMN_LATITUDE = "latitude";
	public static String COLUMN_X_AXIS = "xAxis";
	public static String COLUMN_Y_AXIS = "yAxis";
	public static String COLUMN_Z_AXIS = "zAxis";
	
	public static String LOCATION_TABLE_NAME="locationtable";
	public static String COLUMN_USER_ID = "userId";
	public static String COLUMN_LOCATION_ID = "locationId";
	public static String COLUMN_FROM_LOCATION = "fromLocation";
	public static String COLUMN_TO_LOCATION = "toLocation";
	public static String COLUMN_VEHICLE = "vehicle";
	
	private static final  String TAG = "com.java.mk.driving.db.DrivingDBAdapter" ;
	private static final Logger logger = Logger.getLogger(TAG);

	private DBConnectionPool dbConnectionPool;
	protected Connection connection;
	public DrivingDBAdapter()
	{
		dbConnectionPool= DBConnectionPool.getInstance();
	}

	public void open()
	{
		try {
			connection = (Connection)dbConnectionPool.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close()
	{
		dbConnectionPool.free(connection);
	}

}
