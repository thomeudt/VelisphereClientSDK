package com.velisphere.milk.security;


	import java.security.NoSuchAlgorithmException;

	import javax.crypto.KeyGenerator;
	import javax.crypto.Mac;
	import javax.crypto.SecretKey;
	import javax.crypto.spec.SecretKeySpec;

	import org.apache.commons.codec.binary.Base64;
	import org.apache.commons.codec.binary.Hex;

	public class HashTool {
		
		public static String getHmacSha1(String value, String key) {
	        try {
	            // Get an hmac_sha1 key from the raw key bytes
	            byte[] keyBytes = key.getBytes();           
	            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

	            // Get an hmac_sha1 Mac instance and initialize with the signing key
	            Mac mac = Mac.getInstance("HmacSHA1");
	            mac.init(signingKey);

	            // Compute the hmac on input data bytes
	            byte[] rawHmac = mac.doFinal(value.getBytes());

	            // Convert raw bytes to Hex
	            byte[] hexBytes = new Hex().encode(rawHmac);

	            //  Covert array of Hex bytes to a String
	            return new String(hexBytes, "UTF-8");
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	
	}

