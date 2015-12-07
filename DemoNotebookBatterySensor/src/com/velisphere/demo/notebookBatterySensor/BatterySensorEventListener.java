package com.velisphere.demo.notebookBatterySensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.velisphere.milk.amqpClient.AmqpClient;
import com.velisphere.milk.configuration.ConfigData;
import com.velisphere.milk.interfaces.EventListener;
import com.velisphere.milk.messageUtils.MessageFabrik;




public class BatterySensorEventListener implements EventListener {
	
	private static final String rebootID = "0439691d-ed98-4bf4-a199-0a7f39faa54c";
	private static final String sleepID = "a1e5ab03-264b-48b8-bcdd-90d1d31cad98";
	private static final String shutdownID = "99285f16-5a9e-4b26-912b-d5732c97adb7";

	
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
			
			System.out.println("AllProperties requested and discarded as not supported by the battery sensor demo");
			
		}

		@Override
		public void newInboundMessage(AmqpClient amqpClient, String message) {
		
			// First, notify that a new message has been received
			
			System.out.println(" [IN] New Inbound Message");
			System.out.println(" [IN] Content: " + message);
			
			// Now check for relevant messages
			
			// Do we have to reboot?
			
			try {
				String rebootRequest = MessageFabrik.extractProperty(message, rebootID);
				if(rebootRequest.equals("1")) triggerReboot();
			} catch (IOException e) {
				// do nothing if property not found
			}

			
			// Do we have to go to sleep?
			
			try {
				String sleepRequest = MessageFabrik.extractProperty(message, sleepID);
				System.out.println(" [IN] Sleep ID: " + sleepRequest);
				if(sleepRequest.equals("1")) triggerSleep();
			} catch (IOException e) {
				// do nothing if property not found
			}
			
			// Do we have to shut down?
			
			try {
				String shutdownRequest = MessageFabrik.extractProperty(message, shutdownID);
				if(shutdownRequest.equals("1")) triggerShutdown();
			} catch (IOException e) {
				// do nothing if property not found
			}


			
		}

		
		private void triggerReboot() throws IOException
		{
			System.out.println(" [IN] Reboot requested. Rebooting now.");
			Process retrieveChargeLevel = Runtime.getRuntime().exec("shutdown -r 5");		
			InputStream inputStream = retrieveChargeLevel.getInputStream();
		    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		    String returnValue = bufferedReader.readLine();
		    System.out.println(" [IN] Reboot request returned value " + returnValue);
		}
		

		private void triggerSleep() throws IOException
		{
			System.out.println(" [IN] Sleep mode requested. Going to sleep now.");
			Process retrieveChargeLevel = Runtime.getRuntime().exec("pm-suspend");		
			InputStream inputStream = retrieveChargeLevel.getInputStream();
		    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		    String returnValue = bufferedReader.readLine();
		    System.out.println(" [IN] Sleep request returned value " + returnValue);
	
		}
		

		private void triggerShutdown() throws IOException
		{
			System.out.println(" [IN] Shutdown requested. Shutting down now.");
			Process retrieveChargeLevel = Runtime.getRuntime().exec("shutdown -h 5");		
			InputStream inputStream = retrieveChargeLevel.getInputStream();
		    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		    String returnValue = bufferedReader.readLine();
		    System.out.println(" [IN] Shutdown request returned value " + returnValue);
	
			
		}
		
		
		
}
