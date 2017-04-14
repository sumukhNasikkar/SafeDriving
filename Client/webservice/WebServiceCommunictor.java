package com.android.mk.driving.webservice;

import java.util.Map;

import android.content.Context;
import android.util.Log;

public class WebServiceCommunictor {

	static final String TAG = "com.android.client.webservice.WebServiceCommunicator";
	public String webrespjson;
	public WebService webService;
	Context mContext;
	public WebServiceCommunictor(Context context , String webServiceURL) {
		mContext = context;
		webService = new WebService(webServiceURL , mContext);
	}
	
	
	public String invokeMethod(String methodName , Map<String, String> params)
	{
		try{
			
			webrespjson = webService.webInvoke(methodName , params);	          
		} 
		catch(Exception e){
	           Log.d(TAG,"Error in Web Service Call");
		}
	       
	    /*if(webrespjson!=null)
	    {
	    	return jsonParser(methodName, webrespjson , params);
	    }
	    else
	    {
	    	return false;
	    }*/
		return webrespjson;
	}
	/*public String invokeSendOrderInvoke(String methodName ,ArrayList <OrderDetailBean> orderList,int userId,String tableNumber)
	{
		try{
			
			webrespjson = webService.webSendOrderInvoke(methodName , orderList,userId,tableNumber);	          
		} 
		catch(Exception e){
	           Log.d("Error in Web Service Call: ","Error");    
		}
	       
	    if(webrespjson!=null)
	    {
	    	return jsonParser(methodName, webrespjson , params);
	    }
	    else
	    {
	    	return false;
	    }
		return webrespjson;
	}*/
	public String invokeGetMethod(String methodName , Map<String, String> params)
	{
		try{
			
			webrespjson = webService.webGet(methodName , params);	          
		} 
		catch(Exception e){
	           Log.d("Error in Web Service Call: ","Error");    
		}
	       
	    /*if(webrespjson!=null)
	    {
	    	return jsonParser(methodName, webrespjson , params);
	    }
	    else
	    {
	    	return false;
	    }*/
		return webrespjson;
	}
		
}
