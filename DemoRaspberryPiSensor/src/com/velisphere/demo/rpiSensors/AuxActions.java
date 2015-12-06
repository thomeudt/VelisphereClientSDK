package com.velisphere.demo.rpiSensors;

import java.io.IOException;

public class AuxActions {

	public void rebootSystem() throws IOException
	{
		Process p = Runtime.getRuntime().exec("shutdown -r 5");
	}
	
}
