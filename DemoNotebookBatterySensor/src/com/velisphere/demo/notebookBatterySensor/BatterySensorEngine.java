package com.velisphere.demo.notebookBatterySensor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.TimerTask;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import com.velisphere.milk.amqpClient.AmqpClient;
import com.velisphere.milk.configuration.ConfigData;

public class BatterySensorEngine extends TimerTask {
   
	private static final String batteryOneID = "07da2f9d-7160-475d-9d3d-fcaccd9e3f6e";
	private static final String batteryTwoID = "da5e6822-64e2-45e7-b039-8d626ec8db3c";
	private static final String locationID = "befec1c0-076d-4d99-889f-754b194dde25";
	private AmqpClient amqpClient;
	
	public BatterySensorEngine(AmqpClient amqpClient)
	{
		this.amqpClient = amqpClient;
	}
	
	
	public void run() {
    	
    			
		// determine battery count
		
		int batCount = determineBatteryCount();
		
		// determine charge level and submit to broker
		
		int i = 1;
		while (1 <= batCount)
		{
			try {
				if (i == 1)
				{
					submitToBroker(batteryOneID, determineChargeLevel(1));	
				}
				
				else if (i == 2)
				{
					submitToBroker(batteryTwoID, determineChargeLevel(2));	
				}
				
				
			} catch (IOException e) {
				System.out.println(" [ER] Error determining charge levle for battery #" + i);
				e.printStackTrace();
			}
			
			i = i + 1;
		}
		
			
		
		     		
		
	}
	
	private void submitToBroker(String batteryID, String chargeLevel)
	{
	
	    // send to controller
	    
	    try {
			
			HashMap<String, String> messageHash = new HashMap<String, String>();
			
			// Sensor Data
			
			messageHash.put(batteryID, chargeLevel);
		
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
			
			// messageHash.put(locationID, "{"	+ String.valueOf(location.latitude) + "}" + "[" + String.valueOf(location.longitude) + "]");
			
			
			// Send out
			
			System.out.println(" [IN] Message Hash Sent to Controller: " + messageHash);
			
			amqpClient.sendHashTable(messageHash, ConfigData.epid, "REG");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	       
		
	}

	
	private String determineChargeLevel(int batNumber) throws IOException
	{
		// determine charge level
		
		Process retrieveChargeLevel = Runtime.getRuntime().exec("cat /sys/class/power_supply/CMB"+batNumber+"/capacity");		
		InputStream inputStream = retrieveChargeLevel.getInputStream();
	    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	    String chargeLevel = bufferedReader.readLine();
	    System.out.println(" [IN] Battery #"+batNumber+" charge level is " + chargeLevel);
	    return chargeLevel;

	}
    
    private int determineBatteryCount()
    {
    	//this method is to determine if and how many batteries are present 
    	
    	int batCount = 0;
    	
    	Path path = Paths.get("/sys/class/power_supply/CMB1");

    	if (Files.exists(path)) {
    	    batCount = batCount + 1;
    	}

    	path = Paths.get("/sys/class/power_supply/CMB2");

    	if (Files.exists(path)) {
    	    batCount = batCount + 1;
    	}

    	
    	return batCount;
    }
    
    
    
    
}
