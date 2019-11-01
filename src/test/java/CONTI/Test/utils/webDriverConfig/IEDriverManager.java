package CONTI.Test.utils.webDriverConfig;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;

public class IEDriverManager extends DriverManager{

	private InternetExplorerDriverService ieService;;
	private static Logger log = Logger.getLogger(IEDriverManager.class);

	
	
	@Override
	protected void startService() {
		if (null == ieService) {
			try {
				ieService = new InternetExplorerDriverService.Builder()
						.usingDriverExecutable(new File(propertyManager.getIeDriverOnwindowsPath()))
						.usingAnyFreePort()
						.build();

				ieService.start();
			} catch (IOException e) {				
				log.error("Error could not start Internet Explorer service");
			}
		}
		
	}

	@Override
	public void stopService() {
		if (null != ieService && ieService.isRunning())
			ieService.stop();
		
	}

	@Override
	protected void createDriver() {
		System.setProperty("webdriver.ie.driver", propertyManager.getIeDriverOnwindowsPath());		
		InternetExplorerOptions options = new InternetExplorerOptions()
				.destructivelyEnsureCleanSession();
		options.ignoreZoomSettings() ;	
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		options.setCapability("acceptInsecureCerts", true);
		options.setCapability("acceptSslCerts",true);
		driver = new InternetExplorerDriver(options);
		driver.manage().window().maximize();
		
	}	

}
