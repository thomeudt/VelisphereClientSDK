package com.velisphere.demo.linuxScreenShots;

import java.io.IOException;
import java.util.Timer;
import com.velisphere.milk.amqpClient.AmqpClient;
import com.velisphere.milk.configuration.ConfigFileAccess;
import com.velisphere.milk.configuration.Provisioner;

public class LinuxScreenshotsMain {

	public static void main(String[] args) throws IOException  {
	
		System.out.println(" ----------------------------------------------------------------------------------------------");
		System.out.println(" VELISPHERE DEMO: SCREENSHOT ENGINE");
		System.out.println(" (C) 2015 Thorsten Meudt");	
		System.out.println(" Licensed under the GPLv2 license, http://www.gnu.org/licenses/old-licenses/gpl-2.0.de.html");
		System.out.println(" Learn more about the VeliSphere IoT System at www.connectedthingslab.com");
		System.out.println(" ----------------------------------------------------------------------------------------------");
		System.out.println(" ");	
		
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

		
		ScreenshotEventListener eventListener = new ScreenshotEventListener();

		
		// Start Server and activate listener

		AmqpClient amqpClient = new AmqpClient(eventListener);
		
		amqpClient.startClient();
		
		// Start timer to submit screenshots via HTTP
		
		Timer timer = new Timer();
		 timer.schedule(new ScreenShotEngine(), 0, 15000);
		
		
		

	}
	
	
	
}
