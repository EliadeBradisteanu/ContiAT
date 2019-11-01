package CONTI.Test;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import CONTI.Test.utils.ScreenshotUtil;
import CONTI.Test.utils.extentReports.ExtentTestManager;
import CONTI.pages.CreateVehiclePage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.MyFleetPage;
import CONTI.pages.SearchAccountPage;
import CONTI.pages.SearchVehiclesPage;
import CONTI.testdata.JsonDataHandlerVehicleData;
import CONTI.testdata.VehicleData;

public class ShowNoShowVehicleInPortalNoAdmin_14423 extends BaseTest {
	
	private static final String TESTNAME = "Show, No Show Vehicle In Portal No Admin";
	private static final String NO_RESULTS_MESSAGE = "Pas de véhicules correspondants aux critères sélectionnés";
	private static final String VEHICLE_IN_PORTAL_NO_ADMIN = "vehicleInPortalNoAdmin";
		
	private Logger log = Logger.getLogger(ShowNoShowVehicleInPortalNoAdmin_14423.class);

	private SoftAssert assertSoftly = new SoftAssert();	
	private JsonDataHandlerVehicleData readVehicleData;
	private VehicleData vehicleData;

	private LoginPage loginPage;
	private MainPage mainPage;
	private SearchVehiclesPage searchVehiclesPage;
	private CreateVehiclePage createVehiclePage;
	private SearchAccountPage searchAccountPage;
	private MyFleetPage myFleetPage;

	@BeforeClass
	public void before() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Open Homepage and Verify Login"
                                       + "<br> 2. Create Vehicle"
                                       + "<br> 3. Check Vehicle in MyFleet does not appear");

		loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());

		try {
			readVehicleData = new JsonDataHandlerVehicleData();
		} catch (IOException e) {
			log.error("could not read data", e);
			e.printStackTrace();
		}
		vehicleData = readVehicleData.getVehicleData(VEHICLE_IN_PORTAL_NO_ADMIN);
	}

	@AfterClass
	public void cleanUp() {				
		mainPage.doLogout();
		getDriver().close();	
		vehicleData.incrementLPN();
		vehicleData.incrementInternalCustomerId();
		try {
			readVehicleData.saveData(vehicleData);
		} catch (IOException e) {
			log.error("could not write data", e);
			e.printStackTrace();
		}
		ExtentTestManager.endTest();
	}
	
	@Test(priority = 32, description = "1. Open Homepage and Verify Login")
	public void openHomepageAndVerifyLogin() {		

		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "1-beforeLogin");

		log.info("Login with username: " + propertyManager.getUserMhpcku1());
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
			ExtentTestManager.getTest().log(Status.FAIL, "1. Open Homepage and Verify Login FAILED");
		}

		assertSoftly.assertAll();
	}
	
	@Test(priority = 33, description = "2. Create Vehicle")
	public void createVehicle() {		
		
		searchVehiclesPage = mainPage.openAdministration()
		                             .openVehicles();
		
		createVehiclePage = searchVehiclesPage.clickCreate();
		searchAccountPage = createVehiclePage.selectFleetId();	
		
		
		searchAccountPage.searchByFleetIdDepotId(vehicleData.getFleetID(), vehicleData.getDepotID())		
		                 .clickFirstEntryInTable();
		
		createVehiclePage.setPressureUnitBar()
		                 .setVehicleTypeToBus()
		                 .setAxleConfiguration(vehicleData.getAxleConfiguration())
		                 .scroll("-130")
		                 .setTirePressure(vehicleData.getPressure())
		                 .setVehicleStatusToActive()
		                 .setInternalCustomerVehicleId(vehicleData.getInternalCustomerVehicleId())
		                 .setUserVehicleTypeYard()
		                 .setTempratureUnitFahrenheit()
		                 .setLicensePlateNumber(vehicleData.getLpn())
		                 .setLicensePlateNumberCountry(vehicleData.getLpnCountry())
		                 .setVehicleManufacturer(vehicleData.getMake())		                
		                 .setPayOption(vehicleData.getBilling());
			                 
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "2-createVehicle");   
							              
		createVehiclePage.scroll("-760")
		                 .clickOnSave();  
		
		boolean successfullyCreated = createVehiclePage.checkSuccessAlert();
		
        assertSoftly.assertTrue(successfullyCreated, "successfully created");
		if (successfullyCreated) {
			ExtentTestManager.getTest().log(Status.PASS, "2. Create Vehicle PASSED");			
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "2. Create Vehicle FAILED");			
		}
	
		assertSoftly.assertAll();
	}
	
	@Test(priority = 34, description = "3. Check Vehicle in MyFleet does not appear")
	public void checkVehicleInMtFleet() {
				
		driverManager.quitDriver();		
		runDriver();
		loginPage = new LoginPage(driver, wait);
			
		loginPage.openUrl(propertyManager.getContiUrl());
		log.info("login with username: " + propertyManager.getUserTcUserA());
		mainPage = loginPage.doLogin(propertyManager.getUserTcUserA(), propertyManager.getPassTcUserA());
		loginPage.openUrl(propertyManager.getTcUserAUrl());
		
		ExtentTestManager.getTest().log(Status.INFO, "Login proceeded.");
	 	
		// After login there may be a popup article which needs to be closed
		mainPage.closeArticleIfVisible();
		
		myFleetPage = mainPage.openContiConnect()
                              .openMyFleet();
		
		myFleetPage.filter(vehicleData.getInternalCustomerVehicleId(), vehicleData.getLpn());
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-filterForVehicle");

		assertSoftly.assertTrue(myFleetPage.getWebElementText(myFleetPage.getBody()).contains(NO_RESULTS_MESSAGE));

		boolean checkNoResults = myFleetPage.getWebElementText(myFleetPage.getBody()).contains(NO_RESULTS_MESSAGE);
		
		assertSoftly.assertTrue(checkNoResults, "no results found");
		if (checkNoResults) {
			ExtentTestManager.getTest().log(Status.PASS, "3. Vehicle Not Shown PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "3. Vehicle Not Shown FAILED");
		}
		
		assertSoftly.assertAll();
	}
}
