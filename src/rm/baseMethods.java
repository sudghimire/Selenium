package rm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class baseMethods {

	static String setting_noofprofile = "1";
	static String setting_dropreport = "No";
	static String setting_environment = "Dev";
	static String setting_browser = "Firefox";
	static String setting_browserLocation = "";
	static String setting_installpath = "";
	static String url = "";
	static String username = "";
	static String password = "";
	static String front = "";
	static String isCTP = "";
	static String analysisType1 = "";
	static String analysisType2 = "";
	static String adjNorm = "";
	static String monthlyCost = "";
	static String riskScore = "";
	static String genInterval = "";
	static String repFormat = "";
	static String repName = "";

	static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

	protected static void setupEnvironment() throws DOMException {
		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Setting - up test environment ...");

		File file = new File("config.xml");
		DocumentBuilder dBuilder;

		try {

			dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			NodeList nList = doc.getElementsByTagName("setting");
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;
			setting_noofprofile = eElement.getElementsByTagName("noofprofile")
					.item(0).getTextContent();
			setting_dropreport = eElement.getElementsByTagName("dropreport")
					.item(0).getTextContent();
			setting_environment = eElement.getElementsByTagName("environment")
					.item(0).getTextContent();
			setting_browser = eElement.getElementsByTagName("browser").item(0)
					.getTextContent();
			setting_browserLocation = eElement
					.getElementsByTagName("browserLocation").item(0)
					.getTextContent();
			setting_installpath = eElement
					.getElementsByTagName("installationpath").item(0)
					.getTextContent().replace("\\W", "");
		} catch (ParserConfigurationException | SAXException | IOException e1) {

			e1.printStackTrace();
		}
	}

	protected static void initialize(String Profile) {
		try {
			System.out.println(dateFormat.format(new Date()) + " : "
					+ "Initializing for " + Profile);
			File file = new File("config.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			Document doc = dBuilder.parse(file);
			NodeList nList = doc.getElementsByTagName(Profile);
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;
			repName = eElement.getElementsByTagName("repName").item(0)
					.getTextContent();
			url = eElement.getElementsByTagName("url").item(0).getTextContent();
			username = eElement.getElementsByTagName("username").item(0)
					.getTextContent();
			password = eElement.getElementsByTagName("password").item(0)
					.getTextContent();
			front = eElement.getElementsByTagName("client").item(0)
					.getTextContent();
			isCTP = eElement.getElementsByTagName("isCTP").item(0)
					.getTextContent();
			analysisType1 = eElement.getElementsByTagName("analysisType1")
					.item(0).getTextContent();
			analysisType2 = eElement.getElementsByTagName("analysisType2")
					.item(0).getTextContent();
			adjNorm = eElement.getElementsByTagName("adjNorm").item(0)
					.getTextContent();
			monthlyCost = eElement.getElementsByTagName("monthlyCost").item(0)
					.getTextContent();
			riskScore = eElement.getElementsByTagName("riskScore").item(0)
					.getTextContent();
			genInterval = eElement.getElementsByTagName("genInterval").item(0)
					.getTextContent();
			repFormat = eElement.getElementsByTagName("repFormat").item(0)
					.getTextContent();
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	protected static boolean isDownloadCompletes(String foldername)
			throws InterruptedException {
		// TODO Auto-generated method stub
		boolean downloadCompletes = false;
		File folder = new File(foldername);

		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				while (listOfFiles[i].getName().contains("part")) {

					System.out.println(dateFormat.format(new Date()) + " : "
							+ "Waiting Until Download completes ...");
					Thread.sleep(1000);
				}
				downloadCompletes = true;
			}
		}
		return downloadCompletes;
	}
	protected static void downloadReport(WebDriver driver)
			throws InterruptedException {
		// TODO Auto-generated method stub

		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Downloading Report in specified folder ...");
		Thread.sleep(5000);
		if (repFormat.equalsIgnoreCase("pdf")) {
			// download pdf
			driver.findElement(
					By.xpath("//*[@id='table_buttons']/tbody/tr/td[4]/img"))
					.click();
			Thread.sleep(15000);
		} else if (repFormat.equalsIgnoreCase("rtf")) {
			// download rtf
			driver.findElement(
					By.xpath(".//*[@id='table_buttons']/tbody/tr/td[3]/img"))
					.click();
			Thread.sleep(15000);
		} else {
			driver.findElement(
					By.xpath("//*[@id='table_buttons']/tbody/tr/td[3]/img"))
					.click();
			Thread.sleep(15000);
			driver.findElement(
					By.xpath("//*[@id='table_buttons']/tbody/tr/td[4]/img"))
					.click();
			Thread.sleep(15000);
		}

		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Waiting until download completes...");

	}
	protected static String getFolderName(String profile) throws IOException {

		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String CurrentTime = sdf.format(cal.getTime()).toString();
		String driveName = setting_installpath.substring(0, 1);
		File currentDir = new File(CurrentTime);
		File currentsubDir = new File(CurrentTime);
		String FolderName = CurrentTime;

		if (currentDir.isDirectory()) {
			if (currentsubDir.isDirectory()) {
				FolderName = CurrentTime;
			} else {
				FolderName = CurrentTime;
				final File file1 = new File("run1.bat");
				file1.createNewFile();
				PrintWriter writer = new PrintWriter(file1, "UTF-8");
				writer.println(driveName + ":");
				writer.println("cd "
						+ setting_installpath.replace("\\\\", "\\"));
				writer.println("mkdir " + CurrentTime);
				writer.println("exit");
				writer.close();
				try {
					Runtime.getRuntime().exec("cmd /c start run1.bat");
				} catch (IOException e) {
				}
			}

		} else {
			final File file2 = new File("run2.bat");
			file2.createNewFile();
			PrintWriter writer = new PrintWriter(file2, "UTF-8");
			writer.println(driveName + ":");
			writer.println("cd " + setting_installpath.replace("\\\\", "\\"));
			writer.println("mkdir " + CurrentTime);
			writer.println("cd " + CurrentTime);
			writer.println("mkdir " + profile);
			writer.println("exit");
			writer.close();
			try {
				Runtime.getRuntime().exec("cmd /c start run2.bat");
			} catch (IOException e) {

			}
		}

		return FolderName.replaceAll("\\W", "");
	}
	protected static void dropReport(WebDriver driver)
			throws NoAlertPresentException {
		// TODO Auto-generated method stub
		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Dropping test report...");
		driver.findElement(By.xpath("//*[@id='table_buttons']/tbody/tr/td[2]"))
				.click();

		Alert alert = driver.switchTo().alert();
		alert.accept();

	}

	protected static void validate(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Validating Report ...");
		String Status = "Queued";
		String reportid = "999999";
		reportid = driver.findElement(
				By.xpath("//*[@id='grid']/tbody/tr[3]/td[2]")).getText();
		boolean reportStatus = true;
		while (reportStatus) {
			try {
				Thread.sleep(10000);

				Status = driver.findElement(
						By.xpath("//*[@id='grid']/tbody/tr[3]/td[13]"))
						.getText();

				System.out.println(dateFormat.format(new Date()) + " : "
						+ "Your Report Id is - " + reportid);
				if (Status.equalsIgnoreCase("Error")) {
					System.out.println(dateFormat.format(new Date()) + " : "
							+ "Test Fail. Report goes to Error state.");
					reportStatus = false;

				} else if (Status.equalsIgnoreCase("Completed")) {

					System.out.println(dateFormat.format(new Date()) + " : "
							+ "Working ... Report Status is :: Completed.");
					System.out.println(dateFormat.format(new Date()) + " : "
							+ "Test Pass. Report generated successfully.");

					reportStatus = false;

				} else {
					System.out.println(dateFormat.format(new Date()) + " : "
							+ "Working ... Report Status is :: " + Status);
					reportStatus = true;
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	protected static void reportManagerSelection(WebDriver driver) {
		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Setting Report manager for report generation ...");

		WebDriverWait wait = new WebDriverWait(driver, 10);

		WebElement reportManager = wait.until(ExpectedConditions
				.elementToBeClickable(By
						.xpath("//*[@id='GlobalNavigation1']/ul/li[1]/a")));
		reportManager.click();

		// 1 - Data Selection Tab
		WebElement reportTitle = driver.findElement(By.name("repName"));
		reportTitle.sendKeys(repName);
		WebElement BOBReport = driver.findElement(By.id("checkbox0"));
		BOBReport.click();
		WebElement AddReport = driver.findElement(By.name("btnAddPT"));
		AddReport.click();

		// 2 - Report Tab
		WebElement ReportTab = driver.findElement(By
				.xpath("//*[@id='modules-cpanel']/div[1]/h2[2]"));
		ReportTab.click();

		// select MIR package
		Select mirdropdown = new Select(driver.findElement(By
				.xpath("//*[@id='rptPkgSelection']/select")));
		mirdropdown.selectByVisibleText("Medical Intelligence Report");

		driver.findElement(By.id("checkTd345")).click();
		driver.findElement(By.id("checkTd347")).click();
		driver.findElement(By.id("checkTd348")).click();
		driver.findElement(By.id("checkTd349")).click();
		driver.findElement(By.id("checkTd351")).click();
		driver.findElement(By.id("checkTd352")).click();
		driver.findElement(By.id("checkTd354")).click();
		driver.findElement(By.id("checkTd355")).click();
		driver.findElement(By.id("checkTd356")).click();
		driver.findElement(By.id("checkTd357")).click();

		if (isCTP.equalsIgnoreCase("No")) {
			driver.findElement(By.id("checkTd359")).click();
			driver.findElement(By.id("checkTd360")).click();
			driver.findElement(By.id("checkTd361")).click();
			driver.findElement(By.id("checkTd362")).click();
			driver.findElement(By.id("checkTd363")).click();
		}

		driver.findElement(By.name("btnChapContinue")).click();

		// 3 - Generation Tab
		// CTP or Predefined
		System.out.println(dateFormat.format(new Date()) + " : " + "CTP ? :"
				+ isCTP);
		if (isCTP.equalsIgnoreCase("Yes")) {
			Select dropdown = new Select(driver.findElement(By
					.id("ctp_MIR_Time_Period")));
			dropdown.selectByVisibleText("Custom");
			// analysisType1 and analysisType2

			if (analysisType2.contains("Paid")) {
				Select dropdown2 = new Select(driver.findElement(By
						.id("ctp_MIR_reporting_By")));
				dropdown2.selectByVisibleText("Paid");

			} else if (analysisType2.contains("Incurred")) {
				Select dropdown2 = new Select(driver.findElement(By
						.id("ctp_MIR_reporting_By")));
				dropdown2.selectByVisibleText("Incurred");

			} else {
				Select dropdown2 = new Select(driver.findElement(By
						.id("ctp_MIR_reporting_By")));
				dropdown2.selectByVisibleText("Incurred and Paid");
			}

		}

		if (isCTP.equalsIgnoreCase("No")) {
			Select dropdown = new Select(driver.findElement(By
					.id("ctp_MIR_Time_Period")));
			dropdown.selectByVisibleText("Predefined");
			// analysisType1 and analysisType2
			if (analysisType1.contains("Rolling")) {
				Select dropdown2 = new Select(driver.findElement(By
						.name("reportingPeriod")));
				dropdown2.selectByVisibleText("Rolling Year");
				Select dropdown3 = new Select(driver.findElement(By
						.name("utilizationAnalysis")));
				dropdown3.selectByVisibleText("Rolling Year");

			}

			if (analysisType1.contains("Full")) {
				Select dropdown2 = new Select(driver.findElement(By
						.name("reportingPeriod")));
				dropdown2.selectByVisibleText("Full Cycle");
				Select dropdown3 = new Select(driver.findElement(By
						.name("utilizationAnalysis")));
				dropdown3.selectByVisibleText("Full Cycle");

			}

			if (analysisType1.contains("Year To Date")) {
				Select dropdown2 = new Select(driver.findElement(By
						.name("reportingPeriod")));
				dropdown2.selectByVisibleText("Year To Date");
				Select dropdown3 = new Select(driver.findElement(By
						.name("utilizationAnalysis")));
				dropdown3.selectByVisibleText("Year To Date");

			}

			if (analysisType2.contains("Paid")) {
				Select dropdown2 = new Select(driver.findElement(By
						.name("reportingBasis")));
				dropdown2.selectByVisibleText("Paid");
				Select dropdown3 = new Select(driver.findElement(By
						.name("utilizationReportingBasis")));
				dropdown3.selectByVisibleText("Paid");

			}

			if (analysisType2.contains("Incurred")) {
				Select dropdown2 = new Select(driver.findElement(By
						.name("reportingBasis")));
				dropdown2.selectByVisibleText("Incurred");
				Select dropdown3 = new Select(driver.findElement(By
						.name("utilizationReportingBasis")));
				dropdown3.selectByVisibleText("Incurred");

			}

		}

		// Adjusted Norm
		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Adjusted Norm :" + adjNorm);
		if (adjNorm.equalsIgnoreCase("Age-Gender-Geography")) {
			Select dropdown = new Select(driver.findElement(By
					.name("adjustedNorm")));
			dropdown.selectByVisibleText("Age-Gender-Geography");
		} else if (adjNorm.equalsIgnoreCase("Age-Gender")) {
			Select dropdown = new Select(driver.findElement(By
					.name("adjustedNorm")));
			dropdown.selectByVisibleText("Age-Gender");
		} else {
			Select dropdown = new Select(driver.findElement(By
					.name("adjustedNorm")));
			dropdown.selectByVisibleText("None");
		}

		// Monthly Cost
		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Monthly Cost :" + monthlyCost);
		if (monthlyCost.equalsIgnoreCase("PEPM")) {
			Select dropdown = new Select(driver.findElement(By
					.name("monthlyCost")));
			dropdown.selectByVisibleText("Per Employee (PEPM)");
		} else {
			Select dropdown = new Select(driver.findElement(By
					.name("monthlyCost")));
			dropdown.selectByVisibleText("Per Member (PMPM)");
		}

		// Risk Score
		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Risk Score :" + riskScore);
		if (riskScore.equalsIgnoreCase("RRS")) {
			Select dropdown = new Select(driver.findElement(By
					.name("riskScore")));
			dropdown.selectByVisibleText("Prospective RRS (Model #26)");
		} else {
			Select dropdown = new Select(driver.findElement(By
					.name("riskScore")));
			dropdown.selectByVisibleText("Risk Index");
		}

		// Generation Interval
		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Generation Interval :" + genInterval);
		if (genInterval.equalsIgnoreCase("Quaterly")) {
			Select dropdown = new Select(driver.findElement(By
					.name("generationInterval")));
			dropdown.selectByVisibleText("Quaterly");
		} else if (genInterval.equalsIgnoreCase("Monthly")) {
			Select dropdown = new Select(driver.findElement(By
					.name("generationInterval")));
			dropdown.selectByVisibleText("Monthly");
		} else {
			Select dropdown = new Select(driver.findElement(By
					.name("generationInterval")));
			dropdown.selectByVisibleText("Single");
		}

		// Report Format
		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Report Format :" + repFormat);
		if (repFormat.equalsIgnoreCase("PDF")) {
			if (driver.findElement(By.name("reportFormatRtf")).isSelected()) {
				driver.findElement(By.name("reportFormatRtf")).click();
			}
			if (!driver.findElement(By.name("reportFormatPdf")).isSelected()) {
				driver.findElement(By.name("reportFormatPdf")).click();
			}
		} else if (repFormat.equalsIgnoreCase("RTF")) {
			if (!driver.findElement(By.name("reportFormatRtf")).isSelected()) {
				driver.findElement(By.name("reportFormatRtf")).click();
			}
			if (driver.findElement(By.name("reportFormatPdf")).isSelected()) {
				driver.findElement(By.name("reportFormatPdf")).click();
			}
		} else {
			if (!driver.findElement(By.name("reportFormatRtf")).isSelected()) {
				driver.findElement(By.name("reportFormatRtf")).click();
			}
			if (!driver.findElement(By.name("reportFormatPdf")).isSelected()) {
				driver.findElement(By.name("reportFormatPdf")).click();
			}
		}

		// 4 - User access
		driver.findElement(By.name("btnRepGenContinue")).click();
		driver.findElement(By.name("btnFinalSave")).click();

	}

	protected static void quit(WebDriver driver) {
		// TODO Auto-generated method stub

		driver.findElement(By.linkText("Logout")).click();
		driver.quit();
		System.out.println(dateFormat.format(new Date()) + " : "
				+ "All task completed. Closing application.");
		System.out.println("------------------------------------");
	}

	protected static void setup(WebDriver driver) throws InterruptedException {

		System.out.println(dateFormat.format(new Date()) + " : "
				+ "Browser Seting - up ...");
		driver.get(url);
		byte[] decodedpassword = Base64.decodeBase64(password.getBytes());

		WebElement userelement = driver.findElement(By.id("username"));
		WebElement passelement = driver.findElement(By.id("password"));
		WebElement submitelement = driver
				.findElement(By.className("btnNormal"));
		userelement.sendKeys(username);
		passelement.sendKeys(new String(decodedpassword));
		submitelement.click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.switchTo().frame("mainFrame");

		Select dropdown = new Select(driver.findElement(By
				.id("applicationSection")));
		dropdown.selectByVisibleText(front);

		Thread.sleep(1000);

	}

}
