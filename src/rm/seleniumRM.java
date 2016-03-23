package rm;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class seleniumRM extends baseMethods {

	public static void main(String[] args) throws IOException {
		// initializing profile dev by default
		String Profile = "profiledev";
		ArrayList<String> result = new ArrayList<>();
		System.setOut(new PrintStream(new FileOutputStream("output.txt")));

		setupEnvironment();

		if (setting_environment.equalsIgnoreCase("Dev")) {
			System.out.println(dateFormat.format(new Date()) + " : "
					+ "Running Development profile ...");
			result.add("profiledev");
		} else {
			int num = Integer.parseInt(setting_noofprofile);

			for (int i = 1; i <= num; i++) {
				result.add("profile" + i);
			}
			System.out.println(dateFormat.format(new Date()) + " : "
					+ "Total Number of profiles to be run : "
					+ setting_noofprofile);

		}
		for (String ls : result) {
			Profile = ls;
			WebDriver driver = null;
			initialize(Profile);
			String foldername = setting_installpath.replace("\\\\", "\\")
					+ getFolderName(Profile) + "\\" + Profile;
			if (setting_browser.equalsIgnoreCase("Chrome")) {
				System.out.println(dateFormat.format(new Date()) + " : "
						+ "Setting up chrome browser for testing.");

				System.setProperty("webdriver.chrome.driver",
						setting_browserLocation);
				driver = new ChromeDriver();
			} else {

				System.out.println(dateFormat.format(new Date()) + " : "
						+ "Setting up firefox browser for testing.");

				FirefoxProfile profile = new FirefoxProfile();

				profile.setAcceptUntrustedCertificates(true);

				profile.setPreference("browser.download.folderList", 2);
				profile.setPreference("browser.download.dir",
						foldername.replace("\\t\\W", ""));
				profile.setPreference("browser.download.panel.shown", false);
				profile.setPreference(
						"browser.download.manager.showWhenStarting", false);

				profile.setPreference(
						"plugin.disable_full_page_plugin_for_types",
						"application/pdf, application/rtf");
				profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
						"application/pdf, application/rtf");
				profile.setPreference("pdfjs.disabled", true);
				driver = new FirefoxDriver(profile);
			}

			driver.manage().window().maximize();
			try {
				setup(driver);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				reportManagerSelection(driver);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				validate(driver);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				downloadReport(driver);
				if (setting_dropreport.equalsIgnoreCase("Yes")) {
					driver.manage().timeouts()
							.implicitlyWait(20, TimeUnit.SECONDS);
					dropReport(driver);

				}
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				// quit when download completes
				boolean isDownloadCompletes = isDownloadCompletes(foldername);
				if (isDownloadCompletes) {
					quit(driver);
				} else {
					System.out
							.println(dateFormat.format(new Date())
									+ " : "
									+ "Download still pending. Remove any PART file from download location "
									+ foldername);
				}
			} catch (Exception e) {
				quit(driver);
				e.printStackTrace();
			}
		}
	}

}
