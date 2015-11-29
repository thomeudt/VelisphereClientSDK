package com.velisphere.demo.linuxScreenShots;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import com.velisphere.milk.restClient.FileStorageClient;
import com.velisphere.milk.restClient.FileTypes;

public class ScreenShotEngine extends TimerTask {
    public void run() {
    
		try {
			 // Create time stamp
		     Date date = new Date();
		 	
		     System.out.println(" [IN] Sending Screenshot...");
			
			BufferedImage image = new java.awt.Robot().createScreenCapture(new java.awt.Rectangle(250,150,500,500));
			File outputfile = new File(System.getProperty("user.dir")
					+ System.getProperty("file.separator")
					+ "screenshot_"+date.toString()+".png");
		    ImageIO.write(image, "png", outputfile);

		    // work around SNIE bug
		    System.setProperty("jsse.enableSNIExtension", "false");

		    String result = FileStorageClient.uploadFile(outputfile.getAbsolutePath(), FileTypes.IMAGES);
			
			   System.out.println(" [IN] VeliSphere reports file ID:" + result);
				
		
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
