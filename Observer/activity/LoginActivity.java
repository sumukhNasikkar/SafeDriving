package com.android.mk.driving.safety.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mk.driving.safety.R;
import com.android.mk.driving.safety.config.ConfigBean;
import com.android.mk.driving.safety.config.ConfigDBHelper;
import com.android.mk.driving.safety.constant.Constant;
import com.android.mk.driving.safety.context.ApplicationContext;
import com.android.mk.driving.safety.main.MyLocationListener;
import com.android.mk.driving.safety.main.OnAlarmReceiver;
import com.android.mk.driving.safety.util.Utility;
import com.android.mk.driving.safety.webservice.WebServiceParser;

public class LoginActivity extends Activity{

	private LocationManager locationManager;
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 100; // in Milliseconds
	private static final int PERIOD=10000;  // 5 minutes
	
	private final String TAG = "com.android.mk.reilwayticket.LoginActivity"; 
	private EditText userNameEditText;
	private EditText passwordEditText;
	private ImageButton okImageButton;
	private ImageButton BrowserImageButton;
	private ImageButton cancelImageButton;
	private static final int CONFIG_ID = Menu.FIRST;
	private String serverIPAddress = "";
	private ConfigDBHelper configDBHelper;
	private ConfigBean ipConfigBean = null;
	private ProgressDialog loginWaitDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.login_layout);

		//initialize locationmanager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
				new MyLocationListener(this));

		// initialize configuration
		Utility.initializeConfig(this);
		
		//Stop alarm service
		AlarmManager mgr=(AlarmManager)LoginActivity.this.getSystemService(Context.ALARM_SERVICE);
		Intent i=new Intent(LoginActivity.this, OnAlarmReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(LoginActivity.this, 10255, i, PendingIntent.FLAG_UPDATE_CURRENT);
		mgr.cancel(pi);
		
		//initialize alarm service
		/*AlarmManager mgr=(AlarmManager)LoginActivity.this.getSystemService(Context.ALARM_SERVICE);
		Intent i=new Intent(LoginActivity.this, OnAlarmReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(LoginActivity.this, 10256, i, PendingIntent.FLAG_UPDATE_CURRENT);
		mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime(),PERIOD,pi);*/
		
		

		ipConfigBean = ((ApplicationContext)this.getApplicationContext()).getIpConfigBean();
		if(ipConfigBean!=null)
			serverIPAddress = ipConfigBean.getValue();

		
		userNameEditText = (EditText)findViewById(R.id.login_layout_userNameEditText);
		passwordEditText = (EditText)findViewById(R.id.login_layout_passwordEditText);
		okImageButton = (ImageButton)findViewById(R.id.login_layout_OkButton);
		cancelImageButton = (ImageButton)findViewById(R.id.login_layout_cancelButton);

		
		BrowserImageButton=(ImageButton)findViewById(R.id.browseButton);
		BrowserImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//String ipaddress=Utility.getLocalIpAddress();
				String ipaddress=serverIPAddress;
				Uri uri = Uri.parse("http://" + ipaddress + ":8080/"+ "SafeDrivingServer"); // missing 'http://' will cause crashed
				Log.d(TAG, "URL: "+uri);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		
		cancelImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoginActivity.this.finish();
			}
		});
		super.onCreate(savedInstanceState);

		okImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if("".equals(serverIPAddress))
					Toast.makeText(LoginActivity.this, "Please configure server host address.", Toast.LENGTH_SHORT).show();
				else
				{
					loginWaitDialog = ProgressDialog.show(LoginActivity.this, "", "Loading", true);
					loginWaitDialog.show();
					LoginTask task = new LoginTask();
					task.execute(new String[] {"hello"});
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		menu.add(0, CONFIG_ID, 0, R.string.ip_config).setIcon(android.R.drawable.ic_menu_more);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId()) 
		{
		case CONFIG_ID: {

			//create the dialog and initialize it
			final Dialog dialog = new Dialog(LoginActivity.this);
			dialog.setContentView(R.layout.ip_config_layout);
			RelativeLayout mainRelativeLayout=(RelativeLayout)dialog.findViewById(R.id.ip_config_layout_relativelayout);
			mainRelativeLayout.requestFocus();

			//set title and cancelable
			dialog.setTitle("IP Configuration");
			dialog.setCancelable(true);

			//set up cancel buttons and on click listener
			ImageButton cancelButton = (ImageButton)dialog.findViewById(R.id.ip_config_layout_cancelButton);
			ImageButton testConnButton = (ImageButton)dialog.findViewById(R.id.ip_config_layout_testConButton);
			ImageButton okButton = (ImageButton)dialog.findViewById(R.id.ip_config_layout_OkButton);

			cancelButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			//get IP address from utility function and display it on the screen
			TextView clientIpaddressText = (TextView)dialog.findViewById(R.id.ip_config_layout_clientIpTextView);
			//	     		System.out.println(Utility.getLocalIpAddress());
			try{
				clientIpaddressText.setText("Ip Address: " + Utility.getLocalIpAddress());

			}catch(Exception e)
			{
				Log.d(TAG, "exception in find ip address : " + e.getMessage());
				clientIpaddressText.setText("Ip Address: exception " + e.getMessage());
			}
			//get the server Ip entered in the text box
			final  EditText serverIpEditText = (EditText)dialog.findViewById(R.id.ip_config_layout_serverIpEditText);
			final ConfigDBHelper configDBHelper = new ConfigDBHelper(LoginActivity.this);
			serverIpEditText.setText(serverIPAddress);
			//set up test connection button and check the connection status

			testConnButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					serverIPAddress = serverIpEditText.getText().toString();
				}
			});

			//set up ok buttons and on click listener
			okButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					try{
						ConfigBean tempConfigBean = new ConfigBean();
						tempConfigBean.setProperty(Constant.PROPERTY_SERVER_IP_ADDRERSS);
						tempConfigBean.setPropertyType(Constant.PROPERTY_TYPE_SERVER_IP_ADDRERSS);
						tempConfigBean.setValue(serverIpEditText.getText().toString());
						boolean result = configDBHelper.updateConfiguration(tempConfigBean);
						((ApplicationContext)getApplicationContext()).setIpConfigBean(tempConfigBean);
						serverIPAddress = serverIpEditText.getText().toString();
						if(result == true)
						{
							((ApplicationContext)LoginActivity.this.getApplicationContext()).setIpConfigBean(tempConfigBean);
							//Toast.makeText(ManageDataActivity.this, "ServerIP updated successfully", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
					}
					catch(Exception ex)
					{
						//Toast.makeText(ManageDataActivity.this, "exception : "  +  ex.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});
			dialog.show();
		}
		break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class LoginTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = urls[0];
			WebServiceParser webServiceParser = new WebServiceParser(LoginActivity.this, "http://"+serverIPAddress+":8080/SafeDrivingServer/rest/webService/");
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", userNameEditText.getText().toString());
			params.put("password", passwordEditText.getText().toString());
			int userId = webServiceParser.validateUser(params);
			return userId+"";
		}

		@Override
		protected void onPostExecute(String result) {
			((ApplicationContext)LoginActivity.this.getApplicationContext()).setUserId(Integer.parseInt(result));
			if(Integer.parseInt(result) >0 )
			{
				Toast.makeText(LoginActivity.this, "Login successfully.", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				userNameEditText.setText("");
				passwordEditText.setText("");
				LoginActivity.this.finish();
			}
			else
				Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
			loginWaitDialog.dismiss();
		}
	}


}
