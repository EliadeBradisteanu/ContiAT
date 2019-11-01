/*package CONTI.Test.Old;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CONTI.pages.AdministrationPage;
import CONTI.pages.AlertPage;
import CONTI.pages.DashboardPage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.MyFleetPage;
import CONTI.pages.NotificationsPage;

public class ContiKSTest  {

	//private String URL = "https://www.conti-connect.de/";
	private String URL = "https://cocqa.conti.de/";
	private Logger log = Logger.getLogger(this.getClass());
	
	private SoftAssert assertSoftly = new SoftAssert();
	private static final String TESTNAME = "ContiKSTest";

	@BeforeTest(alwaysRun = true)
	public void setup() throws MalformedURLException {
		
	} 

	@AfterTest
	public void cleanUp() {
		driver.close();
		eReport.flush();
	}

	@Test(priority = 1, description = "1.  Webseite aufrufen und Login verifizieren")
	public void WebSeiteAufrufenundLoginVerifizieren() {
		ExtentTest eTest = eReport.startTest("1.Check Login");
		driver.get(URL);
		takeScreenshot(TESTNAME, "beforeLogin");
		LoginPage loginPage = new LoginPage(driver);

		String username = getTestData(2, 2, eTest);
		String password = getTestData(3, 2, eTest);
		log.info("login with username: " + username);
		loginPage.doLogin(username, password);

		eTest.log(LogStatus.INFO, "Login proceeded.");
		
		//Nach dem Login kommt auf der QA Umgebung (sporadisch) ein Artikel Dialog. if für Abfangen und schließen.
				boolean isarticlevisiblecheck = loginPage.isArticleVisibleCheck();
				if (isarticlevisiblecheck) {
					loginPage.closeArticle();
				} 
		// Check if correct user has been logged in
		boolean checkUsername = loginPage.getLoggedInUser().contains(username);
		takeScreenshot(TESTNAME, "afterLogin");
		assertSoftly.assertTrue(checkUsername);
		if (checkUsername) {
			eTest.log(LogStatus.PASS, "1. Webseite aufrufen und Login verifizieren PASSED");
		} else {
			eTest.log(LogStatus.FAIL, "1. Webseite aufrufen und Login verifizieren FAILED");
		}
		eReport.endTest(eTest);
		eReport.flush();
	}

	@Test(priority = 2, description = "2. Klappe Alert auf und Prüfe ob Einzelheiten angezeigt werden")
	public void erstenAlertaufklappen() {
		ExtentTest alertTest = eReport.startTest("2. Check Alert and Alert drop down.");

		AlertPage alertPage = new AlertPage(driver);
		alertPage.clickFirstAlertfromAlertsPage();
		
		boolean checkDisplayFirstTableEntry = alertPage.getFirstAlertsFirstSensorofFirstTableEntry().isDisplayed();
		
		assertSoftly.assertTrue(checkDisplayFirstTableEntry);

		if (checkDisplayFirstTableEntry) {
			alertTest.log(LogStatus.PASS, "2. Check Alert and Alert drop down PASSED");
		} else {
			alertTest.log(LogStatus.FAIL, "2. Check Alert and Alert drop down FAILED");
		}
		eReport.endTest(alertTest);
		eReport.flush();

	}

	@Test(priority = 3, description = "3. Check alert Detail")
	public void detailsdeserstenAlertsoeffnen() {
		ExtentTest detailAlertTest = eReport.startTest("3. Check alert Details");
		AlertPage alertPage = new AlertPage(driver);

		String fleetName = alertPage.getFirstAlertsFleetNamefromAlertsPage();
		
		alertPage.clickDetailsButtonfromFirstAlertonTable();

		String depotName = alertPage.getDepotNamefromSingleAlert();

		boolean checkDepotName = depotName.contains(fleetName);
		assertSoftly.assertTrue(checkDepotName);

		if (checkDepotName) {
			detailAlertTest.log(LogStatus.PASS, "3. Check alert Details PASSED");
		} else {
			detailAlertTest.log(LogStatus.FAIL, "3. Check alert Details FAILED");
		}

		eReport.endTest(detailAlertTest);
		eReport.flush();
	}

	@Test(priority = 4, description = "4. Check alert details diagram")
	public void PruefeobDiagramVorhanden() {
		ExtentTest detailAlertDiagramTest = eReport.startTest("4. Check alert details diagram");
		AlertPage alertPage = new AlertPage(driver);

		boolean checkHistoryChart = alertPage.getHistoryChart().isDisplayed();
		assertSoftly.assertTrue(checkHistoryChart);

		if (checkHistoryChart) {
			detailAlertDiagramTest.log(LogStatus.PASS, "4. Check alert details diagram PASSED");
		} else {
			detailAlertDiagramTest.log(LogStatus.FAIL, "4. Check alert details diagram FAILED");
		}

		eReport.endTest(detailAlertDiagramTest);
		eReport.flush();

	}

	@Test(priority = 5, description = "5. MyFleet check drop down")
	public void OeffneMyFleetundKlappeFZGauf() {
		ExtentTest myFleetTest = eReport.startTest("5. MyFleet check drop down");

		MainPage mainPage = new MainPage(driver);

		mainPage.openMyFleetArea();		

		MyFleetPage myFleetPage = new MyFleetPage(driver);
		myFleetPage.openFirstVehiclesAlertsArea();

		boolean checkVehicleStatus = myFleetPage.getVehicleStatus().isDisplayed();
		assertSoftly.assertTrue(checkVehicleStatus);

		if (checkVehicleStatus) {
			myFleetTest.log(LogStatus.PASS, "5. MyFleet check drop down PASSED");
		} else {
			myFleetTest.log(LogStatus.FAIL, "5. MyFleet check drop down FAILED");
		}

		eReport.endTest(myFleetTest);
		eReport.flush();

	}

	// TODO: add detail view for MyFleet

	@Test(priority = 6, description = "6. Dashboard check")
	public void OeffneDashboardUndWarte(){
		ExtentTest dashboardTest = eReport.startTest("6. Dashboard check");
		MainPage mainPage = new MainPage(driver);
		mainPage.openDashboardArea();

		DashboardPage dashboardPage = new DashboardPage(driver);
		long startTime = System.currentTimeMillis();

		dashboardPage.openDepotDropDown();
		long endTime = System.currentTimeMillis();

		boolean checkDepotIsDisplayed = dashboardPage.getFirstDepotFromDropDown().isDisplayed();
		assertSoftly.assertTrue(checkDepotIsDisplayed);

		if (checkDepotIsDisplayed) {
			dashboardTest.log(LogStatus.PASS, "Dashboard check PASSED");
		} else {
			dashboardTest.log(LogStatus.FAIL, "Dashboard check FAILED");
		}
		if (endTime - startTime > 30000) {
			dashboardTest.log(LogStatus.WARNING, "Loading Time exceeds 30s [" + (endTime - startTime) + "]");
		}

		eReport.endTest(dashboardTest);
		eReport.flush();
	}

	//@Test(priority = 7, description = "7. Öffne Notification und Suche nach Sources und Recipients")
	public void OeffneNotificationundSuche() {
		ExtentTest notificationTest = eReport.startTest("7. Notification and Recipients check");

		MainPage mainPage = new MainPage(driver);
		mainPage.openNotificationsArea();

		// Check source
		NotificationsPage notificationsPage = new NotificationsPage(driver);
		String source = getTestData(4, 2, notificationTest);

		notificationsPage.search(source);

		notificationsPage = new NotificationsPage(driver);

		// Check if searching returns search results
		boolean checkFirstDepotDisplayed = notificationsPage.getFirstDepotFromDropDown().isDisplayed();
		assertSoftly.assertTrue(checkFirstDepotDisplayed);

		if (checkFirstDepotDisplayed) {
			notificationTest.log(LogStatus.PASS, "OeffneNotificationundSucheSource PASSED");
		} else {
			notificationTest.log(LogStatus.FAIL, "OeffneNotificationundSucheSource FAILED");
		}

		// Check searching for people
		notificationsPage.openRecipients();

		String name = getTestData(5, 2, notificationTest);
		notificationsPage.search(name);

		notificationsPage = new NotificationsPage(driver);

		String nameOfFirstResult = notificationsPage.getNameOfFirstResult();
		boolean checkNameOfFirstResult = nameOfFirstResult.contains(name);
		assertSoftly.assertTrue(checkNameOfFirstResult);
		if (checkNameOfFirstResult) {
			notificationTest.log(LogStatus.PASS, "OeffneNotificationundSuchePerson PASSED");
		} else {
			notificationTest.log(LogStatus.FAIL, "OeffneNotificationundSuchePerson FAILED");
		}

		eReport.endTest(notificationTest);
		eReport.flush();

	}

	//@Test(priority = 8, description = "8. Öffne Administration und Suche dort")
	public void OeffneAdministrationundSuche() {
		ExtentTest administrationTest = eReport.startTest("8. Administration check");
		MainPage mainPage = new MainPage(driver);
		mainPage.openAdministration();

		AdministrationPage administrationPage = new AdministrationPage(driver);

		// Search for LPN, show 50 results and check first entry
		String lpn = getTestData(6, 2, administrationTest);

		administrationPage.searchLPN(lpn);
		administrationPage.showFiftyResultsinList();

		boolean checkFirstEntry = administrationPage.validateFirstEntryInTable();
		assertSoftly.assertTrue(checkFirstEntry);

		if (checkFirstEntry) {
			administrationTest.log(LogStatus.PASS, "Administration vehicle search PASSED");
		} else {
			administrationTest.log(LogStatus.FAIL, "Administration vehicle search FAILED");
		}

		administrationPage.openAdminAccounts();

		// search for sales org and check first entry
		String salesOrg = getTestData(7, 2, administrationTest);
		administrationPage.searchSalesOrg(salesOrg);

		checkFirstEntry = administrationPage.validateFirstEntryInTable();

		assertSoftly.assertTrue(checkFirstEntry);

		if (checkFirstEntry) {
			administrationTest.log(LogStatus.PASS, "Administration account search PASSED");
		} else {
			administrationTest.log(LogStatus.FAIL, "Administration account search FAILED");
		}

		// search for serial number and check first entry
		administrationPage.openAdminTelematicsBoxes();
		String serialNumber = getTestData(8, 2, administrationTest);
		administrationPage.searchTelematicsBoxes(serialNumber);

		checkFirstEntry = administrationPage.validateFirstEntryInTable();

		assertSoftly.assertTrue(checkFirstEntry);
		if (checkFirstEntry) {
			administrationTest.log(LogStatus.PASS, "Administration Telematic Boxes search PASSED");
		} else {
			administrationTest.log(LogStatus.FAIL, "Administration Telematic Boxes search FAILED");
		}

		// search for SIM ID in yard readers and check first entry
		administrationPage.openAdminYardReaders();
		String simID = getTestData(9, 2, administrationTest);

		administrationPage.searchYardReaders(simID);

		checkFirstEntry = administrationPage.validateFirstEntryInTable();

		assertSoftly.assertTrue(checkFirstEntry);
		if (checkFirstEntry) {
			administrationTest.log(LogStatus.PASS, "OeffneAdministrationundSuche PASSED");
		} else {
			administrationTest.log(LogStatus.FAIL, "OeffneAdministrationundSuche FAILED");
		}

		eReport.endTest(administrationTest);
		eReport.flush();
	}
}
*/