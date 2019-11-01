package CONTI.pages;

import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage<LoginPage>{
	
	@FindBy(id = "username")
	private WebElement username;

	@FindBy(id = "password")
	private WebElement password;
	
	@FindBy(id = "login_btn")
	private WebElement loginButton;
	
	public LoginPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);		
	}
	
	public LoginPage enterUsername(String username) {
		enterKeysIntoWebElement(Optional.of(this.username), username);
		return this;
	}
	
	public LoginPage enterPasword(String password) {
		enterKeysIntoWebElement(Optional.of(this.password), password);		
		return this;
	}
	
	public MainPage clickLogin() {
		clickElementIfPresent(loginButton);
		return new MainPage(driver, wait);		
	}
	
	public MainPage doLogin(String username, String password) {
		wait.until(ExpectedConditions.visibilityOfAllElements(this.username,this.password, loginButton));
		return enterUsername(username)
				.enterPasword(password)
				.clickLogin();
	}

}
