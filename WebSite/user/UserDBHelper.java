package com.java.mk.driving.user;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class UserDBHelper {
	private static final String TAG= "com.java.parking.user.UserDBHelper";
	private Logger logger = Logger.getLogger(TAG);
	private UserDBAdapter userDBAdapter;
	public UserDBHelper() {
		userDBAdapter = new UserDBAdapter();
	}
	
	public ArrayList<UserBean> fetchAllUser(){
		userDBAdapter.open();
		ResultSet resultSet = userDBAdapter.fetchAll();
		ArrayList<UserBean> userBeanList = new ArrayList<UserBean>();
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				while(resultSet.next()){
					userBeanList.add(fetchUserFromResultSet(resultSet));
				}
				resultSet.close();
			}
			catch (Exception e) {
				logger.error(e);
			}
			
		}
		userDBAdapter.close();
		return userBeanList; 
	}
	
	private UserBean fetchUserFromResultSet(ResultSet resultSet){ 
		UserBean userBean=new UserBean();
		try
		{
			userBean.setUserId(resultSet.getInt(UserDBAdapter.COLUMN_USER_ID));
			userBean.setUserName(resultSet.getString(UserDBAdapter.COLUMN_USER_NAME));
			userBean.setUserAddress(resultSet.getString(UserDBAdapter.COLUMN_USER_ADDRESS));
			userBean.setUserPhoneNumber(resultSet.getString(UserDBAdapter.COLUMN_USER_PHONE_NUMBER));
			userBean.setUserEmailId(resultSet.getString(UserDBAdapter.COLUMN_USER_EMAIL_ID));
			userBean.setUsername(resultSet.getString(UserDBAdapter.COLUMN_USERNAME));
			userBean.setPassword(resultSet.getString(UserDBAdapter.COLUMN_PASSWORD));
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		return userBean;
	}
	
	public int updateUser(UserBean userBean){
		int result = 0;
		userDBAdapter.open();
		result = userDBAdapter.update(userBean);
		userDBAdapter.close();
		return result;
	}
	
	public int createUser(UserBean userBean){
		int result = 0;
		userDBAdapter.open();
		result = userDBAdapter.insert(userBean);
		userDBAdapter.close();
		return result;
	}
	
	public UserBean getLastUserProfile()
	{
		userDBAdapter.open();
		ResultSet resultSet = userDBAdapter.fetchLastInsertedRow();
		UserBean userProfileBean = new UserBean();
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				if(resultSet.next()){
					userProfileBean = (fetchUserFromResultSet(resultSet));
				}
				resultSet.close();
			}
			catch (Exception e) {
				logger.error(e);
			}
			
		}
		userDBAdapter.close();
		return userProfileBean;
	}
	
	public ArrayList<UserBean> getUserByUserId(int userId){
		userDBAdapter.open();
		ResultSet resultSet = userDBAdapter.getUserByUserId(userId);
		ArrayList<UserBean> userProfileBeanList = new ArrayList<UserBean>();
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				while(resultSet.next()){
					userProfileBeanList.add(fetchUserFromResultSet(resultSet));
				}
				resultSet.close();
			}
			catch (Exception e) {
				logger.error(e);
			}
		}
		userDBAdapter.close();
		return userProfileBeanList; 
	}
	
	public UserBean validateUser(String username, String password){
		userDBAdapter.open();
		ResultSet resultSet = userDBAdapter.getUserByUsernameAndPassword(username, password);
		UserBean userBean = null;
		if(resultSet!=null){
			try
			{
				resultSet.beforeFirst();
				while(resultSet.next()){
					userBean = fetchUserFromResultSet(resultSet);
				}
				resultSet.close();
			}
			catch (Exception e) {
				logger.error(e);
			}
		}
		userDBAdapter.close();
		return userBean; 
	}
		
}
