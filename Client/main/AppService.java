
package com.android.mk.driving.main;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.android.mk.driving.activity.MainActivity;
import com.android.mk.driving.config.ConfigBean;
import com.android.mk.driving.config.ConfigDBHelper;
import com.android.mk.driving.constant.Constant;
import com.android.mk.driving.context.ApplicationContext;
import com.android.mk.driving.route.RouteBean;
import com.android.mk.driving.route.RouteDBHelper;
import com.android.mk.driving.webservice.WebServiceParser;

public class AppService extends WakefulIntentService {
	private final String TAG = "android.mk.driving.main.AppService";
	private LocationManager locationManager = null;
	public AppService() {
		super("AppService");
	}
	
	@Override
  	protected void doWakefulWork(Intent intent) {
		
		Log.d(TAG,"in do wake ful work");
		
		/*String serverIPAddress = "";
		ConfigDBHelper configDBHelper = new ConfigDBHelper(this);
		ConfigBean ipConfigBean = configDBHelper.getConfigurationByPropertyType(Constant.PROPERTY_TYPE_SERVER_IP_ADDRERSS);
		if(ipConfigBean!=null)
			serverIPAddress = ipConfigBean.getValue();
		
		Log.d(TAG, "server ip address in service"+serverIPAddress);*/
		
		double longitude = -1;
		double latitude = -1;
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) 
        {
        	longitude = location.getLongitude();
        	latitude = location.getLatitude();
        	
        	String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s",location.getLongitude(), location.getLatitude());
        	Log.d(TAG,message);
        
        	
        	int locationId = ((ApplicationContext)this.getApplicationContext()).getLocationId();
        	RouteBean routeBean = new RouteBean();
        	
        	routeBean.setLocationId(locationId);
        	routeBean.setLatitude(latitude);
        	routeBean.setLongitude(longitude);
        	
        	RouteDBHelper routeDBHelper = new RouteDBHelper(this);
        	routeDBHelper.createRoute(routeBean);
        }
        
        
        
        
    
    	/*WebServiceParser webServiceParser = new WebServiceParser(this,"http://"+serverIPAddress+":8080/FriendFinderServer/rest/webService/");
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("longitude",	longitude);
		params.put("latitude", latitude);
		params.put("userProfileId", userProfileId+"");
		
		userProfileId = webServiceParser.updateLocationByUserId(params);
		
		Log.d(TAG,"user Profile Id - "+userProfileId);*/
		
	}
	
}