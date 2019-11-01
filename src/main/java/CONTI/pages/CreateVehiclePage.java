package CONTI.pages;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.TimeUtil;

public class CreateVehiclePage extends BasePage<CreateVehiclePage> {

	@FindBy(name = "fleetCustomer")
	private WebElement fleetId;

	@FindBy(name = "fleetCustomerName")
	private WebElement fleetCustomerName;

	@FindBy(name = "depot")
	private WebElement depotId;

	@FindBy(name = "depotName")
	private WebElement depotName;

	@FindBy(name = "vehicleStatus")
	private WebElement vehicleStatusDropDown;
	
	@FindBy(css = "#select_option_50")
	private WebElement vehicleStatusActive;

	@FindBy(name = "type")
	private WebElement vehicleType;

	@FindBy(css = "#select_option_52")
	private WebElement vehicleTypeBus;

	@FindBy(name = "axleConfiguration")
	private WebElement axleConfiguration;

	@FindBy(css = ".md-select-menu-container.md-active.md-clickable")
	private WebElement axleSelector;

	@FindBy(tagName = "md-option")
	private List<WebElement> axleConfigurationList;

	@FindBy(css = "[ng-model*='newVehicle.customerVehicleId']")
	private WebElement internalCustomerVehicleId;

	@FindBy(name = "cpcContractType")
	private WebElement userVehicleType;

	@FindBy(css = "#select_option_17")
	private WebElement userVehicleTypeYardVehicle;

	@FindBy(name = "pressureUnit")
	private WebElement pressureUnit;

	@FindBy(css = "#select_option_29")
	private WebElement pressureUnitBar;

	@FindBy(css = "#select_option_30")
	private WebElement pressureUnitPsi;
	 
	@FindBy(name = "temperatureUnit")
	private WebElement temperatureUnit;

	@FindBy(css = "#select_option_33")
	private WebElement temperatureUnitCelsius;

	@FindBy(css = "#select_option_34")
	private WebElement temperatureUnitFahrenheit;

	@FindBy(css = "[ng-model*='newVehicle.lpn']")
	private WebElement licensePlateNumber;

	@FindBy(css = "#select_38")
	private WebElement countrySelector;

	@FindBy(tagName = "md-option")
	private List<WebElement> countryList;

	@FindBy(css = "#select_40")
	private WebElement vehicleManufacturer;

	@FindBy(xpath = ".//*[@class='md-select-menu-container md-active md-clickable']")
	private WebElement makeSelector;

	@FindBy(tagName = "md-option")
	private List<WebElement> makeList;

	@FindBy(name = "payOption")
	private WebElement payOption;

	@FindBy(css = "#select_option_344")
	private WebElement payOptionShowInPortalAndPay;
	
	@FindBy(css = "#select_option_346")
	private WebElement payOptionNoShowNoPay;
	
	@FindBy(tagName = "md-option")
	private List<WebElement> billings;
	
	

	// ------------------TIRE PREASSURE--------------------
	
	@FindBy(css = ".fap-bird-view-click-capture")
	private List<WebElement> addTargetPressures;
	
	@FindBy(css = "#d3js-input-dialog-text")
	private WebElement inputDialogPressure;
	
	@FindBy(css = "#d3js-input-dialog-button")
	private WebElement okButton;
	
	// ------------------SAVE DIALOGS--------------------
	
	@FindBy(css = ".md-icon-button.md-button.md-ink-ripple")
	private WebElement saveVehicleButton;
	
	@FindBy(css = ".fap-modal-primary")
	private WebElement saveConfirmation;

	// ------ HHT Upload -------------

	@FindBy(xpath = "//input[@type='file']")
	private WebElement hhtInput;

	@FindBy(xpath = "//*[@role='alert' and contains(text(),'Successfully saved')]")
	private WebElement successMessage;

