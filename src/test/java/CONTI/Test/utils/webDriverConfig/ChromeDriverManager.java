package CONTI.Test.utils.webDriverConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverManager extends DriverManager {

	private ChromeDriverService chService;
	private static Logger log = Logger.getLogger(ChromeDriverManager.class);

	@Override
	protected void startService() {
		if (null == chService) {
			try {
				chService = new ChromeDriverService.Builder()
						.usingDriverExecutable(new File(propertyManager.getChromeDriverOnWindowsPath()))
						.usingAnyFreePort().build();

				chService.start();
			} catch (IOException e) {
				log.error("Error, could not start chrome service");
			}
		}
	}

	@Override
	public void stopService() {
		if (null != chService && chService.isRunning())
			chService.stop();
	}

	@Override
	protected void createDriver() {

		Map<String, Object> prefs = new HashMap<String, Object>();

		// Set the notification setting it will override the default setting
		prefs.put("profile.default_content_setting_values.notifications", 2);
		prefs.put("profile.default_content_settings.popups", 0);		
		prefs.put("profile.default_content_setting_values.automatic_downloads", 1);
	    prefs.put("download.default_directory", new File("src/main/resources/downloads").getAbsolutePath());

		ChromeOptions options = new ChromeOptions();		
		options.setExperimentalOption("prefs", prefs);
		options.addArguments("--disable-impl-side-painting");
		options.addArguments("--disable-gpu-sandbox");
		options.addArguments("--disable-accelerated-jpeg-decoding");
		options.addArguments("--test-type=ui");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-gpu");
		options.addArguments("--disable-notifications");
		options.addArguments("--lang=en-GB");
		options.addArguments("start-maximized");
		options.addArguments("--incognito");

		driver = new ChromeDriver(chService, options);

	}
}
