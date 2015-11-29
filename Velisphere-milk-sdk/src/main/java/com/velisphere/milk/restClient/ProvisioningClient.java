package com.velisphere.milk.restClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velisphere.milk.configuration.ConfigData;
import com.velisphere.milk.configuration.ConfigFileAccess;
import com.velisphere.milk.configuration.SecretData;

public class ProvisioningClient {

	public static String macProvisioning() {
		InetAddress ip;

		String identifier = "";

		// disable certificate matching check

		javax.net.ssl.HttpsURLConnection
				.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

					@Override
					public boolean verify(String hostname, SSLSession session) {
						// TODO Auto-generated method stub
						return true;
					}

				});

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("TLS");

			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Continue from here;

			Client client = ClientBuilder.newBuilder().sslContext(sc).build();

			StringBuilder sb = new StringBuilder();

			try {
				ip = InetAddress.getLocalHost();
				System.out.println("Current IP address : "
						+ ip.getHostAddress());

				Enumeration<NetworkInterface> networks = NetworkInterface
						.getNetworkInterfaces();
				while (networks.hasMoreElements()) {
					NetworkInterface network = networks.nextElement();
					byte[] mac = network.getHardwareAddress();

					if (mac != null) {
						System.out.print("Current MAC address : ");
						for (int i = 0; i < mac.length; i++) {
							sb.append(String.format("%02X%s", mac[i],
									(i < mac.length - 1) ? "-" : ""));
						}
						identifier = sb.toString();
						System.out
								.println("[IN] Using the following identifier for provisioning: "
										+ identifier);

					}
				}

				System.out
						.println("[IN] Requesting provisioning server address... ");

				WebTarget target = client
						.target("http://www.connectedthingslab.com:8080/BlenderServer/rest/config/get/general");
				Response response = target.path("TOUCAN").request().get();

				System.out
						.println("[IN] Request completed, response from server is "
								+ response);

				String toucanIP = response.readEntity(String.class);

				System.out
						.println("[IN] Using the following provisioning server:  "
								+ toucanIP);

				target = client.target("https://" + toucanIP
						+ "/rest/provisioning/put");

				System.out
						.println("[IN] Sending provisioning request to server... ");

				response = target.path("endpoint").path(identifier).request()
						.put(Entity.text(ConfigData.epcid));

				System.out.println("[IN] Response from provisioning server is "
						+ response);

				String jsonInput = response.readEntity(String.class);

				ObjectMapper mapper = new ObjectMapper();

				SecretData secretData = new SecretData();
				try {
					secretData = mapper.readValue(jsonInput, SecretData.class);
				} catch (JsonParseException e) {

					System.out
							.println("[ER] Response from server was in an unexpcted format. Could be a man in the middle attack. ");
					e.printStackTrace();
				} catch (JsonMappingException e) {

					System.out
							.println("[ER] Response from server was in an unexpcted format. Could be a man in the middle attack. ");
					e.printStackTrace();
				} catch (IOException e) {

					System.out.println("[ER] General I/O exception. ");
					e.printStackTrace();
				}
				System.out
						.println("[IN] Writing endpoint ID and authentication secret to file, and setting status to provisioned... ");

				ConfigFileAccess.saveParamChangesAsXML(secretData.getSecret(),
						secretData.getEpid(), true);

			} catch (UnknownHostException e) {
				System.out.println("[ER] Unknown Host " + e);
				e.printStackTrace();
			} catch (SocketException e) {
				System.out.println("[ER] Socket Error " + e);
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e1) {
			System.out
					.println("[ER] Required encryption algorithm is not available. Your environment might not be compatible.");
			e1.printStackTrace();
		} catch (KeyManagementException e1) {
			System.out
					.println("[ER] A key management exception occured. Your environment might not be compatible.");
			e1.printStackTrace();
		}
		return identifier;

	}

	public static String customProvisioning(String customUniqueIdentifier) {
		InetAddress ip;

		String identifier = customUniqueIdentifier;

		// disable certificate matching check

		javax.net.ssl.HttpsURLConnection
				.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

					@Override
					public boolean verify(String hostname, SSLSession session) {
						// TODO Auto-generated method stub
						return true;
					}

				});

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("TLS");

			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Client client = ClientBuilder.newClient();

			Client client = ClientBuilder.newBuilder().sslContext(sc).build();

			StringBuilder sb = new StringBuilder();

			System.out
					.println("[IN] Requesting provisioning server address... ");

			WebTarget target = client
					.target("http://www.connectedthingslab.com:8080/BlenderServer/rest/config/get/general");

			Response response = target.path("TOUCAN").request().get();

			
			System.out
					.println("[IN] Request completed, response from server is "
							+ response);

			String toucanIP = response.readEntity(String.class);

			System.out
					.println("[IN] Using the following provisioning server:  "
							+ toucanIP);

			target = client.target("https://" + toucanIP
					+ "/rest/provisioning/put");

			System.out
					.println("[IN] Sending provisioning request to server... ");

			response = target.path("endpoint").path(identifier).request()
					.put(Entity.text(ConfigData.epcid));

			System.out.println("[IN] Response from provisioning server is "
					+ response);

			String jsonInput = response.readEntity(String.class);

			ObjectMapper mapper = new ObjectMapper();

			SecretData secretData = new SecretData();
			try {
				secretData = mapper.readValue(jsonInput, SecretData.class);
			} catch (JsonParseException e) {

				System.out
						.println("[ER] Response from server was in an unexpcted format. Could be a man in the middle attack. ");
				e.printStackTrace();
			} catch (JsonMappingException e) {

				System.out
						.println("[ER] Response from server was in an unexpcted format. Could be a man in the middle attack. ");
				e.printStackTrace();
			} catch (IOException e) {

				System.out.println("[ER] General I/O exception. ");
				e.printStackTrace();
			}
			System.out
					.println("[IN] Writing endpoint ID and authentication secret to file, and setting status to provisioned... ");

			ConfigFileAccess.saveParamChangesAsXML(secretData.getSecret(),
					secretData.getEpid(), true);
		} catch (NoSuchAlgorithmException e1) {
			System.out
					.println("[ER] Required encryption algorithm is not available. Your environment might not be compatible.");
			e1.printStackTrace();
		} catch (KeyManagementException e1) {
			System.out
					.println("[ER] A key management exception occured. Your environment might not be compatible.");
			e1.printStackTrace();
		}
		return identifier;

	}

}
