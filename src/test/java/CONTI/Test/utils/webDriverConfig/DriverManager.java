package CONTI.Test.utils.webDriverConfig;

import org.openqa.selenium.WebDriver;

import CONTI.Test.utils.PropertyManager;


public abstract class DriverManager {

	protected WebDriver driver;
	protected static PropertyManager propertyManager = PropertyManager.getInstance();
    
	protected abstract void startService();
    public abstract void stopService();
    protected abstract void createDriver();
    
    public void quitDriver() {
        if (null != driver) {
        	driver.close();
            driver.quit();
            driver = null;
        }

    }

    public WebDriver getDriver() {
        if (null == driver) {
            startService();
            createDriver();
        }
        return driver;
    }
    
	
	
}
