package CONTI.Test.utils.webDriverConfig;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;

public class FirefoxDriverManager extends DriverManager{

	private GeckoDriverService gkService;
	private static Logger log = Logger.getLogger(FirefoxDriverManager.class);

	
	@Override
	protected void startService() {
		if (null == gkService) {
			try {
				gkService = new GeckoDriverService.Builder()
						.usingDriverExecutable(new File(propertyManager.getFirefoxDriverOnwindows64Path()))
						.usingAnyFreePort()
						.build();

				gkService.start();
			} catch (IOException e) {				
				log.error("Error could not start gecko service");
			}
		}
		
	}

	@Override
	public void stopService() {
		if (null != gkService && gkService.isRunning())
			gkService.stop();		
	}

	@Override
	protected void createDriver() {
		System.setProperty("webdriver.gecko.driver", propertyManager.getFirefoxDriverOnwindows64Path());
	    FirefoxOptions firefoxOptions = new FirefoxOptions();		
	    firefoxOptions.setCapability("marionette", true);
        driver = new FirefoxDriver(firefoxOptions);
		
	}

}
