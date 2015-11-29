package com.velisphere.milk.interfaces;

public interface EventListener {

	public void isAliveRequested();
	public void allPropertiesRequested();
	public void newInboundMessage(String message);
	
}
