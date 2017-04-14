package com.android.mk.driving.main;
import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener
{

		private final String TAG = "com.android.friend.location.MyLocationListener";
		//private ArrayList <LocationBean> locationBeanList = null;
		double longitude;
		double latitude;
		Context myContext;
		
		public MyLocationListener(Context context)
		{
			myContext=context;
		}
		public void onLocationChanged(Location location) {
    		
			//Toast.makeText(myContext, "Now Location is " + location.getLongitude()+"  "+location.getLatitude(), Toast.LENGTH_SHORT).show();
    		String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",location.getLongitude(), location.getLatitude());
    		Log.d(TAG,"Now Location is " + location.getLongitude()+"  "+location.getLatitude());
    		longitude=location.getLongitude();
    		latitude=location.getLatitude();
    		
        }
    	public void onStatusChanged(String s, int i, Bundle b) 
    	{
    		//Toast.makeText(AddLocation.this, "Provider status changed",Toast.LENGTH_LONG).show();
    		
        }

        public void onProviderDisabled(String s)
        {
        	//Toast.makeText(AddLocation.this,"Provider disabled by the user. GPS turned off",Toast.LENGTH_LONG).show();
        }
        public void onProviderEnabled(String s) {

            //Toast.makeText(AddLocation.this,"Provider enabled by the user. GPS turned on",Toast.LENGTH_LONG).show();
        }
        
        public void checkForProfile()
        {
//        	Decimal
        	
        	//get data from DB location table
        	//compare
        	//get the profile id and get the profile from profile table
        	
        	
        

        }
    }

