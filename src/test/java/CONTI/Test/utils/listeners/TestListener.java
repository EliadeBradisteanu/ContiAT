package CONTI.Test.utils.listeners;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

import CONTI.Test.BaseTest;
import CONTI.Test.utils.extentReports.ExtentManager;
import CONTI.Test.utils.extentReports.ExtentTestManager;

public class TestListener extends BaseTest implements ITestListener {

	private Logger log = Logger.getLogger(TestListener.class);

	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}

	// Before starting all tests, below method runs.
	@Override
	public void onStart(ITestContext iTestContext) {
		log.info("I am in onStart method " + iTestContext.getName());
		iTestContext.setAttribute("WebDriver", this.driver);
	}

	// After ending all tests, below method runs.
	@Override
	public void onFinish(ITestContext iTestContext) {
		log.info("I am in onFinish method " + iTestContext.getName());

		// Do tier down operations for extentreports reporting!
		ExtentTestManager.endTest();
		ExtentManager.getReporter4().flush();
	}

	@Override
	public void onTestStart(ITestResult iTestResult) {
		log.info("I am in onTestStart method " + getTestMethodName(iTestResult) + " start");
	}

	@Override
	public void onTestSuccess(ITestResult iTestResult) {
		log.info("I am in onTestSuccess method " + getTestMethodName(iTestResult) + " succeed");

		// Extentreports log operation for passed tests.
		ExtentTestManager.getTest().log(Status.PASS, "Test passed " + iTestResult.getName());
	}

	@Override
	public void onTestFailure(ITestResult iTestResult) {
		log.info("I am in onTestFailure method " + getTestMethodName(iTestResult) + " failed");

		// Get driver from BaseTest and assign to local webdriver variable.
		Object testClass = iTestResult.getInstance();
		WebDriver webDriver = ((BaseTest) testClass).getDriver();

		// Take base64Screenshot screenshot.
		String base64Screenshot = "data:image/png;base64,"
				+ ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BASE64);

		// Extentreports log and screenshot operations for failed tests.		
		ExtentTestManager.getTest()
		                  .log(Status.FAIL, "Test Failed " + iTestResult.getName() + " Test case FAILED due to below issues:");
		ExtentTestManager.getTest().fail(iTestResult.getThrowable());
		ExtentTestManager.getTest().addScreenCaptureFromBase64String(base64Screenshot);			
	}

	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		log.info("I am in onTestSkipped method " + getTestMethodName(iTestResult) + " skipped");

		// Extentreports log operation for skipped tests.
		ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped " + iTestResult.getName());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
		log.info("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
	}

}
