package com.velisphere.milk.interfaces;

import com.velisphere.milk.amqpClient.AmqpClient;


public interface EventListener {

    public void requestIsAlive(AmqpClient amqpClient);
    public void requestAllProperties(AmqpClient amqpClient);    
    public void newInboundMessage(AmqpClient amqpClient, String message);

	
}
