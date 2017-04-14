package com.android.mk.driving.activity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mk.driving.R;
import com.android.mk.driving.config.ConfigBean;
import com.android.mk.driving.constant.Constant;
import com.android.mk.driving.context.ApplicationContext;
import com.android.mk.driving.location.LocationBean;
import com.android.mk.driving.location.LocationDBHelper;
import com.android.mk.driving.main.AccelerometerListener;
import com.android.mk.driving.main.AccelerometerManager;
import com.android.mk.driving.main.MyLocationListener;
import com.android.mk.driving.main.OnAlarmReceiver;
import com.android.mk.driving.route.RouteBean;
import com.android.mk.driving.route.RouteDBHelper;
import com.android.mk.driving.webservice.WebServiceParser;

public class MainActivity extends Activity implements AccelerometerListener{

	private static Context CONTEXT;
	private static final int PERIOD=10000;  // 5 minutes
	private static final String TAG = "com.android.mk.driving.MainActivity";
	private static final int CONFIG_ID = Menu.FIRST;
	private String serverIPAddress;
	private Button setLocationButton;
	private Button startButton;
	private Button stopButton;
	private Button uploadButton;
	private TextView textView;
	private SensorManager mSensorManager; 
	private Sensor mAccelerometer; 
	private LocationManager locationManager;
	ProgressDialog loadingDialog;
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	private boolean saveFlag = false;
	//private AlarmManager mgr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CONTEXT = this;
		setContentView(R.layout.activity_main);
		
		
		textView = (TextView)findViewById(R.id.activity_main_textView2);
		textView.setText("");
		startButton = (Button)findViewById(R.id.activity_main_startButton);
		stopButton = (Button)findViewById(R.id.activity_main_stopButton);
		uploadButton = (Button)findViewById(R.id.activity_main_uploadButton);
		setLocationButton = (Button)findViewById(R.id.activity_main_setLocationButton);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	locationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        MINIMUM_TIME_BETWEEN_UPDATES,
        MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
        new MyLocationListener(this));
		
		/*if(((ApplicationContext)this.getApplicationContext()).isSession() && ((ApplicationContext)this.getApplicationContext()).isSessionEnd())
		{
			setLocationButton.setVisibility(View.GONE);
			startButton.setVisibility(View.GONE);
			stopButton.setVisibility(View.GONE);
			uploadButton.setVisibility(1);
		}
		else if(((ApplicationContext)this.getApplicationContext()).isSession() && ((ApplicationContext)this.getApplicationContext()).isSessionStart())
		{
			setLocationButton.setVisibility(View.GONE);
			startButton.setVisibility(View.GONE);
			stopButton.setVisibility(1);
			uploadButton.setVisibility(View.GONE);
		}
		else if(((ApplicationContext)this.getApplicationContext()).isSession() && !((ApplicationContext)this.getApplicationContext()).isSessionStart())
		{
			setLocationButton.setVisibility(View.GONE);
			startButton.setVisibility(1);
			stopButton.setVisibility(View.GONE);
			uploadButton.setVisibility(View.GONE);
		}
		else
		{
			setLocationButton.setVisibility(1);
			startButton.setVisibility(View.GONE);
			stopButton.setVisibility(View.GONE);
			uploadButton.setVisibility(View.GONE);
		}*/


		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlarmManager mgr=(AlarmManager)MainActivity.this.getSystemService(Context.ALARM_SERVICE);
				Intent i=new Intent(MainActivity.this, OnAlarmReceiver.class);
				PendingIntent pi=PendingIntent.getBroadcast(MainActivity.this, 10255, i, PendingIntent.FLAG_UPDATE_CURRENT);
				mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime(),PERIOD,pi);
				((ApplicationContext)MainActivity.this.getApplicationContext()).setSessionStart(true);
				startButton.setVisibility(View.GONE);
				stopButton.setVisibility(1);

				if (AccelerometerManager.isSupported()) {
					AccelerometerManager.startListening(MainActivity.this);
				}
				/*mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
				mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
				mSensorManager.registerListener(MainActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);*/
			}
		});

		stopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlarmManager mgr=(AlarmManager)MainActivity.this.getSystemService(Context.ALARM_SERVICE);
				Intent i=new Intent(MainActivity.this, OnAlarmReceiver.class);
				PendingIntent pi=PendingIntent.getBroadcast(MainActivity.this, 10255, i, PendingIntent.FLAG_UPDATE_CURRENT);
				mgr.cancel(pi);
				((ApplicationContext)MainActivity.this.getApplicationContext()).setSessionEnd(true);
				stopButton.setVisibility(View.GONE);
				uploadButton.setVisibility(1);

				if (AccelerometerManager.isListening()) {
					AccelerometerManager.stopListening();
				}
				textView.setText("");
			}
		});

		uploadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int pos=0;
				final String[] items = {"save"};
				AlertDialog.Builder helpBuilder = new AlertDialog.Builder(MainActivity.this);
				// helpBuilder.setIcon(R.drawable.popup_pro);
				helpBuilder.setTitle("Upload Data");

				helpBuilder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
	             @Override
	             public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
	                 if (isChecked) {
	                	 saveFlag = true;
	                	 Toast.makeText(MainActivity.this, saveFlag+"", Toast.LENGTH_SHORT).show();
	                     // If the user checked the item, add it to the selected items
	                     //s.add(indexSelected);
	                 } else {//if (seletedItems.contains(indexSelected)) {
	                	 saveFlag = false;
	                	 Toast.makeText(MainActivity.this, saveFlag+"", Toast.LENGTH_SHORT).show();
	                     // Else, if the item is already in the array, remove it
	                     //seletedItems.remove(Integer.valueOf(indexSelected));
	                 }
	             }
				});
				

				helpBuilder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						loadingDialog = ProgressDialog.show(MainActivity.this, "", "loading", true);
						loadingDialog.show();
						AllTask task = new AllTask();
						task.execute(new String[] {""});
					}
				});

				AlertDialog helpDialog = helpBuilder.create();
				helpDialog.show();
				
				
				
				
				/**/
				
			}
		});

		setLocationButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, SetLocationActivity.class);
				startActivity(intent);
			}
		});


		//Set Server IP Address
		ConfigBean ipConfigBean = ((ApplicationContext)this.getApplicationContext()).getIpConfigBean();
		if(ipConfigBean!=null)
		{
			serverIPAddress = ipConfigBean.getValue();
		}

		Toast.makeText(this, serverIPAddress, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		Toast.makeText(this, "on update", Toast.LENGTH_SHORT).show();
		if(((ApplicationContext)this.getApplicationContext()).isSession() && ((ApplicationContext)this.getApplicationContext()).isSessionEnd())
		{
			setLocationButton.setVisibility(View.GONE);
			startButton.setVisibility(View.GONE);
			stopButton.setVisibility(View.GONE);
			uploadButton.setVisibility(1);
		}
		else if(((ApplicationContext)this.getApplicationContext()).isSession() && ((ApplicationContext)this.getApplicationContext()).isSessionStart())
		{
			setLocationButton.setVisibility(View.GONE);
			startButton.setVisibility(View.GONE);
			stopButton.setVisibility(1);
			uploadButton.setVisibility(View.GONE);
		}
		else if(((ApplicationContext)this.getApplicationContext()).isSession() && !((ApplicationContext)this.getApplicationContext()).isSessionStart())
		{
			setLocationButton.setVisibility(View.GONE);
			startButton.setVisibility(1);
			stopButton.setVisibility(View.GONE);
			uploadButton.setVisibility(View.GONE);
		}
		else
		{
			setLocationButton.setVisibility(1);
			startButton.setVisibility(View.GONE);
			stopButton.setVisibility(View.GONE);
			uploadButton.setVisibility(View.GONE);
		}
		super.onResume();
	}

	public static Context getContext() {
		return CONTEXT;
	}

	/**
	 * onShake callback
	 */
	public void onShake(float force) {
		Toast.makeText(this, "Phone shaked : " + force, 1000).show();
	}

	/**
	 * onAccelerationChanged callback
	 */
	public void onAccelerationChanged(float x, float y, float z) {
		if(x != 0 || y != 0 || z != 0)
		{
			textView.setText("x:"+x+"y:"+y+"z:"+z);
			
			//get location bean from context
			int locationId = ((ApplicationContext)MainActivity.this.getApplicationContext()).getLocationId();
			
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			double latitude = 0;
			double longitude = 0;
			if (location != null) 
			{
				longitude = location.getLongitude();
				latitude = location.getLatitude();
			}
			String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s",longitude, latitude);
			//Toast.makeText(AddLocation.this, message,Toast.LENGTH_LONG).show();
			Log.d(TAG,message);
			Log.d(TAG,"x:"+x+"y:"+y+"z:"+z);
			
			RouteBean routeBean = new RouteBean();
			routeBean.setLocationId(locationId);
			routeBean.setLatitude(latitude);
			routeBean.setLongitude(longitude);
			routeBean.setxAxis(x);
			routeBean.setyAxis(y);
			routeBean.setzAxis(z);
			routeBean.setLocationId(locationId);
			
			RouteDBHelper routeDBHelper = new RouteDBHelper(this);
			routeDBHelper.createRoute(routeBean);
		}
	}
	
	private class AllTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			//String response = urls[0];

			int status = 0;
			LocationDBHelper locationDBHelper = new LocationDBHelper(MainActivity.this);
			int locationId = ((ApplicationContext)MainActivity.this.getApplicationContext()).getLocationId();
			int userId = ((ApplicationContext)MainActivity.this.getApplicationContext()).getUserId();
			Log.d(TAG, "location id :"+locationId);
			Log.d(TAG, "user id :"+userId);
			LocationBean locationBean = locationDBHelper.getLocationByLocationId(locationId);
			
			if(locationBean != null)
			{
				if(!saveFlag)
				{
					Map<String, String> params = new HashMap<String, String>();
					params.put("locationId", locationBean.getLocationId()+"");
					params.put("fromLocation", locationBean.getFromLocation());
					params.put("toLocation", locationBean.getToLocation());
					params.put("vehicle", locationBean.getVehicle());
					params.put("userId", userId+"");
					
					RouteDBHelper routeDBHelper = new RouteDBHelper(MainActivity.this);
					ArrayList<RouteBean> routeBeanList = routeDBHelper.getAllRouteByLocationId(locationId);
					Iterator<RouteBean> iterator = routeBeanList.iterator();
	
					JSONArray jsonArray = new  JSONArray();
					while (iterator.hasNext()) {
						RouteBean routeBean = (RouteBean) iterator.next();
						JSONObject jsonObject = new JSONObject();
						try {
							jsonObject.put("longitude", routeBean.getLongitude());
							jsonObject.put("latitude", routeBean.getLatitude());
							jsonObject.put("xAxis", routeBean.getxAxis());
							jsonObject.put("yAxis", routeBean.getyAxis());
							jsonObject.put("zAxis", routeBean.getzAxis());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						jsonArray.put(jsonObject);
					}
					params.put("routeData", jsonArray.toString());
					//WebServiceParser webServiceParser = new WebServiceParser(MainActivity.this, "http://"+serverIPAddress+":8080/SafeDrivingServer/rest/webService/");
					
					WebServiceParser webServiceParser = new WebServiceParser(MainActivity.this, "http://"+serverIPAddress+":8080/SafeDrivingServer/rest/webService/");
					status = webServiceParser.uploadData(params);
				}
				else
				{
					try
				    {
				        File dir = new File(Environment.getExternalStorageDirectory(), Constant.SAFE_DRIVING_DIRECTORY);
				        if (!dir.exists()) {
				            dir.mkdirs();
				        }
				        File file = new File(dir, Constant.UPLOAD_RESULT_FILE);
				        FileWriter writer = new FileWriter(file);
				        writer.append(locationBean.getLocationId()+","+locationBean.getFromLocation()+","+locationBean.getToLocation()+","+locationBean.getVehicle()+","+userId);
				        writer.flush();
				        
				        RouteDBHelper routeDBHelper = new RouteDBHelper(MainActivity.this);
						ArrayList<RouteBean> routeBeanList = routeDBHelper.getAllRouteByLocationId(locationId);
						Iterator<RouteBean> iterator = routeBeanList.iterator();
		
						while (iterator.hasNext()) {
							RouteBean routeBean = (RouteBean) iterator.next();
							writer.append("|");
							writer.append(routeBean.getLatitude()+","+routeBean.getLongitude()+","+routeBean.getxAxis()+","+routeBean.getyAxis()+","+routeBean.getzAxis());
							writer.flush();	
						}
				        
				        writer.close();
				        status = 1;
				    }
				    catch(IOException e)
				    {
				    	e.printStackTrace();
				    }
				}
			}
			
			return status+"";
		}

		@Override
		protected void onPostExecute(String result) {
			//Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();

			if(Integer.parseInt(result) > 0)
			{
				Toast.makeText(MainActivity.this, "Successfully upload data", Toast.LENGTH_SHORT).show();
				((ApplicationContext)MainActivity.this.getApplicationContext()).setSession(false);
				((ApplicationContext)MainActivity.this.getApplicationContext()).setSessionStart(false);
				((ApplicationContext)MainActivity.this.getApplicationContext()).setSessionEnd(false);
				
				setLocationButton.setVisibility(1);
				startButton.setVisibility(View.GONE);
				stopButton.setVisibility(View.GONE);
				uploadButton.setVisibility(View.GONE);
				
				//textView.setText("");
			}
			else
				Toast.makeText(MainActivity.this, "Failed to upload data", Toast.LENGTH_SHORT).show();
			loadingDialog.dismiss();
		}
	}

}
