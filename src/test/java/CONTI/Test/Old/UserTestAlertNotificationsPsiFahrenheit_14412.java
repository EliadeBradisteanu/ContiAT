/*package CONTI.Test.Old;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CONTI.pages.NotificationsPage;
import CONTI.Test.AbstractContiTests;
import CONTI.pages.AssignRecipientPage;
import CONTI.pages.CreateRecipientsPage;
import CONTI.pages.CreateVehiclePage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.MyFleetPage;
import CONTI.pages.MyFleetVehiclePage;
import CONTI.pages.VehiclePage;
import CONTI.testdata.JsonDataReaderUsers;
import CONTI.testdata.JsonDataHandlerRecipientsData;
import CONTI.testdata.RecipientsData;

public class UserTestAlertNotificationsPsiFahrenheit_14412 extends AbstractContiTests {

	private String URL = "https://cocqa.conti.de/";
	private Logger log = Logger.getLogger(this.getClass());

	private SoftAssert assertSoftly = new SoftAssert();
	private static final String TESTNAME = "DeleteRecipientWithoutAssignmentTest";

	private JsonDataReaderUsers readUserData;
	private JsonDataHandlerRecipientsData readRecipientsData;

	private RecipientsData recipientsData;
	private RecipientsData recipientsDataSecondUser;

	@BeforeTest(alwaysRun = true)
	public void setup() throws MalformedURLException {
		testSetup(TESTNAME);
		readUserData = new JsonDataReaderUsers();
		try {
			readRecipientsData = new JsonDataHandlerRecipientsData();
		} catch (IOException e) {
			log.error("could not read data", e);
			e.printStackTrace();
		}
		recipientsData = readRecipientsData.getRecipientsData("recipient4");
		recipientsDataSecondUser = readRecipientsData.getRecipientsData("recipient5");
	}

	@AfterTest
	public void cleanUp() {
		MainPage mainPage = new MainPage(driver);
		//mainPage.logout();
		//driver.close();
		eReport.flush();
	}

	@Test(priority = 1, description = "1.  Webseite aufrufen und Login verifizieren")
	public void WebSeiteAufrufenundLoginVerifizieren() {
		ExtentTest eTest = eReport.startTest("1.Check Login");
		driver.get(URL);
		takeScreenshot(TESTNAME, "1-beforeLogin");
		LoginPage loginPage = new LoginPage(driver);

		String username = "TC_User_B4";
		String password = readUserData.getUserPassword("TC_User_B4");
		log.info("login with username: " + username);
		loginPage.doLogin(username, password);

		eTest.log(LogStatus.INFO, "Login proceeded.");
			
		// Check if correct user has been logged in
		boolean checkUsername = loginPage.getLoggedInUserTCUserB4().contains(username);
		takeScreenshot(TESTNAME, "1-afterLogin");

		assertSoftly.assertTrue(checkUsername, "checkusername");
		if (checkUsername) {
			eTest.log(LogStatus.PASS, "1. Webseite aufrufen und Login verifizieren PASSED");
		} else {
			eTest.log(LogStatus.FAIL, "1. Webseite aufrufen und Login verifizieren FAILED");
		}
		eReport.endTest(eTest);
		eReport.flush();
		assertSoftly.assertAll();
	}

	
	@Test(priority = 2, description = "2. Open MyFleet and search vehicle")
	public void MyFleetOeffnenUndFzgSuchen() {
		ExtentTest eTest = eReport.startTest("1.Check Login");
		
		
	}
	
	
	@Test(priority = 3, description = "3. Upload HTT File")
	public void UploadHHTFile() {
		ExtentTest eTest = eReport.startTest("1.Check Login");
		CreateVehiclePage createVehiclePage = new CreateVehiclePage(driver);
		
		createVehiclePage.uploadHTT(System.getProperty("user.dir") + ".\\Resourcen\\hhtFiles\\generic_HHT_S2_D4forMHP.asc");
	
	}
	
	@Test(priority = 4, description = "4. Create Recipient for Assignment")
	public void openCreateVehicleDialog() {
		ExtentTest createRecipientTest = eReport.startTest("4. Create Recipient Check");
		MainPage mainPage = new MainPage(driver);
		mainPage.openNotificationsAreaCss();
		
		NotificationsPage notificationsPage = new NotificationsPage(driver);
		CreateRecipientsPage createRecipientsPage = new CreateRecipientsPage(driver);

		notificationsPage.openRecipients();
		
		notificationsPage.clickCreateRecipientsButton();
		
		createRecipientsPage.setRecipientFirstName(recipientsData.getFirstName());
		createRecipientsPage.setRecipientLastName(recipientsData.getLastName());
		createRecipientsPage.setRecipientMail(recipientsData.getEmail());
		createRecipientsPage.setRecipientPhoneNumber(recipientsData.getTelNo());
		createRecipientsPage.setRecipientCountry(recipientsData.getCountry());
		createRecipientsPage.setRecipientLanguage(recipientsData.getLanguage());
		createRecipientsPage.setRecipientNotifyIssuesFlag();
		createRecipientsPage.setRecipientNotifyUpdatesFlag();
		
		takeScreenshot(TESTNAME, "4-createrecipientbeforesaving");

		createRecipientsPage.clickOnSaveDe();
		
		takeScreenshot(TESTNAME, "4-createrecipientaftersaving");
		
		notificationsPage.search(recipientsData.getFirstName());
		
		String validator = recipientsData.getFirstName() +" " + recipientsData.getLastName();
		
		assertSoftly.assertEquals(notificationsPage.getNameOfFirstResult(), validator);

		boolean successfullyCreated = notificationsPage.checkSuccessCreation();


		if (successfullyCreated) {
			createRecipientTest.log(LogStatus.PASS, "4-Create Recipient PASSED");
		} else {
			createRecipientTest.log(LogStatus.FAIL, "4-Create Recipient FAILED");
		}		

		eReport.endTest(createRecipientTest);
		eReport.flush();
		assertSoftly.assertAll();
	}
	
	@Test(priority = 5, description = "5. Assign Recipient")
	public void assignRecipient() {
		ExtentTest assignRecipient = eReport.startTest("5. Assign Recipient Check");
		MainPage mainPage = new MainPage(driver);
		

		NotificationsPage notificationsPage = new NotificationsPage(driver);
		AssignRecipientPage assignRecipientPage = new AssignRecipientPage(driver);
		mainPage.openNotificationsAreaCss();
		
		notificationsPage.openSources();
		
		notificationsPage.clickFirstSourceInTable();
		assignRecipientPage.clickAssignButton();

		assignRecipientPage.search(recipientsData.getFirstName());
	
		assignRecipientPage.assignSearchedRecipient();
		
		takeScreenshot(TESTNAME, "5-assignRecipient");
	
		mainPage.logout();

		eReport.endTest(assignRecipient);
		eReport.flush();

		assertSoftly.assertAll();
	}
}
*/