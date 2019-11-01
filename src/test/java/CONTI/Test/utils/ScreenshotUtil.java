package CONTI.Test.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import CONTI.Test.BaseTest;

public class ScreenshotUtil{
	
	private static final String SCREENSHOTS = "screenshots";
	private static final String SCREENSHOTS_PATH = "src/main/resources/screenshots";
	private static Logger log = Logger.getLogger(BaseTest.class);
	
	/**
	 * takes screenshot and saves it in folder "Screenshots"
	 * 
	 * @param testClass: subfolder for screenshot
	 * @param fileName: name for .png file
	 */
	public static void takeScreenshot(WebDriver driver, String testClass, String fileName) {
		try {
						
			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			// save file in folder Screenshots + testName
			String fName = SCREENSHOTS_PATH + "\\" + testClass + "\\" + fileName + timestamp + ".png";
			FileUtils.copyFile(scrFile, new File(fName));
			log.info("Screenshot is successfully captured for the test:" + testClass + "  " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Screenshot could NOT be captured for the test:" + testClass + "  " + fileName, e);
		}
	}
	
	/**
	 * Delete the Screenshot directory in order to only have screenshots from current tests
	 */
	public static void deleteScreenshotDir() {
		for(File f : new File("src/main/resources").listFiles()) {
			if(f.getName().contains(SCREENSHOTS)) {				
				try {
					FileUtils.deleteDirectory(f);
				} catch (IOException e) {
					log.error("could not delete Screenshots directory", e);
					e.printStackTrace();
				}
			}
		}
	}

}