	public CreateVehiclePage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}
	
	public SearchAccountPage selectFleetId() {		
		clickElementIfPresent(fleetId);
		return new SearchAccountPage(driver, wait);
	}

	public CreateVehiclePage setVehicleStatusToActive() {
		clickElementIfPresent(vehicleStatusDropDown);		
		clickElementIfPresent(vehicleStatusActive);
		return this;
	}

	public CreateVehiclePage setVehicleTypeToBus() {
		clickElementIfPresent(vehicleType);
		clickElementIfPresent(vehicleTypeBus);
		return this;
	}

	public CreateVehiclePage setAxleConfiguration(String axleConfiguration) {
		clickElementIfPresent(this.axleConfiguration);
		wait.until(ExpectedConditions.attributeToBe(axleSelector, "aria-hidden", "false"));
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(axleConfigurationList.stream().filter(x -> x.getText().trim().equals(axleConfiguration))
				.findAny().get());

		return this;
	}

	public CreateVehiclePage setInternalCustomerVehicleId(String internalCustomerVehicleId) {	
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		enterKeysIntoWebElement(Optional.of(this.internalCustomerVehicleId), internalCustomerVehicleId);

		return this;
	}

	public CreateVehiclePage setUserVehicleTypeYard() {
		clickElementIfPresent(userVehicleType);
		clickElementIfPresent(userVehicleTypeYardVehicle);
		return this;
	}

	public CreateVehiclePage setPressureUnitBar() {
		clickElementIfPresent(pressureUnit);
		clickElementIfPresent(pressureUnitBar);
		return this;
	}

	public CreateVehiclePage setPressureUnitPsi() {
		clickElementIfPresent(pressureUnit);
		clickElementIfPresent(pressureUnitPsi);
		return this;
	}

	public CreateVehiclePage setTempratureUnitCelsius() {
		clickElementIfPresent(temperatureUnit);
		clickElementIfPresent(temperatureUnitCelsius);
		return this;
	}

	public CreateVehiclePage setTempratureUnitFahrenheit() {
		clickElementIfPresent(temperatureUnit);
		clickElementIfPresent(temperatureUnitFahrenheit);
		return this;
	}

	public CreateVehiclePage setLicensePlateNumber(String lpn) {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		enterKeysIntoWebElement(Optional.of(licensePlateNumber), lpn);
		return this;
	}

	public CreateVehiclePage setLicensePlateNumberCountry(String lpnCountry) {
		clickElementIfPresent(countrySelector);
		wait.until(ExpectedConditions.attributeToBe(countrySelector, "aria-expanded", "true"));
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(
				countryList.stream().filter(x -> x.getText().trim().equals(lpnCountry)).findAny().get());

		return this;
	}

	public CreateVehiclePage setVehicleManufacturer(String make) {
		clickElementIfPresent(vehicleManufacturer);
		wait.until(ExpectedConditions.attributeToBe(makeSelector, "aria-hidden", "false"));
		makeList.stream().filter(x -> x.getText().contains(make)).findAny().get().click();

		return this;
	}
	
	public CreateVehiclePage setPayOption(String billing) {
		clickElementIfPresent(payOption);	
		wait.until(ExpectedConditions.attributeToBe(axleSelector, "aria-hidden", "false"));
		clickElementIfPresent(getWebElementFromListByText(billings, billing));

		return this;		
	}
	
	public CreateVehiclePage setTirePressure(String pressure) {
		addTargetPressures.stream()
		                  .forEach(addPressure -> {			                	 
		                	  clickWebElementToOffset(addPressure, 0, -10); 		                	 
		                	  enterKeysIntoWebElement(Optional.of(inputDialogPressure), pressure);
		              		  clickElementIfPresent(okButton);
		                  });  		
		return this;
	}

	
	public CreateVehiclePage clickOnSave() {
		clickElementIfPresent(saveVehicleButton);
		clickElementIfPresent(saveConfirmation);
		
		return this;
	}
	
	public boolean checkSuccessAlert() {
		return isWebElementDisplayed(successMessage);
	}

	public void uploadHTT(String path) {
		hhtInput.sendKeys(path);
	}
}
