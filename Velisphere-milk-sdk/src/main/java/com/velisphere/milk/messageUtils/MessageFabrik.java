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
package com.velisphere.milk.messageUtils;


import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/*
 * This class contains all methods needed to extract values from a MessagePack or to build a new MessagePack
 */


public class MessageFabrik {

	
	String jsonString;
	
	public MessageFabrik(Object object)
	{
	
		ObjectMapper mapper = new ObjectMapper();
		
		System.out.println("Intake: " + object.toString());
	 
		jsonString = new String();
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		System.out.println("JSON generiert: " + jsonString);
				
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
	
		System.out.println("Parsing " + messageBody);
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
