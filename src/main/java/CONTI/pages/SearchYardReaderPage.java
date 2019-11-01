package CONTI.pages;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.TimeUtil;

public class SearchYardReaderPage extends BasePage<SearchYardReaderPage>{
	
	@FindBys({ @FindBy(css = ".cpc-searchfield") })
	private List<WebElement> searchYardReader;	
		
	@FindBy(css = "#select_1")
	private WebElement yardReaderStatus;
	
	@FindBy(css = "#select_option_11")
	private WebElement statusActive;
	
	@FindBy(css = "#select_option_12")
	private WebElement statusInactive;		
		
	@FindBy(css = ".md-raised.md-button.md-ink-ripple")
	private WebElement yardReaderSearchButton;
	
	@FindBy(css = ".vehicle-row.ng-scope td")
	private WebElement firstEntryinTable;
	
	public SearchYardReaderPage(WebDriver driver, WebDriverWait wait) {
		super(driver,wait);
	}
	
	public String getYardReaderId() {
		return getAttribute("value", searchYardReader.get(0));
	}	
	
	public void enterYardReaderId(String searchString) {
		enterKeysIntoWebElement(Optional.of(searchYardReader.get(0)), searchString);
	}
	
	public String getFleetId() {
		return getAttribute("value", searchYardReader.get(1));
	}
	
	public void enterFleeId(String searchString) {
		enterKeysIntoWebElement(Optional.of(searchYardReader.get(1)), searchString);
	}
	
	public String getDepotId() {
		return getAttribute("value", searchYardReader.get(3));
	}
	
	public void enterDepotId(String searchString) {
		enterKeysIntoWebElement(Optional.of(searchYardReader.get(3)), searchString);
	}
		
	public String getStatus() {
		 return getWebElementText(yardReaderStatus);		
	}	
	
	public void setYardReaderStatusToActive() {
		clickElementIfPresent(yardReaderStatus);
		clickElementIfPresent(statusActive);	
	}
	
	public void setYardReaderStatusToInactive() {
		clickElementIfPresent(yardReaderStatus);
		clickElementIfPresent(statusInactive);			
	}			
	
	public SearchYardReaderPage searchYardReaders(String searchString) {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		enterYardReaderId(searchString);
		clickElementIfPresent(yardReaderSearchButton);
		
		return this;
	}
	
	public GeneralDataYardReaderPage clickFirstEntry() {
		clickElementIfPresent(firstEntryinTable);
		
		return new GeneralDataYardReaderPage(driver, wait);
	}

}
