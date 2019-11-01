package CONTI.Test.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.aventstack.extentreports.Status;

import CONTI.Test.utils.extentReports.ExtentTestManager;

public class CsvUtils {

	private static Logger log = Logger.getLogger(CsvUtils.class);

	public static List<String> getCsvData(String path) {
		List<String> records = new ArrayList<>();
		
		int lineNumbers = 0;

		//read only 50 lines sometimes while never exits
		try (Scanner scanner = new Scanner(new File(path));) {
			while (scanner.hasNextLine() && (lineNumbers++ < 50)) {				
					records.add(scanner.nextLine());									
			}
			scanner.close();
		} catch (Exception e) {
			ExtentTestManager.getTest().log(Status.FAIL, "data could not be read");
			log.error("data could not be read", e);
			e.printStackTrace();
		}

		return records;
	}
}
