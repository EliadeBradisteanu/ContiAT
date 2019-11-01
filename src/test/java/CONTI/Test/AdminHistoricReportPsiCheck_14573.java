package CONTI.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import CONTI.Test.utils.ScreenshotUtil;
import CONTI.Test.utils.extentReports.ExtentTestManager;
import CONTI.pages.GeneralInfoMyFleetPage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.MyFleetPage;
import CONTI.testdata.JsonDataHandlerVehicleData;
import CONTI.testdata.VehicleData;
import CONTI.utils.TimeUtil;

public class AdminHistoricReportPsiCheck_14573 extends BaseTest {
	private static final String VEHICLE_ID = "250";
	private static final String DATA_FOR_MY_FLEET_UPDATE = "dataForMyFleetUpdate";
	private static final String TESTNAME = "Historic report with psi";
	
	private Logger log = Logger.getLogger(AdminHistoricReportPsiCheck_14573.class);

	private SoftAssert assertSoftly = new SoftAssert();
	private JsonDataHandlerVehicleData readVehicleData;
	@SuppressWarnings("unused")
	private VehicleData vehicleData;
	
	private LoginPage loginPage;
	private MainPage mainPage;
	private MyFleetPage myFleetPage;
	private GeneralInfoMyFleetPage generalInfoMyFleetPage;

	@BeforeClass
	public void setup() throws MalformedURLException {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Open Homepage and Verify Login"
                                       + "<br> 2. Search vehicle with low Psi tire pressure");

	    loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());

		try {
			readVehicleData = new JsonDataHandlerVehicleData();
		} catch (IOException e) {
			log.error("could not read data", e);
			e.printStackTrace();
		}
		vehicleData = readVehicleData.getVehicleData(DATA_FOR_MY_FLEET_UPDATE);
	}

	@AfterClass
	public void cleanUp() {	
		mainPage.doLogout();
		getDriver().close();	
		ExtentTestManager.endTest();
	}

	@Test(priority = 15, description = "1. Open Homepage and Verify Login")
	public void openHomepageAndVerifyLogin() {		
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-beforeLogin");		
				
		log.info("login with username: " + propertyManager.getUserMhpcku1());
		mainPage = loginPage.doLogin(propertyManager.getUserMhpcku1(), propertyManager.getPasswordMhpcku12());
		
		ExtentTestManager.getTest().log(Status.INFO, "Login proceeded.");

		// After login there may be a popup article which needs to be closed
		mainPage.closeArticleIfVisible();
		
		// Check if correct user has been logged in
		boolean checkUsername = mainPage.getLoggedInUser().contains(propertyManager.getUserMhpcku1());
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-afterLogin");

		assertSoftly.assertTrue(checkUsername, "checkusername");
		if (checkUsername) {
			ExtentTestManager.getTest().log(Status.PASS, "1. Open Homepage and Verify Login PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "1. Open Homepage and Verify Login  FAILED");
		}

		assertSoftly.assertAll();
	}

	@Test(priority = 16, description = "2. Search vehicle with low Psi tire pressure")
	public void searchVehicle() {
		
		mainPage.openContiConnect();		
		myFleetPage = mainPage.openMyFleet();
	
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "2-myFleetPage");		
	
        myFleetPage.filterCustomerVehicleId(VEHICLE_ID); 
		
		generalInfoMyFleetPage = myFleetPage.clickDetailsForVehicleID(VEHICLE_ID);
		
		TimeUtil.sleep(2, TimeUnit.SECONDS);
		generalInfoMyFleetPage.scroll("700");
		
		boolean checkHistoryChart = generalInfoMyFleetPage.getHistoryChart();
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-psiVehicle");
		
		assertSoftly.assertTrue(checkHistoryChart, "checkHistoryChart");
		if (checkHistoryChart) {
			ExtentTestManager.getTest().log(Status.PASS, "3. Check History Chart Psi PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "3. Check History Chart Psi FAILED");
		}
		 
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-psiVehicle");
	}

}
