package com.velisphere.demo.rpiSensors;

import java.util.HashMap;

import com.velisphere.milk.amqpClient.AmqpClient;
import com.velisphere.milk.configuration.ConfigData;
import com.velisphere.milk.interfaces.EventListener;




public class PiEventListener implements EventListener {
	
	  
	
		@Override
		public void requestIsAlive(AmqpClient amqpClient) {
	
			System.out.println("IsAlive Requested...");
			
			HashMap<String, String> messageHash = new HashMap<String, String>();
			messageHash.put("setState", "REACHABLE");
			
			try {
				amqpClient.sendHashTable(messageHash, ConfigData.epid, "CTL");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}

		@Override
		public void requestAllProperties(AmqpClient amqpClient) {
			
			System.out.println("AllProperties requested and discarded as not supported by PiSensor");
			
		}

		@Override
		public void newInboundMessage(AmqpClient amqpClient, String message) {
		
			System.out.println(" [IN] New Inbound Message");
	       
			
		}

		
}
