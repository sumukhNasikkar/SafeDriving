package com.android.mk.driving.config;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;



public class ConfigDBHelper {

	private static final String TAG = "com.android.mk.driving.config.ConfigDBHelper";
	private ConfigDBAdapter configDBAdapter;

	public ConfigDBHelper(Context mContext) {
		configDBAdapter= new ConfigDBAdapter(mContext);
	}
	
	public boolean updateConfiguration(ConfigBean configBean){
		configDBAdapter.open();
		boolean result = configDBAdapter.updateConfig(configBean);
		configDBAdapter.close();
		if(result)
			return true;
		else 
			return false;
	}
	
	public ConfigBean getConfigurationByPropertyType(String propertyType)
	{
		configDBAdapter.open();
		Cursor configCursor = configDBAdapter.fetchConfigByPropertyType(propertyType);
		ConfigBean configBean = null;
		if( configCursor != null)
		{
			configCursor.moveToFirst();
			if(!configCursor.isAfterLast())
			{	
				configBean = getConfigBeanFromCursor(configCursor);	
			}
			configCursor.close();
		}
		configDBAdapter.close();
		return configBean;
	}
	
	private static ConfigBean getConfigBeanFromCursor(Cursor configCursor)
	{
		ConfigBean tempBean = new ConfigBean();

		tempBean.setProperty(configCursor.getString(configCursor.getColumnIndex(ConfigDBAdapter.COLUMN_PROPERTY)));
		tempBean.setPropertyType(configCursor.getString(configCursor.getColumnIndex(ConfigDBAdapter.COLUMN_PROPERTY_TYPE)));
		tempBean.setValue(configCursor.getString(configCursor.getColumnIndex(ConfigDBAdapter.COLUMN_VALUE)));


		Log.d(TAG, "adding config bean to list : property : " + tempBean.getProperty() + " value : " + tempBean.getValue() );
		return tempBean;

	}
}
