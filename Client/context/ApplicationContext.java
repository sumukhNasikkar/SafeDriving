package com.android.mk.driving.context;

import android.app.Application;

import com.android.mk.driving.config.ConfigBean;
import com.android.mk.driving.location.LocationBean;

public class ApplicationContext extends Application{
	private boolean isSessionEnd = false;
	private boolean isSessionStart = false;
	private boolean isSession = false;
	private ConfigBean ipConfigBean;
	//private LocationBean locationBean;
	private int locationId;
	private float x;
	private float y;
	private float z;
	private int userId;
	
	public ConfigBean getIpConfigBean() {
		return ipConfigBean;
	}
	public void setIpConfigBean(ConfigBean ipConfigBean) {
		this.ipConfigBean = ipConfigBean;
	}
	
	public boolean isSession() {
		return isSession;
	}
	public void setSession(boolean isSession) {
		this.isSession = isSession;
	}
	
	/*public LocationBean getLocationBean() {
		return locationBean;
	}
	public void setLocationBean(LocationBean locationBean) {
		this.locationBean = locationBean;
	}*/
	
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
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
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
