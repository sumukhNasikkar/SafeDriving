package com.java.mk.driving.route;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.java.mk.driving.db.DrivingDBAdapter;

public class RouteDBAdapter extends DrivingDBAdapter {
	
	private static final  String TAG = "com.java.route.route.RouteDBAdapter" ;
	private static final Logger logger = Logger.getLogger(TAG);
	public RouteDBAdapter() {
		super();
	}
	
	public ResultSet fetchAll(){
		String query = "select * from "+ROUTE_TABLE_NAME;
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}
	
		
	public int insert(RouteBean routeBean)
	{
		String query = "insert into routeTable(locationId,longitude,latitude,xAxis,yAxis,zAxis) values('"+routeBean.getLocationId()+"','"+routeBean.getLongitude()+"','"+routeBean.getLatitude()+"','"+routeBean.getxAxis()+"','"+routeBean.getyAxis()+"','"+routeBean.getzAxis()+"')";
		try {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(query);			
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return 0;
	}
	
	public ResultSet fetchLastInsertedRow(){
		String query = "SELECT * FROM routeTable order by routeId desc limit 1";
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}
	
	public ResultSet fetchUniqueRouteByLocationId(int locationId){
		String query = "SELECT * FROM routetable  where locationId = "+locationId+" group by latitude,longitude";
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}
	
	public ResultSet fetchDisportedUniqueRouteByLocationId(int locationId, String axis){
		//String query = "SELECT * FROM routetable where locationId = "+locationId+" and "+ axis +" !=0 group by longitude, latitude";
		String query = "SELECT * FROM routetable where locationId = "+locationId+" and "+ axis +" !=0";
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}
	
	public ResultSet fetchRouteByLocationId(int locationId){
		String query = "SELECT * FROM routetable  where locationId = "+locationId;
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
