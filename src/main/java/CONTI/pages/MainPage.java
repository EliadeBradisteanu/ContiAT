package CONTI.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.TimeUtil;

public class MainPage extends BasePage<MainPage> {

	@FindBy(className = "fancybox-item")
    private WebElement articleCloseButton;

	@FindBy(className = "fancybox-skin")
	private WebElement article;

	@FindBy(className = "ci-header-compressed-userInfo")
	private WebElement loggedInUser;
	
	@FindBy(className = "ci-hidden-sticky")
	private List<WebElement> loggedInUserContiConnect;

	@FindBy(className = "ci-logout")
	private WebElement logOutButton;
	
	// ----------- Navigation Options MainPage--------------

	@FindBy(css = ".ci-plain-list li:nth-child(1)")
	private WebElement contiConnectButton;

	@FindBy(css = ".ci-plain-list li")
	private List<WebElement> contiConnectButtonFleetPortal;

	@FindBy(css = ".ci-plain-list li:nth-child(2)")
	private WebElement administrationButton;
	

	// ----------- Navigation Options ContiConnect--------------
	
	@FindBys({ @FindBy(css = ".fap-submenu li") })
	private List<WebElement> fapSubmenuButtons;

	
	// ----------- Navigation Options Administration--------------

	@FindBy(css = ".fap-submenu li:nth-child(1)")
	private WebElement vehicles;

	@FindBy(css = ".fap-submenu li:nth-child(2)")
	private WebElement accounts;

	@FindBy(css = ".fap-submenu li:nth-child(3)")
	private WebElement telematicsBoxes;

	@FindBy(css = ".fap-submenu li:nth-child(4)")
	private WebElement yardReaders;

	public MainPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public void closeArticleIfVisible() {
		if (isWebElementDisplayed(article)) {
			clickElementIfPresent(articleCloseButton);
			// wait until popup is closed
			wait.until(ExpectedConditions.attributeToBe(body, "class", "ci-bu-plt"));
		}
	}

	public String getLoggedInUser() {
		return loggedInUser.getText();
	}
	
	public String getConntiConnectLoggedInUser() {		
		return loggedInUserContiConnect.get(2).getText();
	}

	public MainPage doLogout() {
		// wait for green notification to disapear
		TimeUtil.sleep(4, TimeUnit.SECONDS);
		clickElementIfPresent(logOutButton);
		
		return this;
	}
	
	
	// ----------- Navigation Options MainPage--------------

	public MainPage openContiConnect() {
		clickElementIfPresent(contiConnectButton);
		return this;
	}
	
	public MainPage openContiConnectFleetPortal() {		
		clickElementIfPresent(contiConnectButtonFleetPortal.get(2));
		return this;
	}	

	public MainPage openAdministration() {
		// wait for green notification to disapear
		TimeUtil.sleep(4, TimeUnit.SECONDS);

		clickElementIfPresent(administrationButton);
		return this;
	}
	
	// ----------- Navigation Options ContiConnect--------------

	public AlertsPage openAlerts() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(fapSubmenuButtons.get(0));
		return new AlertsPage(driver, wait);
	}

	public MyFleetPage openMyFleet() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(fapSubmenuButtons.get(1));
		return new MyFleetPage(driver, wait);
	}

	public DashboardPage openDashboard() {
		clickElementIfPresent(fapSubmenuButtons.get(2));
		return new DashboardPage(driver, wait);
	}

	public ConfigurationPage openConfiguration() {
		// wait for green notification to disapear
		TimeUtil.sleep(4, TimeUnit.SECONDS);
				
		clickElementIfPresent(fapSubmenuButtons.get(3));
		return new ConfigurationPage(driver, wait);
	}
	
	// ----------- Navigation Options --------------

		public SearchVehiclesPage openVehicles() {
			clickElementIfPresent(vehicles);
			return new SearchVehiclesPage(driver, wait);
		}

		public void openAccounts() {
			clickElementIfPresent(accounts);		
			
		}

		public TelematicBoxes openTelematicsBoxes() {
			clickElementIfPresent(telematicsBoxes);
			return new TelematicBoxes(driver, wait);
		}

		public SearchYardReaderPage openYardReaders() {
			clickElementIfPresent(yardReaders);
			return new SearchYardReaderPage(driver, wait);
		}
}
