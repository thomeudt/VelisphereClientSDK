package com.velisphere.demo.notebookBatterySensor;

import java.io.IOException;
import java.util.Timer;
import com.velisphere.milk.amqpClient.AmqpClient;
import com.velisphere.milk.configuration.ConfigFileAccess;
import com.velisphere.milk.configuration.Provisioner;

public class BatterySensorMain {

	public static void main(String[] args) throws IOException  {
	
		System.out.println(" ----------------------------------------------------------------------------------------------");
		System.out.println(" VELISPHERE DEMO: Notebook Battery Sensor (for Linux machines, tested on Ubuntu 15.10");
		System.out.println(" (C) 2015 Thorsten Meudt");	
		System.out.println(" Licensed under the GPLv2 license, http://www.gnu.org/licenses/old-licenses/gpl-2.0.de.html");
		System.out.println(" Learn more about the VeliSphere IoT System at www.connectedthingslab.com");
		System.out.println(" ----------------------------------------------------------------------------------------------");
		System.out.println(" ");	
		System.out.println(" This Demo is designed for Ubuntu based systems and tested on Ubuntu 15.10. Older versions might");
		System.out.println(" still work.");
		System.out.println(" For the SMART monitoring function to work, smartmontools have to be installed.");
		System.out.println(" To install smartmontools, execute SUDO APT-GET INSTALL SMARTMONTOOLS");	
		System.out.println(" ----------------------------------------------------------------------------------------------");
		
		
		// Load Configuration Data
		
		ConfigFileAccess.loadParamChangesAsXML();
		
		// Check if device is already deployed. If not, trigger pre-deployment cycle
	
		System.out.println(" [IN] Calling provisioner to determine if device is flagged as provisioned.");	
		
		
		if (Provisioner.isProvisioned() == false)
			PreDeployment.initiateDeployment();
		else
			regularStartup();
					
	}
	
	public static void regularStartup()
	{


		// Activate Event Responders

		
		BatterySensorEventListener eventListener = new BatterySensorEventListener();

		
		// Start Server and activate listener

		AmqpClient amqpClient = new AmqpClient(eventListener);
		
		amqpClient.startClient();
		
		
		
	
		// Start timer to submit battery charge level at defined intervals
		
		Timer timer = new Timer();
		 timer.schedule(new BatterySensorEngine(amqpClient), 0, 5000);
		
		
		

	}
	
	
	
}
