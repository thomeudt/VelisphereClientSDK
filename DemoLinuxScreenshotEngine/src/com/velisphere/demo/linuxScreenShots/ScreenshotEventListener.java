package com.velisphere.demo.linuxScreenShots;

import java.util.HashMap;

import com.velisphere.milk.amqpClient.AmqpClient;
import com.velisphere.milk.configuration.ConfigData;
import com.velisphere.milk.interfaces.EventListener;




public class ScreenshotEventListener implements EventListener {
	
	  
	
		@Override
		public void requestIsAlive() {
	
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
		public void requestAllProperties() {
			
			System.out.println("AllProperties requested and discarded as not supported by VeliFS");
			
		}

		@Override
		public void newInboundMessage(String message) {
		
			System.out.println(" [IN] New Inbound Message");
	       
			
		}

		
}
