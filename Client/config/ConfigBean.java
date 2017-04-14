package com.android.mk.driving.config;

public class ConfigBean {

	private String property;
	private String value;
	private String propertyType;
	

	
	public String getPropertyType() {
		return propertyType;
	}


	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}


	public String getProperty() {
		return property;
	}


	public void setProperty(String property) {
		this.property = property;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	/*@Override
	protected ConfigBean clone() {
		 
		
		ConfigBean tempdishBean = new ConfigBean();
		
//		int proposedDishId = Integer.parseInt(clientData[index]);
		tempdishBean.setDishId(this.getDishId());
		tempdishBean.setDishDescription(this.getDishDescription());
		tempdishBean.setDishPrice(this.getDishPrice());
		tempdishBean.setDishName(this.getDishName());
		tempdishBean.setDishActive(this.isDishActive());
		tempdishBean.setDishType(this.getDishType());
		return tempdishBean;
	}
	*/
	
}
