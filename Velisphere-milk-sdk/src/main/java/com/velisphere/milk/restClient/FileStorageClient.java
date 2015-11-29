package com.velisphere.milk.restClient;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import com.velisphere.milk.configuration.ConfigData;
import com.velisphere.milk.messageUtils.MessageFabrik;
import com.velisphere.milk.security.HashTool;

public class FileStorageClient {
	
	public static String uploadFile(String localPath, String fileType) {
		
		String uploadID = "";
		
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
			
					

							
		
		
		
		// create JAX-RS Client

		Client client = ClientBuilder.newBuilder().sslContext(sc).build();

		
		// collect REST Web Service (TOUCAN) address from Blender Configuration Server
		WebTarget target = client.target( "http://www.connectedthingslab.com:8080/BlenderServer/rest/config/get/general" );
		Response response = target.path("TOUCAN").request().get();
		String toucanIP = response.readEntity(String.class);
		
		
		// get endpointID and secret from ConfigData
		String endpointID = ConfigData.epid;
		String secret = ConfigData.secret;
				
		
		// Register Multipart Feature for client and instantiate multipart
		
		client.register(MultiPartFeature.class);
	    MultiPart multiPart = new MultiPart();
	 
	    
	    // create file data bodybpart based on file from local file system and add to multipart message
	    FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
	            new File(localPath), MediaType.APPLICATION_OCTET_STREAM_TYPE);
	    multiPart.bodyPart(fileDataBodyPart);
	    
	    
	    // calculate key-hashed message authentication code to attach to message for authentication by VeliSphere
	    
		String hMacEndpointID = HashTool.getHmacSha1(endpointID, secret);

		// add hMAC as formdata plain text multipart to multipart message
		FormDataBodyPart hMACBodyPart = new FormDataBodyPart("hMAC", hMacEndpointID, MediaType.TEXT_PLAIN_TYPE);
	    multiPart.bodyPart(hMACBodyPart);
		
	    // post to toucan server
	    final Response res = client.target("https://"+toucanIP+"/rest/files/post/binary/upload").path(fileType).path(endpointID).request().post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));
		
		
	    // toucan server will provide a response, if things go well, this will be the upload id for further reference, for instance obtaining the file again
	    uploadID = res.readEntity(String.class);
		
		} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (KeyManagementException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		   
	    
		return uploadID;
				
	}
	
}

