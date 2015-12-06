package com.velisphere.demo.rpiSensors;

import com.velisphere.milk.restClient.ProvisioningClient;

public class PreDeployment {
	
	public static void initiateDeployment()
	{
		System.setProperty("jsse.enableSNIExtension", "false");
		String provisioningID = ProvisioningClient.macProvisioning();
		System.out.println("[IN] Provisioning request successfully submitted.");
		System.out.println("[IN] Go to www.velisphere.com, log in with your user name and start the provisioning wizard to complete.");
		System.out.println("[IN] Your Device ID is: " + provisioningID);
		
	}

}
