package com.android.mk.driving.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.mk.driving.R;
import com.android.mk.driving.context.ApplicationContext;
import com.android.mk.driving.location.LocationBean;
import com.android.mk.driving.location.LocationDBHelper;

public class SetLocationActivity extends Activity {

	private final String TAG = "com.android.mk.driving.activity.SetLocationActivity";
	private ImageButton backImageButton ;
	private ImageButton submitImageButton;
	private EditText toLocationEditText;
	private EditText fromLocationEditText;
	private Spinner vehicleSpinner;
	private String vehicle; 
	private ArrayList<String> vehicleArrayList  = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.set_location_layout);
		
		backImageButton = (ImageButton)findViewById(R.id.set_location_layout_cancelButton);
		submitImageButton = (ImageButton)findViewById(R.id.set_location_layout_OkButton);
		toLocationEditText = (EditText)findViewById(R.id.set_location_layout_toLocationEditText);
		fromLocationEditText = (EditText)findViewById(R.id.set_location_layout_fromLocationEditText);
		vehicleSpinner = (Spinner)findViewById(R.id.set_location_layout_vehicleSpinner);	
			
		vehicleArrayList = new ArrayList<String>();
		vehicleArrayList.add("Bike");
		vehicleArrayList.add("Car");
		vehicleArrayList.add("Truck");
		vehicleArrayList.add("Bus");
		//android.R.layout.simple_spinner_item
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, vehicleArrayList);
		vehicleSpinner.setAdapter(adapter);
		
		vehicleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				vehicle = vehicleArrayList.get(arg2);
				Toast.makeText(SetLocationActivity.this, "Selected vehicle -"+vehicle, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		submitImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				LocationBean locationBean = new LocationBean();
				locationBean.setFromLocation(fromLocationEditText.getText().toString());
				locationBean.setToLocation(toLocationEditText.getText().toString());
				locationBean.setVehicle(vehicle);
				LocationDBHelper locationDBHelper = new LocationDBHelper(SetLocationActivity.this);
				locationBean.setLocationId(locationDBHelper.createLocation(locationBean));
				Toast.makeText(SetLocationActivity.this, "Location set successfully "+locationBean.getLocationId(), Toast.LENGTH_SHORT).show();
				((ApplicationContext)SetLocationActivity.this.getApplicationContext()).setSession(true);
				//((ApplicationContext)SetLocationActivity.this.getApplicationContext()).setLocationBean(locationBean);
				((ApplicationContext)SetLocationActivity.this.getApplicationContext()).setLocationId(locationBean.getLocationId());
				SetLocationActivity.this.finish();
			}
		});
		
		backImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SetLocationActivity.this.finish();
			}
		});
	}
	
	

}
