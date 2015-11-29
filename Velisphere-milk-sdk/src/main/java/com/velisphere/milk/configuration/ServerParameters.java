package com.velisphere.milk.configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class ServerParameters {

	public static String bunny_ip = "";

	public static void autoConf() {
		Client client = ClientBuilder.newClient();

		WebTarget target = client
				.target("http://www.connectedthingslab.com:8080/BlenderServer/rest/config/get/general");
		Response response = target.path("RABBIT").request().get();

		ServerParameters.bunny_ip = response.readEntity(String.class);

		System.out.println(" [IN] Message broker URL: "
				+ ServerParameters.bunny_ip);

	}
}