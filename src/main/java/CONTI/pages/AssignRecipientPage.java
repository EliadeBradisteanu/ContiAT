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

public class AssignRecipientPage extends BasePage<ConfigurationPage>{

	@FindBy(xpath= ".//*[@class='cpc-dialog _md md-transition-in']/.//*[@class='fap-search-block']/cpc-search-input/div/input")//Placeholder:Recherche les destinataires… //Trouver des véhicules ou des destinataires …
	private WebElement searchField;  
	
	@FindBy(xpath = ".//*[@class='cpc-dialog _md md-transition-in']/.//*[@class='vehicle-entry ng-scope']/tr[1]")	
	//@FindBy(css = ".vehicle-entry tr td")
	private WebElement firstEntryInSearchedRecipientsTable;	
	
	@FindBys({ @FindBy(css = ".vehicle-row") })
	private List<WebElement> vehicleRows;
	
	@FindBy(css = ".md-icon-button")
	private WebElement closeButton;		
	
	public AssignRecipientPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}
	
	public AssignRecipientPage search(String recipient) {
		enterKeysIntoWebElement(Optional.of(searchField), recipient);
		pressEnter(searchField);
		return this;
	}
	
	public AssignRecipientPage assingSearchedRecipient(String recipientFirstName) {		
		TimeUtil.sleep(2, TimeUnit.SECONDS);				
		clickElementIfPresent(getWebElementFromListByText(vehicleRows, recipientFirstName));		
		
		return this;
	}
}
