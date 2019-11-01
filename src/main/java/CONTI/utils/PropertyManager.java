package CONTI.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyManager {

	private static final String CHROME_DRIVER_ONWINDOWS_PATH = "chrome_driver_onwindows_path";
	private static final String CHROME_DRIVER_ONLINUX_PATH = "chrome_driver_onlinux_path";
	private static final String FIREFOX_DRIVER_ONWINDOWS64_PATH = "firefox_driver_onwindows64_path";
	private static final String IE_DRIVER_ONWINDOWS_PATH = "ie_driver_onwindows_path";
	private static final String REMOTE_DRIVER_URL = "remote_driver_url";
	
	
	private static final String CONTI_URL = "conti_url";
 	
	
	
	private static PropertyManager instance;
	private static final Object lock = new Object();
	private static String propertyFilePath = System.getProperty("user.dir")
			+ "\\src\\main\\resources\\config\\MainConfiguration.properties";
	private static Properties properties = new Properties();

	private static Logger log = Logger.getLogger(PropertyManager.class);

	/**
	 * Create a singleton instance. We need one instance of Property Manager
	 * 
	 * @return
	 */
	public static PropertyManager getInstance() {
		if (instance == null) {
			synchronized (lock) {
				instance = new PropertyManager();
				instance.loadData();
			}
		}
		return instance;
	}

	private void loadData() {
		try {
			properties.load(new FileInputStream(propertyFilePath));
			//properties.load(this.getClass().getClassLoader().getResourceAsStream("config/MainConfiguration.properties"));
		} catch (IOException e) {
			log.error("Error while reading the properties file");
		}
	}

	public String getChromeDriverOnWindowsPath() {
		return properties.getProperty(CHROME_DRIVER_ONWINDOWS_PATH);
	}

	public static String getChromeDriverOnlinuxPath() {
		return properties.getProperty(CHROME_DRIVER_ONLINUX_PATH);
	}

	public static String getFirefoxDriverOnwindows64Path() {
		return properties.getProperty(FIREFOX_DRIVER_ONWINDOWS64_PATH);
	}

	public static String getIeDriverOnwindowsPath() {
		return properties.getProperty(IE_DRIVER_ONWINDOWS_PATH);
	}

	public static String getRemoteDriverUrl() {
		return properties.getProperty(REMOTE_DRIVER_URL);
	}
	
	public String getContiUrl() {
		return properties.getProperty(CONTI_URL);
	}

}
