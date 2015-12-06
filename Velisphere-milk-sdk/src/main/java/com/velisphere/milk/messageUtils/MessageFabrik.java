package com.velisphere.milk.messageUtils;


import java.io.IOException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;


/*
 * This class contains all methods needed to extract values from a MessagePack or to build a new MessagePack
 */


public class MessageFabrik {

	
	private String jsonString;
	
	public MessageFabrik(Object object)
	{
	
		ObjectMapper mapper = new ObjectMapper();

		jsonString = new String();

		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
				
	}

	
	
	public static String extractProperty(String jsonInput, String propertyID) throws IOException 
	{

		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser jp = factory.createParser(jsonInput);
		String foundValue = new String();

		while (jp.nextToken() != JsonToken.END_OBJECT) {

			String fieldname = jp.getCurrentName();
			if (propertyID.equals(fieldname)) {
				jp.nextToken();
				foundValue = jp.getText(); 
			}
		}

		jp.close();		 

		return foundValue;  
	}



	public String getJsonString() {
		return jsonString;
	}

		
	public static String[] parseOuterJSON(String messageBody) throws IOException
	{
	
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser jp = factory.createParser(messageBody);
		
		
		String[] hMACandPayload = new String[2];
		
		while (jp.nextToken() != JsonToken.END_OBJECT) {

			
			hMACandPayload[0] = jp.getCurrentName();
			hMACandPayload[1] = jp.getText();
		}

		jp.close();		 
		

		return hMACandPayload;  

	}
	

}
