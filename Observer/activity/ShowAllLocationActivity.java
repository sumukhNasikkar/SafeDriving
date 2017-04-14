package com.android.mk.driving.safety.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.mk.driving.safety.R;
import com.android.mk.driving.safety.config.ConfigBean;
import com.android.mk.driving.safety.context.ApplicationContext;
import com.android.mk.driving.safety.location.LocationBean;
import com.android.mk.driving.safety.main.OnAlarmReceiver;
import com.android.mk.driving.safety.route.RouteBean;
import com.android.mk.driving.safety.route.RouteDBHelper;
import com.android.mk.driving.safety.webservice.WebServiceParser;

public class ShowAllLocationActivity extends Activity {

	private final String TAG = "com.android.mk.driving.activity.SetLocationActivity";
	private static final int PERIOD=10000;  // 5 minutes
	private ImageButton backImageButton ;
	private ListView routeListView;
	private ArrayList<LocationBean> locationBeanList;
	private String serverIPAddress = "";
	ProgressDialog progressDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.show_all_route_layout);

		ConfigBean ipConfigBean = ((ApplicationContext)this.getApplicationContext()).getIpConfigBean();
		if(ipConfigBean!=null)
		{
			serverIPAddress = ipConfigBean.getValue();
		}
		Toast.makeText(this, serverIPAddress, Toast.LENGTH_SHORT).show();

		backImageButton = (ImageButton)findViewById(R.id.show_all_route_layout_backImageButton);
		routeListView = (ListView)findViewById(R.id.show_all_route_layout_allRouteListView);

		locationBeanList = ((ApplicationContext)this.getApplicationContext()).getLocationBeanList();
		AllLocationListAdapter allLocationListAdapter = new AllLocationListAdapter(this, locationBeanList);
		routeListView.setAdapter(allLocationListAdapter);
		routeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				Toast.makeText(ShowAllLocationActivity.this, locationBeanList.get(arg2).getLocationId()+"", Toast.LENGTH_SHORT).show();
				AlertDialog.Builder helpBuilder = new AlertDialog.Builder(ShowAllLocationActivity.this);
				helpBuilder.setTitle("Route Data");
				helpBuilder.setMessage("Do you want to download all route data from "+locationBeanList.get(arg2).getFromLocation()+" to "+locationBeanList.get(arg2).getToLocation()+"?");
				helpBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {	
					public void onClick(DialogInterface dialog, int which) {
						((ApplicationContext)ShowAllLocationActivity.this.getApplicationContext()).setLocationId(locationBeanList.get(arg2).getLocationId());
						progressDialog = ProgressDialog.show(ShowAllLocationActivity.this, "", "loading", true);
						progressDialog.show();
						new AsyncTask<String, String, String>() {
							@Override
							protected String doInBackground(String... arg) {
								Map<String, String> params = new HashMap<String, String>();
								params.put("locationId", locationBeanList.get(arg2).getLocationId()+"");
								WebServiceParser webServiceParser = new WebServiceParser(ShowAllLocationActivity.this, "http://"+serverIPAddress+":8080/SafeDrivingServer/rest/webService/");
								ArrayList<RouteBean> routeBeanList = webServiceParser.getAllRoute(params);
								Log.d(TAG,routeBeanList.size()+"");


								RouteDBHelper routeDBHelper = new RouteDBHelper(ShowAllLocationActivity.this);
								routeDBHelper.deleteAllRoute();
								for(RouteBean routeBean : routeBeanList)
								{
									int routeId = routeDBHelper.createRoute(routeBean);
									Log.d(TAG, "inserted id = "+routeId);
								}
								
								return routeBeanList.size()+"";
							}

							@Override
							protected void onPostExecute(String val)
							{
								Toast.makeText(ShowAllLocationActivity.this, val, Toast.LENGTH_SHORT).show();
								progressDialog.dismiss();
								if(Integer.parseInt(val) > 0)
								{
									
									AlarmManager mgr=(AlarmManager)ShowAllLocationActivity.this.getSystemService(Context.ALARM_SERVICE);
									Intent i=new Intent(ShowAllLocationActivity.this, OnAlarmReceiver.class);
									PendingIntent pi=PendingIntent.getBroadcast(ShowAllLocationActivity.this, 10255, i, PendingIntent.FLAG_UPDATE_CURRENT);
									mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime(),PERIOD,pi);
									
									Toast.makeText(ShowAllLocationActivity.this, "download complete successfully", Toast.LENGTH_SHORT).show();

									((ApplicationContext)ShowAllLocationActivity.this.getApplicationContext()).setSessionEnd(false);
									((ApplicationContext)ShowAllLocationActivity.this.getApplicationContext()).setSessionStart(true);
									Intent intent = new Intent(ShowAllLocationActivity.this, MainActivity.class);
									startActivity(intent);
									ShowAllLocationActivity.this.finish();
								}
								else
									Toast.makeText(ShowAllLocationActivity.this, "Sorry no data found", Toast.LENGTH_SHORT).show();
												
							}

						}.execute();
					}
				});
				helpBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog
						dialog.cancel();
					}
				});
				AlertDialog helpDialog = helpBuilder.create();
				helpDialog.show();

			}
		});

		/*submitImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				LocationBean locationBean = new LocationBean();
				locationBean.setFromLocation(fromLocationEditText.getText().toString());
				locationBean.setToLocation(toLocationEditText.getText().toString());
				locationBean.setVehicle(vehicle);
				LocationDBHelper locationDBHelper = new LocationDBHelper(ShowAllLocationActivity.this);
				locationBean.setLocationId(locationDBHelper.createLocation(locationBean));
				Toast.makeText(ShowAllLocationActivity.this, "Location set successfully "+locationBean.getLocationId(), Toast.LENGTH_SHORT).show();
				((ApplicationContext)ShowAllLocationActivity.this.getApplicationContext()).setSession(true);
				//((ApplicationContext)SetLocationActivity.this.getApplicationContext()).setLocationBean(locationBean);
				((ApplicationContext)ShowAllLocationActivity.this.getApplicationContext()).setLocationId(locationBean.getLocationId());
				ShowAllLocationActivity.this.finish();
			}
		});

		backImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowAllLocationActivity.this.finish();
			}
		});*/
	}



}
