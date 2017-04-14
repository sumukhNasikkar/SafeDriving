package com.android.mk.driving.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.android.mk.driving.R;


public class WelcomeActivity extends Activity {
	ImageButton start;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_layout);
		
		//code for button click start
		start=(ImageButton) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(WelcomeActivity.this,LoginActivity.class);
				startActivity(i);
			}
		});

}
}
