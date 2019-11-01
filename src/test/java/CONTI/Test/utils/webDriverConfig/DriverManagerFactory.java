package CONTI.Test.utils.webDriverConfig;

public final class DriverManagerFactory {
	
	private DriverManagerFactory() { }
	
	public static DriverManager getManager(DriverType type) {
		
		DriverManager driverManager;

		switch (type) {
		case CHROME:
			driverManager = new ChromeDriverManager();
			break;
		case CHROME_LINUX:
			driverManager = new ChromeDriverLinuxManager();
			break;
		case FIREFOX:
			driverManager = new FirefoxDriverManager();
			break;
		case IE:
			driverManager = new IEDriverManager();
			break;
		default:
			driverManager = new ChromeDriverManager();
			break;
		}
		
		return driverManager;
	}

}
