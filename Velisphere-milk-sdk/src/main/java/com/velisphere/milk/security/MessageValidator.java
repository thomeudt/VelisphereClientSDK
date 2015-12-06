package com.velisphere.milk.security;

import com.velisphere.milk.configuration.ConfigData;

public class MessageValidator {

	public static boolean validateHmac(String receivedHMAC, String payload,
			String endpointID) {

		String secret = ConfigData.secret;
		System.out.println(" [IN] Secret in velisphere_config.xml: " + secret);
		String calculatedHmac = HashTool.getHmacSha1(payload, secret);
		System.out.println(" [IN] HMAC calculated using received payload and local secret: " + calculatedHmac
				+ " <> Received HMAC: " + receivedHMAC);
		boolean validationOK = false;
		if (calculatedHmac.equals(receivedHMAC))
			{
				validationOK = true;
				System.out.println(" [IN] HMAC matching - Validation OK");
			}
		else
		{
			System.out.println(" [ER] HMAC not matching - Validation failed. Could be security breach.");
		}

		return validationOK;

	}

}