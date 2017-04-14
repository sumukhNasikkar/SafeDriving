package com.java.mk.driving.location;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.java.mk.driving.db.DrivingDBAdapter;

public class LocationDBAdapter extends DrivingDBAdapter {
	
	private static final  String TAG = "com.java.location.location.LocationDBAdapter" ;
	private static final Logger logger = Logger.getLogger(TAG);
	public LocationDBAdapter() {
		super();
	}
	
	public ResultSet fetchAll(){
		String query = "select * from "+LOCATION_TABLE_NAME;
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}
	
		
	public int insert(LocationBean locationBean)
	{
		String query = "insert into locationTable(userId,fromLocation,toLocation,vehicle) values('"+locationBean.getUserId()+"','"+locationBean.getFromLocation()+"','"+locationBean.getToLocation()+"','"+locationBean.getVehicle()+"')";
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			ResultSet rs = statement.getGeneratedKeys();
			int result = 0;
			if (rs.next()){
			    result =rs.getInt(1);
			}
			return result;
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return 0;
	}
	
	public ResultSet fetchLastInsertedRow(){
		String query = "SELECT * FROM locationTable order by locationId desc limit 1";
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}

	public ResultSet fetchLocationById(int id) {
		String query = "select * from locationTable where locationId= '" + id+"'";
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}
	
}
