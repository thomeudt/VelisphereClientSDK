package com.velisphere.demo.rpiSensors;

import java.io.IOException;
import java.util.Timer;

import com.phidgets.PhidgetException;
import com.velisphere.milk.amqpClient.AmqpClient;
import com.velisphere.milk.configuration.ConfigFileAccess;
import com.velisphere.milk.configuration.Provisioner;

public class PiSensorsMain {

	public static void main(String[] args) throws IOException  {
	
		System.out.println(" ----------------------------------------------------------------------------------------------");
		System.out.println(" VELISPHERE DEMO: RaspberryPI Sensor Device using Phidgets");
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

		
		PiEventListener eventListener = new PiEventListener();
	
		// Start Server and activate listener

		AmqpClient amqpClient = new AmqpClient(eventListener);
		
		amqpClient.startClient();
		
		PhidgetMonitoringEngine engine = new PhidgetMonitoringEngine(amqpClient);
		try {
			engine.startMonitoring();
		} catch (PhidgetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		

	}
	
	
	
}
