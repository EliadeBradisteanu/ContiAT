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

public class ConfigurationPage extends BasePage<ConfigurationPage> {

	// Tabs:Sources and Recipients
	@FindBys({ @FindBy(css = ".fap-recipient-switch") })
	private List<WebElement> sourcesRecipientsTabs;
	
	@FindBy(css= ".tree-branch-head")
	private List<WebElement> plusSignOfFirstDepot;
	
	// ----------- Sources -------------- 
	
	@FindBy(css= ".cpc-searchfield")
	private WebElement searchFieldSources;
 	
	@FindBy(css=".tree-label")
	private List<WebElement> searchResultsTree;
	
	@FindBy(css=".fap-table-button.fap-recipient-list-add")
	private WebElement assignRecipientButton;

	// ----------- Recipients -------------- 	
	
	//size 6	
	//@FindBy(css = ".cpc-searchfield.ng-pristine.ng-untouched.ng-valid.ng-empty")
	@FindBy(tagName = "input")
	private List<WebElement> searchFieldRecipient;
	
	@FindBy(css = ".vehicle-row")
	private WebElement firstEntryInRecipientsSearchTable;
	
	@FindBys({ @FindBy(tagName = "button") })
	private List<WebElement> deleteFirstRecipient;
	
	//Search and Create Buttons
	@FindBys({ @FindBy(css = ".md-raised.md-button.md-ink-ripple") })
	private List<WebElement> searchCreateButtons;
   
	//for save confirmation dialog , size 7
    @FindBys({ @FindBy(css = ".md-button.md-ink-ripple")})
	private List<WebElement> confirmations;
    
    @FindBys({ @FindBy(css = ".fap-recipient-checkbox")})
   	private List<WebElement> recipientCheckBoxes;
  	
	public ConfigurationPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public ConfigurationPage openSources() {
		clickElementIfPresent(sourcesRecipientsTabs.get(0));
		return this;
	}

	public ConfigurationPage openRecipients() {
		clickElementIfPresent(sourcesRecipientsTabs.get(1));
		return this;
	}

	public ConfigurationPage searchSources(String sources) {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		wait.until(ExpectedConditions.elementToBeClickable(searchFieldSources));		
		searchFieldSources.clear();
		enterKeysIntoWebElement(Optional.of(searchFieldSources), sources);
	    pressEnter(searchFieldSources);
	    
	    return this;
	}
	
	public ConfigurationPage clickFirstSourceInTable() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(searchResultsTree.get(0));
		return this;
	}
	
	public ConfigurationPage clickFirstVehicleOfFirstSourceInTable() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(searchResultsTree.get(1));
		
		return this;
	}
	
	public ConfigurationPage clickVehicleInSourceTable(String vehicle) {
		TimeUtil.sleep(1, TimeUnit.SECONDS);		
		clickElementIfPresent(getWebElementFromListByText(searchResultsTree, vehicle));
		
		return this;
	}
	
	public AssignRecipientPage clickAssignRecipient() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(assignRecipientButton);
		return new AssignRecipientPage(driver, wait);
	}
	
	public ConfigurationPage searchRecipientsFr(String searchString) {		
		getWebElementFromListByAttribute(searchFieldRecipient, "placeholder", "Recherche les destinataires…");
		enterKeysIntoWebElement(
				Optional.ofNullable(getWebElementFromListByAttribute(searchFieldRecipient, "placeholder", "Recherche les destinataires…"))
				, searchString);
	
		clickElementIfPresent(searchCreateButtons.get(0));
	    return this;
	}	
	
	public ConfigurationPage searchRecipientsDe(String searchString) {		
		getWebElementFromListByAttribute(searchFieldRecipient, "placeholder", "Nach Empfänger suchen...");
		enterKeysIntoWebElement(
				Optional.ofNullable(getWebElementFromListByAttribute(searchFieldRecipient, "placeholder", "Nach Empfänger suchen..."))
				, searchString);
	
		clickElementIfPresent(searchCreateButtons.get(0));
	    return this;
	}
	
	public CreateRecipientPage clickCreateRecipients() {
		clickElementIfPresent(searchCreateButtons.get(1));		
		
		return new CreateRecipientPage(driver, wait);
	}
	
	public String getTextOfFirstRecipientEntry() {
		return getWebElementText(firstEntryInRecipientsSearchTable);
	}
	
	public ConfigurationPage deleteFirstRecipient() {	
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(deleteFirstRecipient.get(3));
	    TimeUtil.sleep(1, TimeUnit.SECONDS);
        clickElementIfPresent(confirmations.get(confirmations.size()-1));
        
        return this;
	}

	public ConfigurationPage clickPlusOfFirstDepot() {
		clickElementIfPresent(plusSignOfFirstDepot.get(0));
		
		return this;
	}	
	
	public void checkboxEmailAndSms() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(recipientCheckBoxes.get(0));
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(recipientCheckBoxes.get(1));
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(recipientCheckBoxes.get(7));
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(recipientCheckBoxes.get(8));		
	}
}
