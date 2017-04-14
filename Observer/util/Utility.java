package com.android.mk.driving.safety.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import android.content.Context;
import android.util.Log;

import com.android.mk.driving.safety.config.ConfigBean;
import com.android.mk.driving.safety.config.ConfigDBHelper;
import com.android.mk.driving.safety.constant.Constant;
import com.android.mk.driving.safety.context.ApplicationContext;

public class Utility {
	private static String TAG = "com.android.client.Utility";
	public static String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Log.e(TAG, ex.toString());
	    }
	    return null;
	}
	
	public static void initializeConfig(Context mContext)
	{
		ConfigDBHelper configDBHelper = new ConfigDBHelper(mContext);
		ConfigBean configBean = configDBHelper.getConfigurationByPropertyType(Constant.PROPERTY_TYPE_SERVER_IP_ADDRERSS);
		((ApplicationContext)mContext.getApplicationContext()).setIpConfigBean(configBean);
	
		
	}
	
	public static String getDateAsString(Date date, String format)
	{
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(format); 
			return dateFormat.format(date);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public static Date getDateFromString(String dateString, String format)
	{	
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if ("K".equalsIgnoreCase(unit)) {
			dist = dist * 1.609344;
		} else if ("N".equalsIgnoreCase(unit)) {
			dist = dist * 0.8684;
		}
		System.out.println("distance ="+dist);
		return (dist);
	}
	
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
	
}
