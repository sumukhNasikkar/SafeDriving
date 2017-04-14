package com.android.mk.driving.safety.context;

import java.util.ArrayList;

import android.app.Application;

import com.android.mk.driving.safety.config.ConfigBean;
import com.android.mk.driving.safety.location.LocationBean;

public class ApplicationContext extends Application{
	
	private boolean isSessionEnd = false;
	private boolean isSessionStart = false;
	private ConfigBean ipConfigBean;
	private int locationId;
	private int routeId;
	private ArrayList<LocationBean> locationBeanList;
	private int userId;
	
	public ConfigBean getIpConfigBean() {
		return ipConfigBean;
	}
	public void setIpConfigBean(ConfigBean ipConfigBean) {
		this.ipConfigBean = ipConfigBean;
	}
	
	public boolean isSessionStart() {
		return isSessionStart;
	}
	public void setSessionStart(boolean isSessionStart) {
		this.isSessionStart = isSessionStart;
	}
	
	public boolean isSessionEnd() {
		return isSessionEnd;
	}
	public void setSessionEnd(boolean isSessionEnd) {
		this.isSessionEnd = isSessionEnd;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public ArrayList<LocationBean> getLocationBeanList() {
		return locationBeanList;
	}
	public void setLocationBeanList(ArrayList<LocationBean> locationBeanList) {
		this.locationBeanList = locationBeanList;
	}
	
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	
}
