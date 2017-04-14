package com.android.mk.driving.safety.activity;

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
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
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
import android.widget.Toast;

import com.android.mk.driving.safety.R;
import com.android.mk.driving.safety.config.ConfigBean;
import com.android.mk.driving.safety.constant.Constant;
import com.android.mk.driving.safety.context.ApplicationContext;
import com.android.mk.driving.safety.location.LocationBean;
import com.android.mk.driving.safety.location.LocationDBHelper;
import com.android.mk.driving.safety.main.AccelerometerManager;
import com.android.mk.driving.safety.main.MyLocationListener;
import com.android.mk.driving.safety.main.OnAlarmReceiver;
import com.android.mk.driving.safety.route.RouteBean;
import com.android.mk.driving.safety.route.RouteDBHelper;
import com.android.mk.driving.safety.webservice.WebServiceParser;

public class MainActivity extends Activity{

	private static Context CONTEXT;
	private static final int PERIOD=10000;  // 5 minutes
	private static final String TAG = "com.android.mk.driving.MainActivity";
	private static final int CONFIG_ID = Menu.FIRST;
	private String serverIPAddress;
	private Button showAllLocationButton;
	private Button stopButton;
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
		stopButton = (Button)findViewById(R.id.activity_main_stopButton);
		showAllLocationButton = (Button)findViewById(R.id.activity_main_showAllRouteButton);
		
		//stopButton.setVisibility(View.GONE);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	locationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        MINIMUM_TIME_BETWEEN_UPDATES,
        MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
        new MyLocationListener(this));

    	//Set Server IP Address
		ConfigBean ipConfigBean = ((ApplicationContext)this.getApplicationContext()).getIpConfigBean();
		if(ipConfigBean!=null)
		{
			serverIPAddress = ipConfigBean.getValue();
		}

		Toast.makeText(this, serverIPAddress, Toast.LENGTH_SHORT).show();

		stopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlarmManager mgr=(AlarmManager)MainActivity.this.getSystemService(Context.ALARM_SERVICE);
				Intent i=new Intent(MainActivity.this, OnAlarmReceiver.class);
				PendingIntent pi=PendingIntent.getBroadcast(MainActivity.this, 10255, i, PendingIntent.FLAG_UPDATE_CURRENT);
				mgr.cancel(pi);
				((ApplicationContext)MainActivity.this.getApplicationContext()).setSessionEnd(true);
				((ApplicationContext)MainActivity.this.getApplicationContext()).setLocationId(0);
				stopButton.setVisibility(View.GONE);
				showAllLocationButton.setVisibility(View.VISIBLE);
				
			}
		});

		/*uploadButton.setOnClickListener(new OnClickListener() {
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
				
				
			}
		});*/

		showAllLocationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				new AsyncTask<String, String, String>() {
					@Override
					protected String doInBackground(String... arg) {
						Map<String, String> params = new HashMap<String, String>();
						
						WebServiceParser webServiceParser = new WebServiceParser(MainActivity.this, "http://"+serverIPAddress+":8080/SafeDrivingServer/rest/webService/");
						ArrayList<LocationBean> locationBeanList = webServiceParser.getAllLocation(params);
						((ApplicationContext)MainActivity.this.getApplicationContext()).setLocationBeanList(locationBeanList);
						Log.d(TAG,locationBeanList.size()+"");
						return locationBeanList.size()+"";
						
					}
					
					@Override
					protected void onPostExecute(String val)
					{
						Toast.makeText(MainActivity.this, val, Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(MainActivity.this, ShowAllLocationActivity.class);
						startActivity(intent);
					}
					
				}.execute();
				
			}
		});

	}

	@Override
	protected void onResume() {
		Toast.makeText(this, "on update", Toast.LENGTH_SHORT).show();
		if(((ApplicationContext)this.getApplicationContext()).isSessionStart() && !((ApplicationContext)this.getApplicationContext()).isSessionEnd())
		{
			showAllLocationButton.setVisibility(View.GONE);
			stopButton.setVisibility(1);
		}
		else
		{
			showAllLocationButton.setVisibility(1);
			stopButton.setVisibility(View.GONE);
		}
		super.onResume();
	}

	public static Context getContext() {
		return CONTEXT;
	}

	/*private class AllTask extends AsyncTask<String, Void, String> {
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
				
				showAllLocationButton.setVisibility(1);
				startButton.setVisibility(View.GONE);
				stopButton.setVisibility(View.GONE);
				
				//textView.setText("");
			}
			else
				Toast.makeText(MainActivity.this, "Failed to upload data", Toast.LENGTH_SHORT).show();
			loadingDialog.dismiss();
		}
	}*/
}
