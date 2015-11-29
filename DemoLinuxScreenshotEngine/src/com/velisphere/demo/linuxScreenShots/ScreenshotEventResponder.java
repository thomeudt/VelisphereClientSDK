package com.velisphere.demo.linuxScreenShots;

import java.io.IOException;
import java.util.HashMap;

import com.velisphere.milk.amqpClient.AmqpClient;
import com.velisphere.milk.configuration.ConfigData;
import com.velisphere.milk.interfaces.EventListener;
import com.velisphere.milk.messageUtils.MessageFabrik;




public class ScreenshotEventResponder implements EventListener {
   
	@Override
	public void isAliveRequested() {

		System.out.println("IsAlive Requested...");
		
		HashMap<String, String> messageHash = new HashMap<String, String>();
		messageHash.put("setState", "REACHABLE");
		
		try {
			AmqpClient.sendHashTable(messageHash, ConfigData.epid, "CTL");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void allPropertiesRequested() {

		System.out.println("AllProperties Requested, but not supported by VeliFS");
		
		
	}


	@Override
	public void newInboundMessage(String message) {
		// TODO Auto-generated method stub

	}
}