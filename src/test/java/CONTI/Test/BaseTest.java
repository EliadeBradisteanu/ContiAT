package CONTI.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.Status;

import CONTI.Test.utils.PropertyManager;
import CONTI.Test.utils.ScreenshotUtil;
import CONTI.Test.utils.extentReports.ExtentTestManager;
import CONTI.Test.utils.webDriverConfig.DriverManager;
import CONTI.Test.utils.webDriverConfig.DriverManagerFactory;
import CONTI.Test.utils.webDriverConfig.DriverType;
import CONTI.testdata.Testdata;

public abstract class BaseTest {

	protected Testdata testData;
	protected DriverManager driverManager;
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected static PropertyManager propertyManager;
	private Logger log = Logger.getLogger(BaseTest.class);

	public WebDriver getDriver() {
		return driver;
	}

	@BeforeSuite
	public void setUp() {		
		ScreenshotUtil.deleteScreenshotDir();
		propertyManager = PropertyManager.getInstance();	

		DOMConfigurator.configure("log4j.xml");
	}

	@AfterSuite
	public void tearDown() {			
		driverManager.stopService();
	}
	
	protected void runDriver() {
		boolean onWindows = System.getProperty("os.name").startsWith("Windows");

		if(propertyManager.getRunRemote()) {			
			setupRemoteWebDriver();
		}else {
			if(onWindows) {
				setupWebDriver(DriverType.CHROME);
			}else {
				setupWebDriver(DriverType.CHROME_LINUX);
			}
		}
	}

	protected void setupWebDriver(DriverType driverType) {

		switch (driverType) {
		case CHROME:
			driverManager = DriverManagerFactory.getManager(DriverType.CHROME);
			break;
		case CHROME_LINUX:
			driverManager = DriverManagerFactory.getManager(DriverType.CHROME_LINUX);
			break;
		case FIREFOX:
			driverManager = DriverManagerFactory.getManager(DriverType.FIREFOX);
			break;
		case IE:
			driverManager = DriverManagerFactory.getManager(DriverType.IE);
			break;
		default:
			driverManager = DriverManagerFactory.getManager(DriverType.CHROME);
			break;
		}

		this.driver = driverManager.getDriver();
		this.wait = new WebDriverWait(driver, 20);
		driver.manage().deleteAllCookies();
		// dynamic wait
		driver.manage().timeouts().pageLoadTimeout(propertyManager.getPageLoadTimeout(), TimeUnit.SECONDS);
		// implicitly wait - is applied globally
		driver.manage().timeouts().implicitlyWait(propertyManager.getImplicitWait(), TimeUnit.SECONDS);

	}

	protected void setupRemoteWebDriver()  {	
		
		DesiredCapabilities dc = DesiredCapabilities.chrome();
		Proxy proxy = new Proxy();
		proxy.setHttpProxy(propertyManager.getRemoteHttpProxy());
		proxy.setSslProxy(propertyManager.getRemoteSslProxy());
		proxy.setNoProxy(propertyManager.getRemoteNoProxy());
		dc.setCapability(CapabilityType.PROXY, proxy);
		dc.setPlatform(Platform.LINUX);
		dc.setBrowserName(BrowserType.CHROME);
		dc.setBrowserName("chrome");
		dc.setCapability("acceptInsecureCerts", true);
		dc.setCapability("acceptSslCerts", true);
	
		URL hub;
		try {
			hub = new URL(propertyManager.getRemoteDriverUrl());
			driver = new RemoteWebDriver(hub, dc);
		} catch (MalformedURLException e) {
			log.error("Incorect hub URL");
			e.printStackTrace();
		}
		
		log.error("Could not start RemoteWebDriver");
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

	}

	protected void setupTestdata() {		
		testData = new Testdata();
		try {
			testData.setExcelFile("Testdata.xls", "CONTI");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("setupTestdata failed", e);
		}		
	}

	protected String getTestData(int rowNum, int colNum) {
		String data = "";
		try {
			data = testData.getCellData(rowNum, colNum);
		} catch (Exception e) {
			ExtentTestManager.getTest().log(Status.FAIL, "data could not be read");
			log.error("data could not be read in row " + rowNum + ", column " + colNum, e);
			e.printStackTrace();
		}
		return data;
	}		
}
