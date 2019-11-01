package CONTI.Test;

import java.io.File;
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
import CONTI.pages.GeneralInfoMyFleetPage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.MyFleetPage;
import CONTI.pages.SearchAccountPage;
import CONTI.pages.SearchVehiclesPage;
import CONTI.testdata.JsonDataHandlerVehicleData;
import CONTI.testdata.VehicleData;

public class AdminHhtUpload_14578 extends BaseTest {

	private static final String TESTNAME = "HHT Upload";
	private static final String HHT_FILE_S2D4 = "hhtFiles/generic_HHT_S2_D4forMHP.asc";
	private static final String HHT_UPLOAD_S2D4 = "hhtUploadS2D4";	

	private Logger log = Logger.getLogger(AdminHhtUpload_14578.class);
	
	private SoftAssert assertSoftly = new SoftAssert();	
	private JsonDataHandlerVehicleData readVehicleData;
	private VehicleData vehicleData;

	private LoginPage loginPage;
	private MainPage mainPage;
	private SearchVehiclesPage searchVehiclesPage;
	private CreateVehiclePage createVehiclePage;
	private SearchAccountPage searchAccountPage;
	private MyFleetPage myFleetPage;
	private GeneralInfoMyFleetPage myFleetGenInfoPageN;

	@BeforeClass
	public void setup() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Open Homepage and Verify Login"
                                       + "<br> 2. Create Vehicle"
                                       + "<br> 3. check MyFleet for sensor values");

		loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());

		try {
			readVehicleData = new JsonDataHandlerVehicleData();
		} catch (IOException e) {
			log.error("could not read data", e);
			e.printStackTrace();
		}
		vehicleData = readVehicleData.getVehicleData(HHT_UPLOAD_S2D4);

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

	@Test(priority = 17, description = "1. Open Homepage and Verify Login")
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
			ExtentTestManager.getTest().log(Status.FAIL, "1. Open Homepage and Verify Login FAILED");
		}

		assertSoftly.assertAll();
	}

	@Test(priority = 18, description = "2. Create Vehicle")
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
                         .setTempratureUnitCelsius()
                         .setLicensePlateNumber(vehicleData.getLpn())
                         .setLicensePlateNumberCountry(vehicleData.getLpnCountry())
                         .setVehicleManufacturer(vehicleData.getMake())		                
                         .setPayOption(vehicleData.getBilling());
		
		File file = new File(getClass().getClassLoader().getResource(HHT_FILE_S2D4).getFile());
		createVehiclePage.uploadHTT(file.getPath());
		
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
	
	@Test(priority = 19, description = "3. check MyFleet for sensor values")
	public void checkMyFleet() {
		
		myFleetPage = mainPage.openContiConnect()
                              .openMyFleet();	
		
		myFleetGenInfoPageN = myFleetPage.filterCustomerVehicleId(vehicleData.getInternalCustomerVehicleId())
		                                .clickDetailsForFirstAlertInTable();
		
		boolean checkSensors = myFleetGenInfoPageN.getSensorAmount() == vehicleData.getSensorAmount();
			
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-checkHHTSensor");
		
		assertSoftly.assertTrue(checkSensors, "successfully created");
		if (checkSensors) {
			ExtentTestManager.getTest().log(Status.PASS, "3-Upload sensor information PASSED");		

		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "3-Upload sensor information FAILED");			
		}
		
		assertSoftly.assertAll();		
	}
}
