package CONTI.pages;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchVehiclesPage extends BasePage<SearchVehiclesPage>{
	
	@FindBys({ @FindBy(className = "cpc-searchfield") })
	private List<WebElement> searchVehicles;
	
	@FindBy(css = ".md-raised.md-button.md-ink-ripple")
	private WebElement vehiclesSearchButton;
	
	@FindBy(css = "[href='#/vehicles/vehiclescreateview']")
	private WebElement createVehicleButton;
	
	@FindBys({ @FindBy(css = ".vehicle-row.ng-scope td") })
	private List<WebElement> vehicleRows;
	
	public SearchVehiclesPage(WebDriver driver, WebDriverWait wait) {
		super(driver,wait);
	}
	
	public CreateVehiclePage clickCreate() {		
		clickElementIfPresent(createVehicleButton);
		return new CreateVehiclePage(driver, wait);
	}
	
	// ----------- Search vehicles by --------------

		public void enterLpn(String searchString) {
			enterKeysIntoWebElement(Optional.of(this.searchVehicles.get(0)), searchString);
		}

		public void enterCustVehicleId(String searchString) {
			enterKeysIntoWebElement(Optional.of(this.searchVehicles.get(1)), searchString);
		}

		public void enterDepotName(String searchString) {
			enterKeysIntoWebElement(Optional.of(this.searchVehicles.get(2)), searchString);
		}

		public void enterDepoId(String searchString) {
			enterKeysIntoWebElement(Optional.of(this.searchVehicles.get(3)), searchString);
		}

		public void enterFleetName(String searchString) {
			enterKeysIntoWebElement(Optional.of(this.searchVehicles.get(4)), searchString);
		}

		public void enterFleetId(String searchString) {
			enterKeysIntoWebElement(Optional.of(this.searchVehicles.get(5)), searchString);
		}
		
		public void searchVehicleByLpnBepotID(String lpn, String depotId) {
			enterLpn(lpn);
			enterDepoId(depotId);
			clickElementIfPresent(vehiclesSearchButton);
		}
		
		public GeneralDataVehiclePage clickFirstEntryFromTable(String lpn) {
			// TODO: with filter
			clickElementIfPresent(vehicleRows.stream().filter(el -> el.getText().contains(lpn)).findFirst().get());

			return new GeneralDataVehiclePage(driver, wait);
		}

}
