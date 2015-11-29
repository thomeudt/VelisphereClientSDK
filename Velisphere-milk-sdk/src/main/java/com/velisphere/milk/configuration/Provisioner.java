package com.velisphere.milk.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Provisioner {
	
		
	public static boolean isProvisioned()
	{
		Properties props = new Properties();
		InputStream is = null;
		boolean returnValue = false;

		try {
			File f = new File(System.getProperty("user.dir")
					+ System.getProperty("file.separator")
					+ "velisphere_config.xml");
			is = new FileInputStream(f);
			props.loadFromXML(is);
			
			if(props.getProperty("Provisioned").equals("true"))
			{
				System.out.println(" [IN] Provisioner determined provisioning state is " + props.getProperty("Provisioned"));
				
				returnValue = true;
			}
			else
			{
				System.out.println(" [IN] Provisioner determined provisioning state is " + props.getProperty("isProvisioned"));
				
				returnValue = false;
			}
		
		
		} catch (Exception e) {
			is = null;
		}
		
		
		return returnValue;

			
	}
	

}
