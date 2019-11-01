package CONTI.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import CONTI.Test.utils.ScreenshotUtil;
import CONTI.Test.utils.extentReports.ExtentTestManager;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.MyFleetPage;
import CONTI.utils.TimeUtil;

public class KPIsNoAdminMyFleet_14369 extends BaseTest {
	
	private static final String TESTNAME = "KPIs No Admin My Fleet";
	private Logger log = Logger.getLogger(KPIsNoAdminMyFleet_14369.class);
	private SoftAssert assertSoftly = new SoftAssert();

	private LoginPage loginPage;
	private MainPage mainPage;
	private MyFleetPage myFleetPage;
	
	@BeforeClass
	public void before() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Open Homepage and Verify Login"
                                       + "<br> 2. Check KPIs");
		
		loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());
	}

	@AfterClass
	public void cleanUp() {
		mainPage.doLogout();
		getDriver().close();
		ExtentTestManager.endTest();
	}	
	
	@Test(priority = 41, description = "1. Open Homepage and Verify Login")
	public void OpenHomepageAndVerifyLogin() {
		
        ScreenshotUtil.takeScreenshot(driver,TESTNAME, "1-beforeLogin");		
		
		log.info("login with username: " + propertyManager.getUserTcUserA());
		mainPage = loginPage.doLogin(propertyManager.getUserTcUserA(), propertyManager.getPassTcUserA());
		loginPage.openUrl(propertyManager.getTcUserAUrl());
		
		ExtentTestManager.getTest().log(Status.INFO, "Login proceeded.");
	 	
		// After login there may be a popup article which needs to be closed
		mainPage.closeArticleIfVisible();
		
		// Check if correct user has been logged in
		boolean checkUsername = mainPage.getLoggedInUser().contains(propertyManager.getUserTcUserA());
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-afterLogin");	
		
		assertSoftly.assertTrue(checkUsername, "checkusername");
		if (checkUsername) {
			ExtentTestManager.getTest().log(Status.PASS, "1. Open Homepage and Verify Login PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "1. Open Homepage and Verify Login FAILED");
		}

		assertSoftly.assertAll();
	}
	
	@Test(priority = 42, description = "2. Check KPIs")
	public void checkKPIs() {
			
		myFleetPage = mainPage.openContiConnect()
                              .openMyFleet();
		
		TimeUtil.sleep(5, TimeUnit.SECONDS);
		String kpiMonitored = myFleetPage.getKpiMonitored();
		String kpiGreen = myFleetPage.getKpiGreen();
		String kpiIncomplete = myFleetPage.getKpiIncomplete();
		
        log.info("Vehicles Monitored:" + kpiMonitored + " Vehicles Green:" + kpiGreen + " Incomplete Vehicles:" + kpiIncomplete);
		
        ScreenshotUtil.takeScreenshot(driver, TESTNAME, "2-kpis");		
		
		String kpiMonitoredJson = getJsonKpis(propertyManager.getMonitoredvehicleUrl());
		String kpiGreenJson = getJsonKpis(propertyManager.getGreendvehicleUrl());
		String kpiIncompleteJson = getJsonKpis(propertyManager.getIncompletedvehicleUrl());
		
		log.info("JSON - Vehicles Monitored:" + kpiMonitoredJson + " Vehicles Green:" + kpiGreenJson + " Incomplete Vehicles:" + kpiIncompleteJson);
		        
        Map<String, Boolean> result = new HashMap<String, Boolean>();

		result.put("KPI Monitored",	kpiMonitored.equals(kpiMonitoredJson));
		result.put("KPI Green", kpiGreen.equals(kpiGreenJson));
		result.put("KPI Incomplete", kpiIncomplete.equals(kpiIncompleteJson));
	
		myFleetPage.switchToFirstTab();
		
		result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "2. KPI check: " + k + " PASSED");
			else
				ExtentTestManager.getTest().log(Status.FAIL, "2. KPI check: " + k + " FAILED");
		});
		
		assertSoftly.assertAll();
 	}
	
	private String getJsonKpis(String url) {
		return myFleetPage.openNewTab()
                          .openUrl(url)
                          .getBody().getText().split(":")[4].split("\\.")[0];
	}
}
