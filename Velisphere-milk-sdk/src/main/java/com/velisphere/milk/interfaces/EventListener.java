package com.velisphere.milk.interfaces;


public interface EventListener {

    public void requestIsAlive();
    public void requestAllProperties();    
    public void newInboundMessage(String message);

	
}
