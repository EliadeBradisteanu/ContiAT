package CONTI.pages;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.TimeUtil;

public class SearchAccountPage extends BasePage<SearchAccountPage>{
	
	@FindBys({ @FindBy(className = "cpc-searchfield") })
	private List<WebElement> searchAccountsFields;
	
	@FindBy(xpath = "//*[@class='md-raised md-button md-ink-ripple']")	
	private WebElement accountSearchButton;
	
	@FindBy(css = ".vehicle-entry tr:nth-child(1)")
	private WebElement firstEntryInTable;
	
	public SearchAccountPage(WebDriver driver, WebDriverWait wait) {
		super(driver,wait);
	}

	// ----------- Search accounts by --------------

	public void enterFleetName(String searchString) {
		enterKeysIntoWebElement(Optional.of(this.searchAccountsFields.get(0)), searchString);
	}
	
	public SearchAccountPage enterFleetId(String searchString) {
		enterKeysIntoWebElement(Optional.of(this.searchAccountsFields.get(1)), searchString);
	    return this;
	}
	
	public void enterDepotName(String searchString) {
		enterKeysIntoWebElement(Optional.of(this.searchAccountsFields.get(2)), searchString);
	}
	
	public void enterDepotId(String searchString) {
		enterKeysIntoWebElement(Optional.of(this.searchAccountsFields.get(3)), searchString);
	}
	
	public void enterCity(String searchString) {
		enterKeysIntoWebElement(Optional.of(this.searchAccountsFields.get(4)), searchString);
	}
	
	public void enterSalesOrg(String searchString) {
		enterKeysIntoWebElement(Optional.of(this.searchAccountsFields.get(5)), searchString);
	}
	
	public void clickSearch() {
		clickElementIfPresent(accountSearchButton);	
	}
	
	public SearchAccountPage searchByFleeId(String fleetId) {		
		enterFleetId(fleetId);	
		clickSearch();	
		TimeUtil.sleep(2, TimeUnit.SECONDS);
		
		//sometimes the fleedId text is truncated so it has to be given again
		while(!getWebElementText(firstEntryInTable).contains(fleetId)) {
			enterFleetId(fleetId);	
			clickSearch();
		}		
		return this;
	}
	
	public SearchAccountPage searchByFleetIdDepotId(String fleetId, String depotId) {
		enterFleetId(fleetId);
		enterDepotId(depotId);
		clickSearch();
        TimeUtil.sleep(2, TimeUnit.SECONDS);
		
		//sometimes the fleedId text is truncated so it has to be given again
		while(!getWebElementText(firstEntryInTable).contains(fleetId)) {
			enterFleetId(fleetId);	
			clickSearch();
		}
		
		return this;
	}
	
	public SearchAccountPage searchByFleetIdAndDepotName(String fleetId, String depotName) {
		enterFleetId(fleetId);
		enterDepotName(depotName);
		clickSearch();
        TimeUtil.sleep(2, TimeUnit.SECONDS);
		
		//sometimes the fleedId text is truncated so it has to be given again
		while(!getWebElementText(firstEntryInTable).contains(fleetId)) {
			enterFleetId(fleetId);	
			clickSearch();
		}
		
		return this;
	}
	
	public void searchAndSelectFirstEntry(String fleetId, String depotId) {
		searchByFleetIdDepotId(fleetId, depotId);
		clickFirstEntryInTable();
	}
	
	
	public void clickFirstEntryInTable() {
		clickElementIfPresent(firstEntryInTable);
		// wait until fleetform no longer exists
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".vehicle-entry tr:nth-child(1)")));
			
	}
}
