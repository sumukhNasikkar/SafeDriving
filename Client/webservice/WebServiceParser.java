package com.android.mk.driving.webservice;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.mk.driving.location.LocationBean;

public class WebServiceParser {
	private final String TAG = "com.android.mk.railwayticket.webservice.WebServiceParser";
	private Context context;
	private WebServiceCommunictor wSCommunictor;
	public WebServiceParser(Context context , String webServiceURL) {
		this.context=context;
		wSCommunictor = new WebServiceCommunictor(context ,webServiceURL);


	}
	
	public int validateUser( Map<String, String> params){
		String response=wSCommunictor.invokeMethod("validateUser", params);
		int userId = -1;
		try{
			if(response!=null)
			{
				
				JSONObject jsonObject=new JSONObject(response);
				userId = jsonObject.getInt("userId");
			}
			//return true;
		}
		catch(Exception e){
			e.printStackTrace();
			//Toast.makeText(context, "" +  e.getMessage(), Toast.LENGTH_LONG).show();
		//	return false;
		}
		return userId;
	}
	
	public int uploadData( Map<String, String> params){
		String response=wSCommunictor.invokeMethod("uploadData", params);
		
		try{
			if(response!=null)
				{Log.d(TAG,response);
				JSONObject jsonObject = new JSONObject(response);
				if(jsonObject!=null)
				{
					return jsonObject.getInt("status");
				}
			}
		}
		catch(Exception e){
			//e.printStackTrace();
			//Toast.makeText(context, "" +  e.getMessage(), Toast.LENGTH_LONG).show();
		//	return false;
		}
		return 0;
	}
	
}
