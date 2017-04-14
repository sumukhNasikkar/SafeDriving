package com.android.mk.driving.safety.activity;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.mk.driving.safety.R;
import com.android.mk.driving.safety.location.LocationBean;


public class AllLocationListAdapter  extends BaseAdapter {
	
	//private final int[] bgColors = new int[] { R.color.list_bg_1, R.color.list_bg_2 };
	private final String TAG = "com.android.mk.location.profile.AllProfileListAdapter";
	private Context mContext;
	private ArrayList<LocationBean> locationBeanList;
	
	//Constructor
	public  AllLocationListAdapter(Context context,ArrayList<LocationBean> locationBeanList){
		mContext=context;
		this.locationBeanList = locationBeanList;
	}
	
	public void updateLocationBeanList(ArrayList<LocationBean> locationBeanList)
	{
		this.locationBeanList = locationBeanList;
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
		
	}


	@Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		super.notifyDataSetInvalidated();
	}


	public int getCount() {
		// TODO Auto-generated method stub
		return locationBeanList.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	//TODO Mayank use view holderfrom dishADapter

	public View getView(int position, View view, ViewGroup arg2) {
		
		ViewHolder viewHolder;
		if(view == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.route_list, null);
			
			viewHolder = new ViewHolder();
			viewHolder.locationIdTextView = (TextView)view.findViewById(R.id.route_list_S_No_TextView);
			viewHolder.fromLocationTextView = (TextView)view.findViewById(R.id.route_list_fromlocationTextView);
			viewHolder.toLocationTextView = (TextView)view.findViewById(R.id.route_list_toLocationTextView);
			
			view.setTag(viewHolder);
        }
        else
        {
        	viewHolder = (ViewHolder)view.getTag();
        }
		Log.d(TAG,"location id in profile"+locationBeanList.get(position).getLocationId());
		
		viewHolder.locationIdTextView.setText((position+1)+"");
		viewHolder.fromLocationTextView.setText(locationBeanList.get(position).getFromLocation());
		viewHolder.toLocationTextView.setText(locationBeanList.get(position).getToLocation());
		
		return view;
	}

	private static class ViewHolder{

		TextView locationIdTextView, fromLocationTextView, toLocationTextView;
	}
}
