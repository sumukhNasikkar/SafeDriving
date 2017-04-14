package com.java.mk.driving.user;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.java.mk.driving.db.DrivingDBAdapter;

public class UserDBAdapter extends DrivingDBAdapter {
	public static final String USER_TABLE_NAME = "userTable";
	public static final String COLUMN_USER_ID = "userId";
	public static final String COLUMN_USER_NAME = "userName";
	public static final String COLUMN_USER_ADDRESS = "userAddress";
	public static final String COLUMN_USER_PHONE_NUMBER = "userPhoneNumber";
	public static final String COLUMN_USER_EMAIL_ID = "userEmailId";
	public static final String COLUMN_USERNAME = "user_name";
	public static final String COLUMN_PASSWORD = "password";
	
	private static final  String TAG = "com.java.parking.user.UserDBAdapter" ;
	private static final Logger logger = Logger.getLogger(TAG);
	public UserDBAdapter() {
		super();
	}
	
	public ResultSet fetchAll(){
		String query = "select * from "+USER_TABLE_NAME;
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}
	
	public int update(UserBean userBean)
	{
		String query = "update userTable set userName = '"+userBean.getUserName()+"', userAddress = '"+userBean.getUserAddress()+"', userPhoneNumber = '"+userBean.getUserPhoneNumber()+"', userEmailId = '"+userBean.getUserEmailId()+"' where userId = '"+userBean.getUserId()+"'";
		try {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return 0;
	}
	
	public int insert(UserBean userBean)
	{
		String query = "insert into userTable(userName,userAddress,userPhoneNumber,userEmailId,user_name,password) values('"+userBean.getUserName()+"','"+userBean.getUserAddress()+"','"+userBean.getUserPhoneNumber()+"','"+userBean.getUserEmailId()+"','"+userBean.getUsername()+"','"+userBean.getPassword()+"')";
		try {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return 0;
	}
	
	public ResultSet fetchLastInsertedRow(){
		String query = "SELECT * FROM userTable order by userId desc limit 1";
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}
	
	public ResultSet getUserByUserId(int userId){
		String query = "SELECT * FROM userTable where userId = " + userId;
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}
	
	public ResultSet getUserByUsernameAndPassword(String username, String password){
		String query = "SELECT * FROM userTable where user_name = '" + username + "' and password = '"+password+"'";
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}
	
	
	public ResultSet getAllUserLocation(int userProfileId){
		String query = "SELECT * FROM userprofiletable where longitude != -1 and longitude != '' and latitude != -1 and latitude != '' and userProfileId != "+userProfileId+" and userProfileId in (select userId from selecteduserprofiletable where selectedUserId = "+userProfileId+")";
		//String query = "SELECT * FROM userprofiletable where longitude != -1 and longitude != '' and latitude != -1 and latitude != '' and userProfileId !="+userProfileId;
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			logger.error(TAG, e);
		}
		return resultSet;
	}
	
	public ResultSet getAllUpdatedProfile(int userProfileId){
		String query = "SELECT * FROM userprofiletable where userProfileId != " + userProfileId;
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
