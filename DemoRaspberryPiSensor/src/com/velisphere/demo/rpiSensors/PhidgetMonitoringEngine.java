package com.velisphere.demo.rpiSensors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.Phidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;
import com.phidgets.event.OutputChangeEvent;
import com.phidgets.event.OutputChangeListener;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;
import com.velisphere.milk.amqpClient.AmqpClient;
import com.velisphere.milk.configuration.ConfigData;

public class PhidgetMonitoringEngine {

	private String touchSensorID = "f3b93c91-dee0-4609-ac1c-100dd00e060a";
	private String lightSensorID = "67f6edfa-3413-41a7-ab16-e9ed72136696";
	private String pressureSensorID = "72d3bbef-1f8e-4d8c-a838-ea119ef98592";
	private String dialKnobID = "ac4bd814-3a65-42f8-b5ad-75e71e63640b";
	private String locationID = "24c1ea6d-c2c5-420d-9743-f718e221e07d";
	
	private AmqpClient amqpClient;
	
	public PhidgetMonitoringEngine(AmqpClient amqpClient)
	{
		this.amqpClient = amqpClient;
	}
	
	
	public void startMonitoring() throws PhidgetException, IOException
	{
			
		System.out.println(Phidget.getLibraryVersion()); 
		final InterfaceKitPhidget ik = new InterfaceKitPhidget(); 
		ik.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae) { 
				System.out.println("attachment of " + ae); 
			} 
		}); 
		ik.addDetachListener(new DetachListener() { 
			public void detached(DetachEvent ae) { 
				System.out.println("detachment of " + ae); 
			} 
		}); 
		ik.addErrorListener(new ErrorListener() { 
			public void error(ErrorEvent ee) { 
				System.out.println("error event for " + ee); 
			} 
		}); 
		ik.addInputChangeListener(new InputChangeListener() { 
			public void inputChanged(InputChangeEvent oe) { 
				System.out.println(oe); 
			} 
		}); 
		ik.addOutputChangeListener(new OutputChangeListener() { 
			public void outputChanged(OutputChangeEvent oe) { 
				System.out.println(oe);
							} 
		}); 
		ik.addSensorChangeListener(new SensorChangeListener() { 
			public void sensorChanged(SensorChangeEvent se) { 
				System.out.println(se);
				System.out.println("[IN] Value change triggered on Sensor "+se.getIndex() +". New Value:" + se.getValue());

				
				try {
					// send to controller
					
					HashMap<String, String> messageHash = new HashMap<String, String>();
					
					// Sensor Data
					
					messageHash.put(touchSensorID, String.valueOf(ik.getSensorValue(6)));
					messageHash.put(lightSensorID, String.valueOf(ik.getSensorValue(7)));
					messageHash.put(pressureSensorID, String.valueOf(ik.getSensorValue(5)));
					messageHash.put(dialKnobID, String.valueOf(ik.getSensorValue(1)));
				
					// Geo Location
					
					File dbfile = new File("GeoLiteCity.dat");
					LookupService lookupService = new LookupService(dbfile, LookupService.GEOIP_MEMORY_CACHE);

					URL whatismyip = new URL("http://checkip.amazonaws.com");
					BufferedReader in = new BufferedReader(new InputStreamReader(
					                whatismyip.openStream()));

					String ip = in.readLine(); //you get the IP as a String
					
					Location location = lookupService.getLocation(ip);

					// Populate region. Note that regionName is a MaxMind class, not an instance variable
					if (location != null) {
					    location.region = regionName.regionNameByCode(location.countryCode, location.region);
					}
					
					messageHash.put(locationID, "{"	+ String.valueOf(location.latitude) + "}" + 
							"[" + String.valueOf(location.longitude) + "]");
					
					
					// Send out
					
					System.out.println("Message Hash Sent to Controller: " + messageHash);
					
					amqpClient.sendHashTable(messageHash, ConfigData.epid, "REG");
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		}); 
		ik.openAny(); 
		System.out.println("waiting for InterfaceKit attachment..."); 
		ik.waitForAttachment(); 
		System.out.println(ik.getDeviceName()); 
		System.in.read(); 
		ik.close(); 
		System.out.println(" ok"); 
		 
	} 
	

	
	
	
}
