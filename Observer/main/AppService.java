
package com.android.mk.driving.safety.main;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;

import com.android.mk.driving.safety.config.ConfigBean;
import com.android.mk.driving.safety.config.ConfigDBHelper;
import com.android.mk.driving.safety.constant.Constant;
import com.android.mk.driving.safety.context.ApplicationContext;
import com.android.mk.driving.safety.route.RouteBean;
import com.android.mk.driving.safety.route.RouteDBHelper;
import com.android.mk.driving.safety.util.Utility;

public class AppService extends Service implements TextToSpeech.OnInitListener, OnUtteranceCompletedListener 
{
	private final String TAG = "com.android.mk.driving.safety.main.AppSerice";
	private TextToSpeech mTts;
	private String spokenText;

	@Override
	public void onCreate() {
		mTts = new TextToSpeech(this, this);
		spokenText = "hello";
	}

	/*@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.US);
			if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
				mTts.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null);
			}
		}
	}*/
	
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = mTts.setLanguage(Locale.US);

			//tts.setPitch(5); // set pitch level

			//tts.setSpeechRate(2); // set speech speed rate
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			} else {
				//btnSpeak.setEnabled(true);
				speakOut("hello");
			}

		} else {
			Log.e("TTS", "Initilization Failed");
		}
	}
	

	@Override
	public void onUtteranceCompleted(String uttId) {
		stopSelf();
	}

	@Override
	public void onDestroy() {
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	private void speakOut(String text) {

		mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//speakOut("hello");
		//Log.d("TTS", "hello");
		
		
		double longitude = -1;
		double latitude = -1;
Log.d("SHRI", "HERE");
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if (location == null)
		{
			longitude = 73.8230021430821;
			latitude = 18.47534748488;
		}
		else
		{
			longitude = location.getLongitude();
			latitude = location.getLatitude();
		}

		{
			String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s",longitude, latitude);
			Log.d(TAG,message);


			int locationId = ((ApplicationContext)this.getApplicationContext()).getLocationId();
			int routeId = ((ApplicationContext)this.getApplicationContext()).getRouteId();
			Log.d(TAG,"route id " +routeId);
			//routeBean.setLocationId(locationId);
			
			RouteDBHelper routeDBHelper = new RouteDBHelper(this);
			ArrayList<RouteBean> routeBeanList = routeDBHelper.fetchDistortedUniqueRouteByLocationId(locationId, "yAxis", routeId);
			if(routeBeanList.size() > 0)
			{
				Log.d(TAG,"first route id :" +routeBeanList.get(0).getRouteId());
			}
			for(RouteBean routeBean : routeBeanList)
			{	
				double distance = Utility.distance(routeBean.getLatitude(),routeBean.getLongitude(), latitude, longitude, "K");
				Log.d(TAG, "distance "+distance);
				if(distance < .1 && routeBean.getyAxis() < -5)
				{
					Log.d(TAG,"select route id :"+routeBean.getRouteId());
					speakOut("attention, there is sudden breaking ahead.");
					((ApplicationContext)getApplication()).setRouteId(routeBean.getRouteId());
					break;
				}
				
				if(distance < .1 && routeBean.getyAxis() > 5)
				{
					Log.d(TAG,"select route id :"+routeBean.getRouteId());
					speakOut("attention, there is plain road ahead.");
					((ApplicationContext)getApplication()).setRouteId(routeBean.getRouteId());
					break;
				}
				
				// distance = Utility.distance(routeBean.getLatitude(),routeBean.getLongitude(), latitude, longitude, "K");
				//Log.d(TAG, "distance "+distance);
				if(distance < .1 && routeBean.getxAxis() < -10)
				{
					Log.d(TAG,"select route id :"+routeBean.getRouteId());
					speakOut("attention, there is a sudden right turn ahead.");
					((ApplicationContext)getApplication()).setRouteId(routeBean.getRouteId());
					break;
				}
				else if(distance < .1 && routeBean.getxAxis() > 10)
				{
					Log.d(TAG,"select route id :"+routeBean.getRouteId());
					speakOut("attention, there is a sudden left turn ahead.");
					((ApplicationContext)getApplication()).setRouteId(routeBean.getRouteId());
					break;
				}
				
				if(distance < .1 && routeBean.getzAxis() < -12)
				{
					Log.d(TAG,"select route id :"+routeBean.getRouteId());
					speakOut("attention, there is a pothole ahead.");
					((ApplicationContext)getApplication()).setRouteId(routeBean.getRouteId());
					break;
				}
				else if(distance < .1 && routeBean.getzAxis() > 12)
				{
					Log.d(TAG,"select route id :"+routeBean.getRouteId());
					speakOut("attention, there is a bump ahead.");
					((ApplicationContext)getApplication()).setRouteId(routeBean.getRouteId());
					break;
				}
			}
			
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
}
/*



extends IntentService implements TextToSpeech.OnInitListener {
	private final String TAG = "android.mk.driving.main.AppService";
	private LocationManager locationManager = null;
	private TextToSpeech tts;
	public AppService() {
		super("AppService");
	}



	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		tts = new TextToSpeech(this, this); 
		super.onCreate();
	}

 

	@Override
	protected void onHandleIntent(Intent intent) {
		tts = new TextToSpeech(this, this);
		Log.d(TAG,"in do wake ful work");

		String serverIPAddress = "";
		ConfigDBHelper configDBHelper = new ConfigDBHelper(this);
		ConfigBean ipConfigBean = configDBHelper.getConfigurationByPropertyType(Constant.PROPERTY_TYPE_SERVER_IP_ADDRERSS);
		if(ipConfigBean!=null)
			serverIPAddress = ipConfigBean.getValue();

		Log.d(TAG, "server ip address in service"+serverIPAddress);

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


			//int locationId = 0;//((ApplicationContext)this.getApplicationContext()).getLocationId();
			RouteBean routeBean = new RouteBean();

			//routeBean.setLocationId(locationId);
			routeBean.setLatitude(latitude);
			routeBean.setLongitude(longitude);

			RouteDBHelper routeDBHelper = new RouteDBHelper(this);
			//ArrayList<RouteBean> routeBeanList = routeDBHelper.getAllRoute();


			speakOut("hello ji");
		}





		WebServiceParser webServiceParser = new WebServiceParser(this,"http://"+serverIPAddress+":8080/FriendFinderServer/rest/webService/");
		Map<String, String> params = new HashMap<String, String>();

		params.put("longitude",	longitude);
		params.put("latitude", latitude);
		params.put("userProfileId", userProfileId+"");

		userProfileId = webServiceParser.updateLocationByUserId(params);

		Log.d(TAG,"user Profile Id - "+userProfileId);

	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			//tts.setPitch(5); // set pitch level

			//tts.setSpeechRate(2); // set speech speed rate

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			} else {
				//btnSpeak.setEnabled(true);
				speakOut("hello");
			}

		} else {
			Log.e("TTS", "Initilization Failed");
		}
	}

	private void speakOut(String text) {

		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

	}

	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}
}*/