package CONTI.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import CONTI.Test.utils.ScreenshotUtil;
import CONTI.Test.utils.extentReports.ExtentTestManager;
import CONTI.pages.CreateVehiclePage;
import CONTI.pages.GeneralDataVehiclePage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.MyFleetPage;
import CONTI.pages.SearchAccountPage;
import CONTI.pages.SearchVehiclesPage;
import CONTI.testdata.JsonDataHandlerVehicleData;
import CONTI.testdata.VehicleData;

public class ShowNoShowVehicleInPortalAdmin_14422 extends BaseTest {
	
	private static final String TESTNAME = "Show, No Show Vehicle In Portal Admin";
	private static final String VEHICLE_IN_PORTAL_ADMIN = "vehicleInPortalAdmin";	
	private static final String NO_RESULTS_MESSAGE = "No vehicles found matching the current filter criteria.";
	
	private Logger log = Logger.getLogger(ShowNoShowVehicleInPortalAdmin_14422.class);

	private SoftAssert assertSoftly = new SoftAssert();	
	private JsonDataHandlerVehicleData readVehicleData;
	private VehicleData vehicleData;

	private LoginPage loginPage;
	private MainPage mainPage;
	private SearchVehiclesPage searchVehiclesPage;
	private CreateVehiclePage createVehiclePage;
	private SearchAccountPage searchAccountPage;
	private MyFleetPage myFleetPage;
	private GeneralDataVehiclePage generalDataPage;
	
	@BeforeClass
	public void before() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Open Homepage and Verify Login"
                                       + "<br> 2. Create Vehicle"
                                       + "<br> 3. Check new vehicle in Administration"
                                       + "<br> 4. Check Vehicle in MyFleet does not appear");

		loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());

		try {
			readVehicleData = new JsonDataHandlerVehicleData();
		} catch (IOException e) {
			log.error("could not read data", e);
			e.printStackTrace();
		}
		vehicleData = readVehicleData.getVehicleData(VEHICLE_IN_PORTAL_ADMIN);
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
	
	@Test(priority = 35, description = "1. Open Homepage and Verify Login")
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
	
	@Test(priority = 36, description = "2. Create Vehicle")
	public void createVehicle() {		
		searchVehiclesPage = mainPage.openAdministration()
		                             .openVehicles();
		
		createVehiclePage = searchVehiclesPage.clickCreate();
		searchAccountPage = createVehiclePage.selectFleetId();	
		
		searchAccountPage.searchByFleetIdDepotId(vehicleData.getFleetID(), vehicleData.getDepotID())
		                 .clickFirstEntryInTable();
		
		createVehiclePage.setPressureUnitPsi()
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
			ExtentTestManager.getTest().log(Status.PASS, "2-Create Vehicle PASSED");			
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "2-Create Vehicle FAILED");			
		}
	
		assertSoftly.assertAll();
	}
	
	@Test(priority = 37, description = "3. Check new vehicle in Administration")
	public void checkVehicleInAdministration() {
		searchVehiclesPage = mainPage.openAdministration()
                                     .openVehicles();
		
		searchVehiclesPage.searchVehicleByLpnBepotID(vehicleData.getLpn(), vehicleData.getDepotID());
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-searchVehicle");
		
		generalDataPage = searchVehiclesPage.clickFirstEntryFromTable(vehicleData.getLpn());
		
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		
		//depending on the nr of target pressure the nr. of info fields changes
		int nrOfTargetPressure = generalDataPage.getAxleInformatios().size();
		
		result.put("FleetID", generalDataPage.getFleetId().equals(vehicleData.getFleetID()));
		result.put("depotID", generalDataPage.getDepotId().equals(vehicleData.getDepotID()));
		result.put("depotName", generalDataPage.getDepotName().equals(vehicleData.getDepotName()));
		result.put("status", generalDataPage.getStatus().equals(vehicleData.getStatus()));
		result.put("vehicleType", generalDataPage.getVehicleType().equals(vehicleData.getVehicleType()));
		result.put("axleConfiguration", generalDataPage.getAxleConfiguration().equals(vehicleData.getAxleConfiguration()));
		result.put("internalCustomerVehicleId", generalDataPage.getCustomerVehicleId().equals(vehicleData.getInternalCustomerVehicleId()));
		result.put("pressureUnit", generalDataPage.getPressureUnit(nrOfTargetPressure).equals(vehicleData.getPressureUnit()));
		result.put("temperatureUnit", generalDataPage.getTemperatureUnit(nrOfTargetPressure).equals(vehicleData.getTemperatureUnit()));
		result.put("lpn", generalDataPage.getLPN(nrOfTargetPressure).equals(vehicleData.getLpn()));
		result.put("pressure", generalDataPage.getAxleInformatios()
				                              .stream()
		                                      .allMatch(x -> x.equals(vehicleData.getPressure() + " " + vehicleData.getPressureUnit()))); 
		result.put("lpnCountry", generalDataPage.getLpnCountry(nrOfTargetPressure).equals(vehicleData.getLpnCountry()));
		result.put("make", generalDataPage.getMake(nrOfTargetPressure).equals(vehicleData.getMake()));
		result.put("billing", generalDataPage.getBilling(nrOfTargetPressure).equals(vehicleData.getBilling()));
	
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-vehiclePage");
		
		result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "3. Administration vehicle check: attribute " + k + " PASSED");			
			else
				ExtentTestManager.getTest().log(Status.PASS, "3. Administration vehicle check: attribute " + k + " FAILED");			
		});
		
		assertSoftly.assertAll();				
	}
	
	@Test(priority = 38, description = "4. Check Vehicle in MyFleet does not appear")
	public void checkVehicleInMtFleet() {	
		
		myFleetPage = mainPage.openContiConnect()
                              .openMyFleet();
		
		myFleetPage.filter(vehicleData.getInternalCustomerVehicleId(), vehicleData.getLpn());
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "4-filterForVehicle");
	
		assertSoftly.assertTrue(myFleetPage.getWebElementText(myFleetPage.getBody()).contains(NO_RESULTS_MESSAGE));

		boolean checkNoResults = myFleetPage.getWebElementText(myFleetPage.getBody()).contains(NO_RESULTS_MESSAGE);
		
		assertSoftly.assertTrue(checkNoResults, "no results found");
		if (checkNoResults) {
			ExtentTestManager.getTest().log(Status.PASS, "4. Vehicle Not Shown PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "4. Vehicle Not Shown FAILED");
		}
		
		assertSoftly.assertAll();
	}

}
