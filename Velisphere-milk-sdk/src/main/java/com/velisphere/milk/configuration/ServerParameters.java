package com.velisphere.milk.configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
public class ServerParameters {

	  public static String bunny_ip = "";

	
	  
	  public static void autoConf(){
			Client client = ClientBuilder.newClient();

			WebTarget target = client.target( "http://www.connectedthingslab.com:8080/BlenderServer/rest/config/get/general" );
			Response response = target.path("RABBIT").request().get();
			
			
			ServerParameters.bunny_ip = response.readEntity(String.class);

			System.out.println(" [IN] Message broker URL: " + ServerParameters.bunny_ip);

	  }
}