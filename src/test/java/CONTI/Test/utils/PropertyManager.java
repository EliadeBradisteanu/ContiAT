package CONTI.Test.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyManager {

	private static final String CHROME_DRIVER_ONWINDOWS_PATH = "chrome_driver_onwindows_path";
	private static final String CHROME_DRIVER_ONLINUX_PATH = "chrome_driver_onlinux_path";
	private static final String FIREFOX_DRIVER_ONWINDOWS64_PATH = "firefox_driver_onwindows64_path";
	private static final String IE_DRIVER_ONWINDOWS_PATH = "ie_driver_onwindows_path";
	
	private static final String RUN_REMOTE = "run_remote";
	private static final String REMOTE_DRIVER_URL = "remote_driver_url";
	private static final String REMOTE_HTTP_PROXY = "remote_http_proxy";
	private static final String REMOTE_SSL_PROXY = "remote_ssl_proxy";
	private static final String REMOTE_NO_PROXY = "remote_no_proxy";	

	private static final String CONTI_URL = "conti_url";
	private static final String TC_USER_A_URL = "tc_user_a_url";
	
	private static final String PRESSURE_SIMULATOR_URL_SE_BU_015 = "pressure_simulrator_url_se_bu_015";
	private static final String PRESSURE_SIMULATOR_URL_AT_BU_000 = "pressure_simulrator_url_at_bu_000";
	
	
	private static final String PAGE_LOAD_TIMEOUT = "page_load_timeout";
	private static final String IMPLICIT_WAIT = "implicit_wait";

	private static final String USER_MHPCKU1 = "user_mhpcku1";
	private static final String USER_MHPCKU2 = "user_mhpcku2";
	private static final String PASSWORD_MHPCKU12 = "password_mhpcku12";

	private static final String USER_TC_USER_A = "user_TC_User_A";
	private static final String PASS_TC_USER_A = "password_TC_User_A";
	
	private static final String USER_TC_USER_B4 = "user_TC_User_B4";
	private static final String PASS_TC_USER_B4 = "password_TC_User_B4";

	private static final String USER_TEST_WS_VIEW = "user_test_ws_view";
	private static final String PASS_TEST_WS_VIEW = "password_test_ws_view";
	
	private static final String USER_TC_USER_C4 = "user_TC_User_C4";
	private static final String PASS_TC_USER_C4 = "password_TC_User_C4";
	
	private static final String FLEET_USER_3288193 = "fleet_user_3288193";
	private static final String PASS_FLEET_USER_3288193 = "password_fleet_user_3288193";
	
	private static final String DEPOT_USER_3288194 = "depot_user_3288194";
	private static final String PASS_DEPOT_USER_3288194 = "password_depot_user_3288194";
	
	private static final String DEPOT_USER_3288200 = "depot_user_3288200";
	private static final String PASS_DEPOT_USER_3288200 = "password_depot_user_3288200";
	
	private static final String MONITOREDVEHICLE_URL = "monitoredVehicle_url";
	private static final String GREENDVEHICLE_URL = "greenVehicle_url";
	private static final String INCOMPLETEDVEHICLE_URL = "incompleteSensor_url";
	
	private static final String DOWNLOADED_FILE_NAMES = "downloaded_file_names";	
	private static final String ALERTS_XLS_TEXT = "alerts_xls_text";
	
	private static final String ALERTS_DETAILS_CVS= "alerts_details_cvs";
	private static final String ALERTS_DETAILS_XLS_OVERVIEW_TEXT = "alerts_details_xls_overview_text";
	private static final String ALERTS_DETAILS_XLS_PRESSURE_MINIMUM_TEXT = "alerts_details_xls_pressure_minimum_text";
	private static final String ALERTS_DETAILS_XLS_PRESSURE_AVERAGE_TEXT = "alerts_details_xls_pressure_average_text";
	private static final String ALERTS_DETAILS_XLS_TEMPERATURE_MAXIMUM_TEXT = "alerts_details_xls_temperature_maximum_text";
	private static final String ALERTS_DETAILS_XLS_TEMPERATURE_AVERAGE_TEXT = "alerts_details_xls_temperature_average_text";
	
	private static final String MYFLEET_DETAILS_CVS= "myfleet_details_cvs";
	private static final String MYFLEET_DETAILS_XLS_OVERVIEW_TEXT = "myfleet_details_xls_overview_text";
	private static final String MYFLEET_DETAILS_XLS_PRESSURE_MINIMUM_TEXT = "myfleet_details_xls_pressure_minimum_text";
	private static final String MYFLEET_DETAILS_XLS_PRESSURE_AVERAGE_TEXT = "myfleet_details_xls_pressure_average_text";
	private static final String MYFLEET_DETAILS_XLS_TEMPERATURE_MAXIMUM_TEXT = "myfleet_details_xls_temperature_maximum_text";
	private static final String MYFLEET_DETAILS_XLS_TEMPERATURE_AVERAGE_TEXT = "myfleet_details_xls_temperature_average_text";
	
	private static final String MYFLEET_XLS_TEXT = "myfleet_xls_text";
	private static final String MYFLEET_CSV_TEXT = "myfleet_csv_text";
	
	private static final String DASHBOARD_XLS_TEXT = "dashboard_xls_text";
	private static final String DASHBOARD_CSV_TEXT = "dashboard_csv_text";
	
	private static PropertyManager instance;
	private static final Object lock = new Object();
	private static final String propertyFilePath = "config/MainConfiguration.properties";
	private static Properties properties = new Properties();

	private static Logger log = Logger.getLogger(PropertyManager.class);

	/**
	 * Create a singleton instance. We need one instance of Property Manager
	 * 
	 * @return
	 */
	public static PropertyManager getInstance() {
		if (instance == null) {
			synchronized (lock) {
				instance = new PropertyManager();
				instance.loadData();
			}
		}
		return instance;
	}

	private void loadData() {
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream(propertyFilePath));
		} catch (IOException e) {
			log.error("Error while reading the properties file");
		}
	}

	public String getChromeDriverOnWindowsPath() {
		return properties.getProperty(CHROME_DRIVER_ONWINDOWS_PATH);
	}

	public String getChromeDriverOnlinuxPath() {
		return properties.getProperty(CHROME_DRIVER_ONLINUX_PATH);
	}

	public String getFirefoxDriverOnwindows64Path() {
		return properties.getProperty(FIREFOX_DRIVER_ONWINDOWS64_PATH);
	}

	public String getIeDriverOnwindowsPath() {
		return properties.getProperty(IE_DRIVER_ONWINDOWS_PATH);
	}

	public boolean getRunRemote() {
		return Boolean.parseBoolean(properties.getProperty(RUN_REMOTE));
	}
	
	public String getRemoteDriverUrl() {
		return properties.getProperty(REMOTE_DRIVER_URL);
	}	

	public String getRemoteHttpProxy() {
		return properties.getProperty(REMOTE_HTTP_PROXY);
	}

	public String getRemoteSslProxy() {
		return properties.getProperty(REMOTE_SSL_PROXY);
	}

	public String getRemoteNoProxy() {
		return properties.getProperty(REMOTE_NO_PROXY);
	}

	public String getContiUrl() {
		return properties.getProperty(CONTI_URL);
	}
	
	public String getTcUserAUrl() {
		return properties.getProperty(TC_USER_A_URL);
	}

	public String getPressureSimulatorLinkSEBU015() {
		return properties.getProperty(PRESSURE_SIMULATOR_URL_SE_BU_015);
	}

	public String getPressureSimulatorUrlAtBu000() {
		return properties.getProperty(PRESSURE_SIMULATOR_URL_AT_BU_000);
	}

	public Long getPageLoadTimeout() {
		return Long.parseLong(properties.getProperty(PAGE_LOAD_TIMEOUT));
	}

	public Long getImplicitWait() {
		return Long.parseLong(properties.getProperty(IMPLICIT_WAIT));
	}

	public String getUserMhpcku1() {
		return properties.getProperty(USER_MHPCKU1);
	}

	public String getUserMhpcku2() {
		return properties.getProperty(USER_MHPCKU2);
	}

	public String getPasswordMhpcku12() {
		return properties.getProperty(PASSWORD_MHPCKU12);
	}

	public String getUserTcUserA() {
		return properties.getProperty(USER_TC_USER_A);
	}

	public String getPassTcUserA() {
		return properties.getProperty(PASS_TC_USER_A);
	}

	public String getUserTcUserB4() {
		return properties.getProperty(USER_TC_USER_B4);
	}

	public String getPassTcUserB4() {
		return properties.getProperty(PASS_TC_USER_B4);
	}

	public String getUserTestWsView() {
		return properties.getProperty(USER_TEST_WS_VIEW);
	}

	public String getPassTestWsView() {
		return properties.getProperty(PASS_TEST_WS_VIEW);
	}	
		
	public String getUserTcUserC4() {
		return properties.getProperty(USER_TC_USER_C4);
	}

	public String getPassTcUserC4() {
		return properties.getProperty(PASS_TC_USER_C4);
	}	

	public String getFleetUser3288193() {
		return properties.getProperty(FLEET_USER_3288193);
	}

	public String getPassFleetUser3288193() {
		return properties.getProperty(PASS_FLEET_USER_3288193);
	}

	public String getDepotUser3288194() {
		return properties.getProperty(DEPOT_USER_3288194);
	}

	public String getPassDepotUser3288194() {
		return properties.getProperty(PASS_DEPOT_USER_3288194);
	}	
	
	public String getDepotUser3288200() {
		return properties.getProperty(DEPOT_USER_3288200);
	}

	public String getPassDepotUser3288200() {
		return properties.getProperty(PASS_DEPOT_USER_3288200);
	}

	public String getMonitoredvehicleUrl() {
		return properties.getProperty(MONITOREDVEHICLE_URL);
	}

	public String getGreendvehicleUrl() {
		return properties.getProperty(GREENDVEHICLE_URL);
	}

	public String getIncompletedvehicleUrl() {
		return properties.getProperty(INCOMPLETEDVEHICLE_URL);
	}

	public String getPropertyfilepath() {
		return propertyFilePath;
	}

	public String getDownloadedFileNames() {
		return properties.getProperty(DOWNLOADED_FILE_NAMES);
	}

	public String getAlertsXlsText() {
		return properties.getProperty(ALERTS_XLS_TEXT);
	}
	
	public String getAlertsDetailsCvs() {
		return properties.getProperty(ALERTS_DETAILS_CVS);
	}

	public String getAlertsDetailsXlsOverviewText() {
		return properties.getProperty(ALERTS_DETAILS_XLS_OVERVIEW_TEXT);
	}

	public String getAlertsDetailsXlsPressureMinimumText() {
		return properties.getProperty(ALERTS_DETAILS_XLS_PRESSURE_MINIMUM_TEXT);
	}
	
	public String getAlertsDetailsXlsPressureAverageText() {
		return properties.getProperty(ALERTS_DETAILS_XLS_PRESSURE_AVERAGE_TEXT);
	}

	public String getAlertsDetailsXlsTemperatureMaximumText() {
		return properties.getProperty(ALERTS_DETAILS_XLS_TEMPERATURE_MAXIMUM_TEXT);
	}

	public String getAlertsDetailsXlsTemperatureAverageText() {
		return properties.getProperty(ALERTS_DETAILS_XLS_TEMPERATURE_AVERAGE_TEXT);
	}

	public String getMyfleetDetailsCvs() {
		return properties.getProperty(MYFLEET_DETAILS_CVS);
	}

	public String getMyfleetDetailsXlsOverviewText() {
		return properties.getProperty(MYFLEET_DETAILS_XLS_OVERVIEW_TEXT);
	}

	public String getMyfleetDetailsXlsPressureMinimumText() {
		return properties.getProperty(MYFLEET_DETAILS_XLS_PRESSURE_MINIMUM_TEXT);
	}

	public String getMyfleetDetailsXlsPressureAverageText() {
		return properties.getProperty(MYFLEET_DETAILS_XLS_PRESSURE_AVERAGE_TEXT);
	}

	public String getMyfleetDetailsXlsTemperatureMaximumText() {
		return properties.getProperty(MYFLEET_DETAILS_XLS_TEMPERATURE_MAXIMUM_TEXT);
	}

	public String getMyfleetDetailsXlsTemperatureAverageText() {
		return properties.getProperty(MYFLEET_DETAILS_XLS_TEMPERATURE_AVERAGE_TEXT);
	}

	public static Properties getProperties() {
		return properties;
	}

	public String getMyfleetXlsText() {
		return properties.getProperty(MYFLEET_XLS_TEXT);
	}

	public String getMyfleetCsvText() {
		return properties.getProperty(MYFLEET_CSV_TEXT);
	}

	public String getDashboardXlsText() {
		return properties.getProperty(DASHBOARD_XLS_TEXT);
	}

	public String getDashboardCsvText() {
		return properties.getProperty(DASHBOARD_CSV_TEXT);
	}
	
}
