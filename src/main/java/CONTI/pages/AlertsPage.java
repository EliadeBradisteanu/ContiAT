package CONTI.pages;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.TimeUtil;

public class AlertsPage extends BasePage<MainPage> {

	// Export, Print to-do list, Filter
	@FindBys({ @FindBy(className = "fap-toolbar-button") })
	private List<WebElement> filterButtons;

	@FindBy(css = "#filter-customerVehicleId")
	private WebElement filterCustomerVehicleId;

	@FindBy(css = ".fap-table-button")
	private WebElement detailsButtonFirstTableEntry;	

	@FindBy(css = ".vehicle-entry tr:nth-child(1)")
	WebElement firstEntryInAlertTable;

	@FindBy(xpath = ".//*[@class='fap-vehicle-list fap-vehicle-overview-list fap-alert-list ng-scope ng-table']/tbody[1]/tr[2]/td/div/div/sensor-status-table/span/table/tbody/tr[1]")
	private WebElement firstSensorofFirstTableEntry;

	@FindBy(css = ".vehicle-detail-table tbody tr:nth-child(1)")
	WebElement firstSensorOfFirstAlertTableEntry;

	@FindBy(css = ".vehicle-entry tr:nth-child(1) td:nth-child(6) div:nth-child(2)")
	private WebElement fleetIdfromAlertsPage;

	@FindBy(css = ".vehicle-masterdata-list tbody:nth-child(1) tr:nth-child(2)")
	private WebElement depotNamefromSingleAlert;

	public AlertsPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}
	
	public void pressFilterButton() {
		clickElementIfPresent(filterButtons.get(2));		
	}
	
	public void pressExportButton() {
		clickElementIfPresent(filterButtons.get(0));		
	}

	public void filterPublicVehicleIdOnly(String CustomerVehicleId) {
		pressFilterButton();
		enterKeysIntoWebElement(Optional.of(filterCustomerVehicleId), CustomerVehicleId);
		clickWebElementToOffset(filterCustomerVehicleId, 0, -20);
	}

	public GeneralInfoAlertsPage clickDetailsForFirstAlertInTable() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(detailsButtonFirstTableEntry);
		return new GeneralInfoAlertsPage(driver, wait);
	}

	public void clickFirstAlertfromAlertsTable() {
		clickElementIfPresent(firstEntryInAlertTable);
	}

	public WebElement getFirstAlertsFirstSensorOfFirstTableEntry() {
		wait.until(ExpectedConditions.elementToBeClickable(firstSensorOfFirstAlertTableEntry));
		return firstSensorOfFirstAlertTableEntry;
	}

	public String getFirstAlertsFleetIdfromAlertsPage() {
		return fleetIdfromAlertsPage.getText();
	}

	public String getDepotNamefromSingleAlert() {
		return depotNamefromSingleAlert.getText();
	}

}
