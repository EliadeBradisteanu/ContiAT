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

public class CreateRecipientPage extends BasePage<CreateRecipientPage> {
	
	@FindBy(css = "[ng-model*='recipientObject.firstName']")
	private WebElement firstName;
	
	@FindBy(css = "[ng-model*='recipientObject.lastName']")
	private WebElement lastName;
	
	@FindBy(css = "[ng-model*='recipientObject.mail']")
	private WebElement eMail;

	@FindBy(css = "[ng-model*='recipientObject.telNo']")
	private WebElement phoneNumber;
	
	@FindBy(css = "[ng-model*='recipientObject.country']")
	private WebElement recipientCountry;	
	
	@FindBys({ @FindBy(css = ".md-ink-ripple")})
	private List<WebElement> countryLanguageList;
	
	@FindBy(css = "[ng-model*='recipientObject.language']")
	private WebElement recipientLanguage;
	
	//the 2 check boxes,  issues and updates	
	@FindBy(css = ".md-container.md-ink-ripple")
	private List<WebElement> checkBoxes;
	
	//@FindBy(css = ".md-icon-button.md-button.md-ink-ripple")
	@FindBy(xpath = "//button[@aria-label='Créer destinataire']")	
	private WebElement saveButtonFr;
	
	@FindBy(css = ".md-icon-button.md-button.md-ink-ripple")	
	private WebElement saveButtonEn;
	
	@FindBy(xpath = "//button[@aria-label='Empfänger erstellen']")	
	private WebElement saveButtonDe;
	
	//for save confirmation dialog , size 7
	@FindBys({ @FindBy(css = ".md-button.md-ink-ripple")})
	private List<WebElement> confirmations;
	
	public CreateRecipientPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}
	
	public CreateRecipientPage enterFirstName(String firstName) {
		enterKeysIntoWebElement(Optional.of(this.firstName), firstName);	
	    return this;
	}
	
	public CreateRecipientPage enterLastName(String lastName) {
		enterKeysIntoWebElement(Optional.of(this.lastName), lastName);	
	    return this;
	}
	
	public CreateRecipientPage enterEMail(String eMail) {
		enterKeysIntoWebElement(Optional.of(this.eMail), eMail);	
	    return this;
	}
	
	public CreateRecipientPage enterPhoneNumber(String phoneNumber) {
		enterKeysIntoWebElement(Optional.of(this.phoneNumber), phoneNumber);	
	     return this;
	}
	
	public CreateRecipientPage selectCountry(String country) {
		clickElementIfPresent(recipientCountry);	
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(countryLanguageList.stream()
				                         .filter(x -> x.getText().contains(country))
				                         .findAny()
				                         .get()); 
		return this;
	}
	
	public CreateRecipientPage selectLanguage(String language) {
		clickElementIfPresent(recipientLanguage);		
		TimeUtil.sleep(1500, TimeUnit.MILLISECONDS);
		clickElementIfPresent(countryLanguageList.stream()
                .filter(x -> x.getText().contains(language))
                .findAny()
                .get()); 
		return this;
	}
	
	public CreateRecipientPage checkNotifyIssuesFlag() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);

		if (checkBoxes.size() == 9) {
			clickElementIfPresent(checkBoxes.get(8));
		} else if (checkBoxes.size() == 12) {
			clickElementIfPresent(checkBoxes.get(11));
		} else {
			clickElementIfPresent(checkBoxes.get(4));
		}
		return this;
	}

	public void checkNotifyUpdatesFlag() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);

		if (checkBoxes.size() == 9) {
			clickElementIfPresent(checkBoxes.get(7));
		} else if (checkBoxes.size() == 12) {
			clickElementIfPresent(checkBoxes.get(10));
		} else {
			clickElementIfPresent(checkBoxes.get(3));
		}
	}
	
	public void clickSaveFr() {	
		clickElementIfPresent(saveButtonFr);	
		TimeUtil.sleep(2, TimeUnit.SECONDS); 		
		clickElementIfPresent(confirmations.get(confirmations.size()-1));
	}
	
	public void clickSaveEn() {	
		clickElementIfPresent(saveButtonEn);		
		TimeUtil.sleep(2, TimeUnit.SECONDS);
		clickElementIfPresent(confirmations.get(confirmations.size()-1));
	}
	
	public void clickSaveDe() {	
		clickElementIfPresent(saveButtonDe);		
		TimeUtil.sleep(2, TimeUnit.SECONDS);
		clickElementIfPresent(confirmations.get(confirmations.size()-1));
	}

}
