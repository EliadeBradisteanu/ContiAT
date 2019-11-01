package CONTI.Test.utils.extentReports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {         

	private static final String EXTENT_REPORTS_RESULTS_PATH = "src/main/resources/extentReports/ExtentReportResults.html";
	
	private static ExtentReports extent;
	private static ExtentHtmlReporter htmlReporter;

	public synchronized static ExtentReports getReporter4() {
		if (extent == null) {
			// Set HTML reporting file location
			htmlReporter = new ExtentHtmlReporter(EXTENT_REPORTS_RESULTS_PATH);
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);			
		
			htmlReporter.config().setDocumentTitle("Automation Report"); // Tittle of Report
			htmlReporter.config().setReportName("Conti Report"); // Name of the report	        
			htmlReporter.config().setTheme(Theme.STANDARD);
	        
			// General information releated to application
			extent.setSystemInfo("Application Name", "Automated testing");
			extent.setSystemInfo("Envirnoment", "QA");
		}
		return extent;
	}
}
