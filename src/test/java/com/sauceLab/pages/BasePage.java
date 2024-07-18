package com.sauceLab.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;

import static com.sauceLab.utilities.TestConstants.*;
import static com.sauceLab.utilities.TestLogger.*;

public abstract class BasePage {

	protected Wait<WebDriver> newWait;
	protected WebDriver driver;
	public String windowHandle;

	public BasePage(WebDriver _driver) {
		driver = _driver;

		newWait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(WAIT_EXPLICIT))
				.pollingEvery(Duration.ofSeconds(WAIT_STANDARD))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class)
				.ignoring(TimeoutException.class)
				.ignoring(InvalidElementStateException.class);

		windowHandle = driver.getWindowHandle();
		PageFactory.initElements(driver, this);

	}

	/***
	 * Scrolls the 'element' into view in the browser
	 * @param element - The WebElement to scroll into view
	 */
	public void scrollIntoView(WebElement element) {
		debug("Scrolling into view " + element.toString());

		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			js.executeScript("arguments[0].scrollIntoView({behaviour: \"auto\", block: \"center\", inline: \"nearest\"});", element);

			waitStandard();
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("Could not scroll the element : [" + element + "] into view as it could not be found.", e);
		}

	}

	/***
	 * Scrolls the 'element' into view in the browser
	 * @param element - The WebElement to scroll into view
	 * @param topOrBottom - Whether to scroll the element to the top or the bottom of its element
	 */
	public void scrollIntoView(WebElement element, boolean topOrBottom) {
		try {
			debug("Scrolling into view " + element.toString() + " aligning to " + topOrBottom);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(arguments[1]);", element, topOrBottom);
			waitStandard();
		} catch (InvalidElementStateException | StaleElementReferenceException | TimeoutException |
				 NoSuchElementException e) {
			error("Could not scroll the element : [" + element + "] into view as it could not be found.", e);
		}
	}

	/**
	 * Scroll to the top of the Current Screen
	 */
	public void scrollToTopOfScreen() {
		info("Scrolling to the top of the screen");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0,0);");
		waitStandard();
	}

	/**
	 * Scrolls Down 100 Pixels
	 */
	public void scrollDown() {
		debug("Scrolling down");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int xCoord = 0;
		int yCoord = 100;
		js.executeScript("scrollBy(arguments[0], arguments[1]);", xCoord, yCoord);
		waitStandard();
	}

	/**
	 * Scrolls Down  a custom amount of Pixels
	 */
	public void scrollDown(int pixels) {
		info("Scrolling down [" + pixels + "] pixels");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int xCoord = 0;
		int yCoord = Math.abs(pixels);
		js.executeScript("scrollBy(arguments[0], arguments[1]);", xCoord, yCoord);
		waitStandard();
	}

	/**
	 * Scrolls Up 100 Pixels
	 */
	public void scrollUp() {
		debug("Scrolling Up");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int xCoord, yCoord;
		xCoord = 0;
		yCoord = -100;
		js.executeScript("scrollBy(arguments[0], arguments[1]);", xCoord, yCoord);
		waitStandard();
	}

	/**
	 * Scrolls Up a custom amount of Pixels
	 */
	public void scrollUp(int pixels) {
		info("Scrolling up [" + pixels + "] pixels");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int xCoord, yCoord;
		xCoord = 0;
		yCoord = -1 * Math.abs(pixels);
		js.executeScript("scrollBy(arguments[0], arguments[1]);", xCoord, yCoord);
		waitStandard();
	}

	/**
	 * Scroll the 'element' into view
	 * Use when WebDriver has trouble scrolling elements on its own
	 *
	 * @param element The element to scroll into view
	 */
	public void scrollElementIntoView(WebElement element) {
		try {
			info("Scrolling " + element.toString() + " into view");
			boolean isOnScreen = isOnScreen(element);

			if (!isOnScreen) {
				info("The scrollable element was not scrolled into view");
			} else {
				info("The element is on the screen");
			}
		} catch (InvalidElementStateException | StaleElementReferenceException | TimeoutException |
				 NoSuchElementException e) {
			error("The element : [" + element + "] could not be found", e);
		}
	}

	/**
	 * Determines if an element is currently "on the screen" but does not
	 * suggest that it is in fact visible
	 *
	 * @param element The element that should be on screen
	 * @return {@link Boolean }
	 */
	public boolean isOnScreen(WebElement element) {
		// we ignore the 'X' portion of the position because we expect elements to be within the bounds of
		// the window on the left and right
		// we check only the 'Y' portion of the position because the page can be taller than one screen
		int y = element.getLocation().getY();
		return y >= 0 && y <= driver.manage().window().getSize().height;
	}

	/**
	 * Drives the browser back through the browser history
	 */
	public void navigateBack() {
		debug("Navigating Back");
		driver.navigate().back();
		waitForPageToLoad();
		waitStandard();
	}

	/***
	 * Switches the context of the WebDriver to the window handle
	 * which is the tab this test began in.
	 */
	public void switchToHomeTab() {
		debug("Switching to Home Tab");
		driver.switchTo().window(windowHandle);
		//switchTab();
		waitStandard();
	}

	/**
	 * Switch the WebDriver to the Current Tab
	 */
	public void switchTab() {
		info("Switching tabs");
		info("Number of tabs: [" + driver.getWindowHandles().size() + "]");
		try {
			driver.getWindowHandles().forEach(tab -> driver.switchTo().window(tab));
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("While switching tabs the driver could not find any to switch to", e);
		}
	}

	/***
	 * Closes the current browser tab
	 */
	public void closeBrowserTab() {
		debug("Closing Browser Tab");
		driver.switchTo().window(driver.getWindowHandle());
		driver.close();
		waitStandard();
	}

	/**
	 * Click an element using Javascript
	 *
	 * @param element the element to click
	 */
	protected <T> void clickWithJavascript(T element) {
		if (element instanceof WebElement) {
			info("Attempting to Click [" + (WebElement) element + "] with JavaScript");
			scrollIntoView((WebElement) element);
			waitStandard();
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		} else if (element instanceof By) {
			WebElement webElement = findElement((By) element);
			info("Attempting to Click [" + webElement + "] with JavaScript");
			scrollIntoView(webElement);
			waitStandard();
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElement);
		}
	}

	//////////// TOP OF WAITS ////////////////////////////////////////////////////////////////////////////////////

	/***
	 * Waits for the amount of 'seconds'
	 * @param seconds - The amount of seconds to wait for
	 */
	public void waitFor(long seconds) {
		try {
			info("Waiting for [" + seconds + "] seconds");
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			error("Error waiting for [" + seconds + "] seconds\n" + e.getMessage());
		}
	}

	/**
	 * Waits for seconds
	 * default=2
	 */
	public void waitStandard() {
		try {
			debug("Waiting for standard [" + WAIT_STANDARD + "] seconds");
			Thread.sleep(WAIT_STANDARD * 1000L);
		} catch (InterruptedException e) {
			error("Error waiting for standard [" + WAIT_STANDARD + "] seconds\n" + e.getMessage());
		}
	}

	/**
	 * Generic method used to wait until element invisible with text
	 */
	public void waitForInvisibilityOfElement(By element) {
		debug("Waiting for the invisibility of [" + element + "]");
		newWait.until(ExpectedConditions.invisibilityOfElementLocated(element));
	}


	/**
	 * Generic method used to wait until element is clickable
	 */
	public void waitUntilElementIsClickable(WebElement element) {
		debug("Waiting for the clickable of [" + element + "]");
		newWait.until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * Generic method used to wait on a WebElements or a By to be Visible
	 *
	 * @param weLst A WebElements or By to wait on
	 * @throws Error to not catch ThreadDeath
	 */
	public void waitForAllElementsVisible(List<WebElement> weLst) {
		newWait.until(ExpectedConditions.visibilityOfAllElements(weLst));
	}

	public <T> void waitForVisibility(T element) {
		info("Waiting for the visibility of element [" + element + "]");
		if (element instanceof WebElement) {
			newWait.until(ExpectedConditions.visibilityOf((WebElement) element));
		} else if (element instanceof By) {
			newWait.until(ExpectedConditions.visibilityOf(findElement((By) element)));
		}
	}

	/**
	 * Generic method used to wait for a Modal to be Visible
	 */
	public void waitForAlertVisibility() {
		debug("Waiting for Modal Visibility ");
		newWait.until(ExpectedConditions.alertIsPresent());
	}


	/***
	 * Waits for the page to load, determined by retrieving
	 * document.readyState waiting 'timeout' seconds before trying again
	 */
	public void waitForPageToLoad() {
		debug("Waiting for the Page to Load");
		ExpectedCondition<Boolean> pageLoadCondition = driver ->
		{
			if (null == driver) {
				return false;
			}
			String complete = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
			return null != complete && complete.equalsIgnoreCase("complete");
		};
		newWait.until(pageLoadCondition);
	}

	/**
	 * Waits for the element to be present in the DOM
	 *
	 * @param locator The By locator strategy to use to find the element
	 */
	public void waitForPresenceOfElement(By locator) {
		info("waiting for presence of element : [" + locator + "]");
		newWait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/**
	 * Waits for the URL to contain the portion
	 *
	 * @param expectedInURL The string portion that is expected to be in the URL
	 * @return
	 */
	public void waitForURLToContain(String expectedInURL) {
		info("Waiting for URL to contain : [" + expectedInURL + "]");
		info("Current URL: [" + getPageURL() + "]");
		newWait.until(ExpectedConditions.urlContains(expectedInURL));
		waitStandard();
	}

	/**
	 * Waits for the URL to be
	 *
	 * @param url The url
	 */
	public void waitForURLToBe(String url) {
		info("waiting for URL to be : " + url);
		info("Current URL: " + getPageURL());
		newWait.until(ExpectedConditions.urlToBe(url));
	}

	/**
	 * Waits for an element to contain a certain value
	 *
	 * @param expectedValue The expected value
	 */
	public <T> void waitForValueOfElementToBe(T element, String expectedValue) {
		info("waiting for value of element to be : " + expectedValue);
		if (element instanceof WebElement) {
			newWait.until(ExpectedConditions.textToBePresentInElementValue((WebElement) element, expectedValue));
		} else if (element instanceof By) {
			newWait.until(ExpectedConditions.textToBePresentInElementValue(findElement((By) element), expectedValue));
		}
	}

	/**
	 * Waits for text to be present in an element
	 *
	 * @param expectedText The expected text
	 */
	public void waitForTextToBePresentInElement(By element, String expectedText) {
		info("waiting for text to be present : [" + expectedText + "] in element : [" + element + "]");
		newWait.until(ExpectedConditions.textToBePresentInElementLocated(element, expectedText));
	}

	/**
	 * Waits for text to match a pattern
	 *
	 * @param expectedPattern The expected pattern
	 */
	public void waitForTextToMatch(By element, Pattern expectedPattern) {
		info("waiting for text to match the pattern : [" + expectedPattern + "] in element : [" + element + "]");
		newWait.until(ExpectedConditions.textMatches(element, expectedPattern));
	}

	/**
	 * Waits for text to match a pattern
	 *
	 * @param <T> A String, int, By or WebElement representing the frame
	 */
	public <T> void waitForFrameAndSwitchToIt(T locator) {
		info("waiting for frame and switching to it");
		if (locator instanceof String) {
			newWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((String) locator));
		} else if (locator instanceof By) {
			newWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((By) locator));
		} else if (locator instanceof WebElement) {
			newWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((WebElement) locator));
		} else if (locator instanceof Integer) {
			newWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((Integer) locator));
		}
	}

	/**
	 * Waits for the presence of children in a parent
	 *
	 * @param parentLocator   The strategy for locating the parent
	 * @param childrenLocator The strategy for locating the children
	 */
	public void waitForPresenceOfChildrenInParent(By parentLocator, By childrenLocator) {
		info("waiting for the presence of children : [" + childrenLocator + "] in the parent : [" + parentLocator + "]");
		newWait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(parentLocator, childrenLocator));
	}

	/**
	 * Waits for the presence of child in a parent
	 *
	 * @param locator      The strategy for locating the parent
	 * @param childLocator The strategy for locating the child
	 */
	public void waitForPresenceOfChildInParent(WebElement locator, By childLocator) {
		info("waiting for the presence of the child : [" + childLocator + "] in the parent : [" + locator + "]");
		newWait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(locator, childLocator));
	}

	/**
	 * Waits for the element to be selected
	 *
	 * @param locator The strategy for locating the parent
	 */
	public <T> void waitForElementToBeSelected(T locator) {
		info("waiting for the element to be selected");
		if (locator instanceof By) {
			newWait.until(ExpectedConditions.elementToBeSelected((By) locator));
		} else if (locator instanceof WebElement) {
			newWait.until(ExpectedConditions.elementToBeSelected((WebElement) locator));
		}
	}

	/**
	 * Waits for the title of the page to contain the expected title
	 *
	 * @param expectedInTitle The text expected in the title
	 */
	public void waitForTitleToContain(String expectedInTitle) {
		info("waiting for the title to contain : " + expectedInTitle);
		newWait.until(ExpectedConditions.titleContains(expectedInTitle));
	}

	/**
	 * Waits for the number of windows to be the expected number
	 *
	 * @param numberOfWindows The number of windows that should be open
	 */
	public void waitForNumberOfWindowsToBe(int numberOfWindows) {
		info("waiting for the number of windows to be : " + numberOfWindows);
		newWait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
	}

	/**
	 * Waits for the number of elements to be greater than the expected number
	 *
	 * @param locator          The strategy to use to find the elements
	 * @param numberOfElements The number of windows that should be open
	 */
	public void waitForNumberOfElementsToBeGreaterThan(By locator, int numberOfElements) {
		info("waiting for the number of elements : [" + locator + "] to be greater than : [" + numberOfElements + "]");
		newWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, numberOfElements));
	}

	/**
	 * Waits for the number of elements to be less than the expected number
	 *
	 * @param locator          The strategy to use to find the elements
	 * @param numberOfElements The maximum number of elements that should be
	 */
	public void waitForNumberOfElementsToBeLessThan(By locator, int numberOfElements) {
		info("waiting for the number of elements : [" + locator + "] to be less than : [" + numberOfElements + "]");
		newWait.until(ExpectedConditions.numberOfElementsToBeLessThan(locator, numberOfElements));
	}


	public void waitForPresenceOfAllElements(By locator) {
		info("Waiting for all elements located by : [" + locator + "]");
		newWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}

	/**
	 * Wait for an element to become enabled
	 *
	 * @param locator The locator for the element
	 */
	public <T> void waitForElementToBeEnabled(T locator) {
		info("Waiting for the element to tbe enabled");
		ExpectedCondition<Boolean> elementEnabledCondition = driver ->
		{
			if (null == driver) {
				return false;
			}

			int retries = 0;
			WebElement element = null;
			if (locator instanceof WebElement) {
				element = (WebElement) locator;
			} else if (locator instanceof By) {
				element = findElement((By) locator);
			}

			// Waits for the element at the locator to become enabled
			// waiting 3 seconds between tries to establish the element is enabled
			// will retry 5 times before giving up and returning the status
			// total wait time for max wait is (3 X 5 = 15 seconds)
			while (!isEnabled(element) && retries < 4) {
				waitFor(3);
				retries++;
			}

			return isEnabled(element);
		};
		newWait.until(elementEnabledCondition);
	}

	/**
	 * Wait for an element to become disabled
	 *
	 * @param locator The locator for the element
	 */
	public <T> void waitForElementToBeDisabled(T locator) {
		info("Waiting for the element to tbe enabled");
		ExpectedCondition<Boolean> elementEnabledCondition = driver ->
		{
			if (null == driver) {
				return false;
			}

			int retries = 0;
			WebElement element = null;
			if (locator instanceof WebElement) {
				element = (WebElement) locator;
			} else if (locator instanceof By) {
				element = findElement((By) locator);
			}

			// Waits for the element at the locator to become disabled
			// waiting 3 seconds between tries to establish the element is disabled
			// will retry 5 times before giving up and returning the status
			// total wait time for max wait is (3 X 5 = 15 seconds)
			while (isEnabled(element) && retries < 4) {
				waitFor(3);
				retries++;
			}

			return !isEnabled(element);
		};
		newWait.until(elementEnabledCondition);
	}

	///////// BOTTOM OF WAITS //////////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets up the ArrayList of WebElements who are descendants of 'parent'
	 * collects requested children using the 'strategy' parameter.
	 * (i.e. Strategy = className, tagName, xpath, etc.)
	 *
	 * @param parent   - The WebElement parent of this list
	 * @param strategy - The strategy to use to ascertain children
	 * @return - The list of child WebElements determined by this method
	 */
	protected ArrayList<WebElement> setUpArrayListChildElementsUsing(WebElement parent, By strategy) {
		debug("Setting up an ArrayList<WebElement>");
		waitStandard();
		return (ArrayList<WebElement>) findElementsInChildren(parent, strategy);
	}

	/**
	 * Creates a LinkedHashMap whose rows are 'rowLength' using the
	 * elements of the ArrayList of WebElement 'parents'
	 *
	 * @param rowLength - The length of the rows in the map
	 * @param parents   - The parents ArrayList of elements to out into the map in rows
	 * @return {@link LinkedHashMap} of {@link Integer},{@link WebElement}
	 */
	protected LinkedHashMap<Integer, ArrayList<WebElement>>
	setUpLinkedHashMapUsing(int rowLength, ArrayList<WebElement> parents) {
		debug("Creating a LinkedHashMap<Integer, ArrayList<WebElement>> to process a table");
		waitStandard();
		// The map that will be returned by this method, stores the rows of elements
		LinkedHashMap<Integer, ArrayList<WebElement>> rows = new LinkedHashMap<Integer, ArrayList<WebElement>>();

		// The temporary row array list
		ArrayList<WebElement> rowList = new ArrayList<>();
		int rowCounter = 0;
		int rowSizeCounter = 0;

		if (null != parents) {
			// For each entry in the arrayList
			for (WebElement parent : parents) {

				rowSizeCounter++;

				if (rowSizeCounter == rowLength) {
					rowList.add(parent);

					rows.put(rowCounter++, rowList);

					rowList = new ArrayList<>();
					rowSizeCounter = 0;
				} else if (rowSizeCounter < rowLength) {
					rowList.add(parent);
				}
			}
		}

		// If there are still elements remaining that were not put into the map
		// before reaching the end of the parents arrayList size in the for loop;
		// then add these to the map as well before exiting this method
		if (!rowList.isEmpty()) {
			rows.put(rowCounter, rowList);
		}

		return rows;
	}

	/**
	 * Retrieve the size of the list of table rows
	 *
	 * @param table - The table containing the rows
	 * @return {@link String }
	 */
	protected String getNumberOfRowsInTable(WebElement table) {
		debug("Retrieving the size of the list of table rows for the table");
		if (isDisplayed(table)) {
			waitStandard();
			//populate an arraylist of table rows
			ArrayList<WebElement> tableRows = setUpArrayListChildElementsUsing(table, By.tagName("tr"));
			return String.valueOf(tableRows.size());
		}
		return "0";
	}

	/**
	 * Retrieve the size of the list of table rows
	 *
	 * @param table - The table containing the rows
	 */
	protected String getNumberOfElementsInTableRow(WebElement table, String row) {
		debug("Retrieving the size of the list of table rows for the table");
		if (isDisplayed(table)) {
			waitStandard();
			//populate an arraylist of table rows
			ArrayList<WebElement> tableRows = setUpArrayListChildElementsUsing(table, By.tagName("tr"));

			// One "table row - <tr>" per row in the linked hash map
			int tableRowsLength = 1;

			// sort that arraylist into a LinkedHashMap in order to index it
			LinkedHashMap<Integer, ArrayList<WebElement>> tableRowsData
					= setUpLinkedHashMapUsing(tableRowsLength, tableRows);

			int rowValue = (Integer.parseInt(row) > 0) ? Integer.parseInt(row) - 1 : Integer.parseInt(row);

			// in the map obtain the table row data for the rowValue
			for (Entry<Integer, ArrayList<WebElement>> e : tableRowsData.entrySet()) {
				if (e.getKey().equals(rowValue)) {
					ArrayList<WebElement> tableData =
							setUpArrayListChildElementsUsing(e.getValue().get(0), By.tagName("td"));
					return String.valueOf(tableData.size());
				}
			}
		}
		return "0";
	}

	private void makeScreenshotsFolder() {
		debug("Checking / Making the Screenshots Folder");
		// Create Screenshots Folder if it doesn't exist already
		File screenshotsFolder = new File(SCREENSHOTS_FOLDER_PATH);
		if (!screenshotsFolder.exists()) {
			screenshotsFolder.mkdir();
		}
	}

	/**
	 * Takes a screenshot and saves the file to the screenshots folder
	 *
	 * @param fileName - The name to use when naming this file
	 * @return - The path to the screenshot that was just taken
	 */
	public String takeScreenshot(String folderName, String fileName) {
		debug("Taking a screenshot");
		makeScreenshotsFolder();
		TakesScreenshot scrn = (TakesScreenshot) driver;
		File scrnShot = scrn.getScreenshotAs(OutputType.FILE);
		File destinationFolder = new File(SCREENSHOTS_FOLDER_PATH + folderName + "/");

		if (!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}

		File destinationFile = new File(SCREENSHOTS_FOLDER_PATH + folderName + "/" + fileName);

		copyFileUsingStream(scrnShot, destinationFile);

		return SCREENSHOTS_FOLDER_PATH + folderName + "/" + fileName;
	}


	/**
	 * Copies a file to a system directory using an output stream
	 *
	 * @param source - The source File to write to disk
	 * @param dest   - The destination file to save to disk
	 */
	private void copyFileUsingStream(File source, File dest) {
		InputStream is = null;
		OutputStream os = null;

		try {
			is = Files.newInputStream(source.toPath());
			os = Files.newOutputStream(dest.toPath());
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} catch (IOException e) {
			warn("SourceFile:" + source.getPath() + "\n" + "Dest File:" + dest.getPath());
			error("\nFailed to copy file to destination.\n" + e.getMessage());
		} finally {
			try {
				if (null != is) {
					is.close();
				}
			} catch (IOException e) {
				error("\nFailed to close the input stream after copying file.\n" + e.getMessage());
			}
			try {
				if (null != os) {
					os.close();
				}
			} catch (IOException e) {
				error("\nFailed to close output stream after copying file.\n" + e.getMessage());
			}
		}
	}

	/**
	 * Opens a new tab in the current browser window
	 */
	public void newTab() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.open('your URL', '_blank');");
	}

	/**
	 * Returns a WebElement located using the unique locator
	 *
	 * @param locator a unique locator
	 * @return {@link WebElement }
	 */
	public WebElement findElement(By locator) {
		WebElement element = null;
		try {
			element = driver.findElement(locator);
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("Could not find the element by locator : [" + locator + "]", e);
		}
		return element;
	}

	/**
	 * Returns a List of WebElement's located using a unique locator
	 *
	 * @param locator the unique locator
	 * @return {@link List<WebElement>}
	 */
	public List<WebElement> findElements(By locator) {
		List<WebElement> elements = null;
		try {
			elements = driver.findElements(locator);
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("Could not find the elements with the locator : [" + locator + "]", e);
		}
		return elements;
	}

	/**
	 * Returns a WebElement located by searching in the children of a parent element
	 * using the unique locator
	 * Can also be used to search for siblings
	 *
	 * @param parent  the parent element
	 * @param locator the child's unique locator
	 * @return {@link WebElement }
	 */
	public WebElement findElementInChildren(WebElement parent, By locator) {
		WebElement element = null;
		try {
			element = parent.findElement(locator);
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element with : [" + locator + "] could not be found in the parent : [" + parent + "]", e);
		}
		return element;
	}


	/**
	 * Returns a List of WebElement's located by searching in the children of a parent element
	 * using a unique locator
	 *
	 * @param parent  the parent WebElement
	 * @param locator the unique locator
	 * @return {@link List<WebElement>}
	 */
	public List<WebElement> findElementsInChildren(WebElement parent, By locator) {
		List<WebElement> elements = null;
		try {
			elements = parent.findElements(locator);
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("Could not find the elements in the children of the element : ["
					+ parent + "] with locator : [" + locator + "]", e);
		}
		return elements;
	}

	/**
	 * Returns whether the 'element' is enabled
	 *
	 * @param element the element
	 * @param <T>     either a 'By' or a 'WebElement'
	 * @return {@link Boolean }
	 */
	public <T> Boolean isEnabled(T element) {
		try {
			if (element instanceof WebElement) {
				return ((WebElement) element).isEnabled();
			} else {
				return findElement((By) element).isEnabled();
			}
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element : [" + element + "] could not be found", e);
		}
		return false;
	}

	/**
	 * Returns whether the 'element' is selected
	 *
	 * @param element the element
	 * @param <T>     either a 'By' or a 'WebElement'
	 * @return {@link Boolean }
	 */
	public <T> Boolean isSelected(T element) {
		try {
			if (element instanceof WebElement) {
				scrollIntoView((WebElement) element);
				return ((WebElement) element).isSelected();
			} else {
				scrollIntoView(findElement((By) element));
				return findElement((By) element).isSelected();
			}
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element : [" + element + "] could not be found", e);
		}
		return false;
	}

	/**
	 * Returns whether the 'element' is displayed
	 *
	 * @param element the element
	 * @param <T>     either a 'By' or a 'WebElement'
	 * @return {@link Boolean }
	 */
	public <T> Boolean isDisplayed(T element) {
		try {
			if (element instanceof WebElement) {
				return ((WebElement) element).isDisplayed();
			} else {
				return findElement((By) element).isDisplayed();
			}
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element : [" + element + "] could not be found", e);
		}
		return false;
	}

	/**
	 * Hover on element
	 *
	 * @param element
	 */
	public void hoverOn(WebElement element) {
		//Creating object of an Actions class
		Actions action = new Actions(driver);

		//Performing the mouse hover action on the target element.
		action.moveToElement(element).perform();
	}

	/**
	 * Clicks a WebElement
	 *
	 * @param element a clickable web element
	 * @param <T>     Either a 'By' or a 'WebElement'
	 */
	public <T> void click(T element) {
		if (null != element) {
			if (element instanceof WebElement) {
				if (((WebElement) element).isEnabled()) {
					try {
						scrollIntoView((WebElement) element);
						((WebElement) element).click();
					} catch (InvalidElementStateException
							 | StaleElementReferenceException
							 | TimeoutException
							 | NoSuchElementException
							 | UnhandledAlertException e) {
						error("An Exception was caught while trying to click the element", e);
					}
				} else {
					error("The WebElement with locator: [" + element + "] was not enabled");
				}
			} else {
				WebElement byElement = findElement((By) element);
				if (byElement.isEnabled()) {
					try {
						scrollIntoView(byElement);
						byElement.click();
						waitForPageToLoad();
					} catch (InvalidElementStateException
							 | StaleElementReferenceException
							 | TimeoutException
							 | NoSuchElementException
							 | UnhandledAlertException e) {
						error("An Exception was caught while trying to click the element [" + element + "]", e);
					}
				} else {
					error("The By element with locator: [" + element + "] was not enabled");
				}
			}
		}
	}

	/**
	 * Press Enter
	 */
	public <T> void pressEnter(T element) {
		if (element instanceof WebElement) {
			((WebElement) element).sendKeys(Keys.RETURN);
		} else if (element instanceof By) {
			WebElement tempElement = findElement((By) element);
			tempElement.sendKeys(Keys.RETURN);
		}
		waitStandard();
	}

	/**
	 * Click the Element as it is
	 *
	 * @param element The element to click
	 * @param <T>     The type of the element (WebElement, By)
	 */
	public <T> void clickRaw(T element) {
		if (element instanceof WebElement) {
			try {
				((WebElement) element).click();
			} catch (InvalidElementStateException
					 | StaleElementReferenceException
					 | TimeoutException
					 | NoSuchElementException
					 | UnhandledAlertException e) {
				error("An Exception was caught while trying to click the element [" + element + "]", e);
			}
		} else if (element instanceof By) {
			try {
				WebElement byElement = findElement(((By) element));
				byElement.click();
			} catch (InvalidElementStateException
					 | StaleElementReferenceException
					 | TimeoutException
					 | NoSuchElementException
					 | UnhandledAlertException e) {
				error("An Exception was caught while trying to click the element [" + element + "]", e);
			}
		}
		waitStandard();
	}


	/**
	 * Enters Input into a WebElement
	 *
	 * @param element the WebElement
	 * @param input   the input
	 * @param <T>     either a 'By' or a 'WebElement'
	 */
	public <T> void input(T element, String input) {
		if (null != element) {
			try {
				if (element instanceof WebElement) {
					scrollIntoView((WebElement) element);
					if (isDisplayed(element)) {
						((WebElement) element).click();
						((WebElement) element).clear();
						((WebElement) element).sendKeys(input);
					}
				} else if (element instanceof By) {
					WebElement tempElement = findElement((By) element);
					scrollIntoView(tempElement);
					if (isDisplayed(tempElement)) {
						tempElement.click();
						tempElement.clear();
						tempElement.sendKeys(input);
					}
				}
			} catch (InvalidElementStateException
					 | StaleElementReferenceException
					 | TimeoutException
					 | NoSuchElementException e) {
				error("The element : [" + element + "] could not be found", e);
			}
		}
	}

	/**
	 * Enters Input into a WebElement
	 * (Does not click or clear the element first)
	 *
	 * @param element the WebElement
	 * @param input   the input
	 * @param <T>     either a 'By' or a 'WebElement'
	 */
	public <T> void inputRaw(T element, String input) {
		if (null != element) {
			try {
				if (element instanceof WebElement) {
					((WebElement) element).sendKeys(input);
				} else if (element instanceof By) {
					WebElement tempElement = findElement((By) element);
					tempElement.sendKeys(input);
				}
			} catch (InvalidElementStateException
					 | StaleElementReferenceException
					 | TimeoutException
					 | NoSuchElementException e) {
				error("The element : [" + element + "] could not be found", e);
			}
		}
	}


	/**
	 * Enter File Path into a WebElement
	 *
	 * @param element  The element to enter the file path into
	 * @param filePath The file path to enter
	 * @param <T>      Either a WebElement or a By
	 */
	public <T> void inputFilePath(T element, String filePath) {
		File file = new File(TEST_ASSETS_FOLDER_PATH + filePath);

		String path = file.getPath();
		info("Sending the following filepath : [" + path + "]");

		try {
			if (element instanceof WebElement) {
				((WebElement) element).sendKeys(path);
			} else {
				WebElement tempElement = findElement((By) element);
				tempElement.sendKeys(path);
			}
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element : [" + element + "] could not be found", e);
		}
	}

	/**
	 * Retrieves the Text from a WebElement
	 *
	 * @param element the WebElement
	 * @param <T>     either a 'By' or a 'WebElement'
	 * @return {@link String }
	 */
	public <T> String getText(T element) {
		try {
			if (null != element) {
				if (element instanceof WebElement) {
					String returnText = ((WebElement) element).getText();
					if (null == returnText || returnText.length() == 0) {
						returnText = getAttribute((WebElement) element, "innerText");
						if (null == returnText || returnText.length() == 0) {
							returnText = getAttribute((WebElement) element, "innerHTML");
							if (null == returnText || returnText.length() == 0) {
								returnText = getAttribute((WebElement) element, "value");
							}
						}
					}
					return returnText;
				} else {
					WebElement byElement = findElement((By) element);
					String returnText = byElement.getText();
					if (null == returnText || returnText.length() == 0) {
						returnText = getAttribute(byElement, "innerText");
						if (null == returnText || returnText.length() == 0) {
							returnText = getAttribute(byElement, "innerHTML");
							if (null == returnText || returnText.length() == 0) {
								returnText = getAttribute(byElement, "value");
							}
						}
					}
					return returnText;
				}
			}
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element : [" + element + "] could not be found", e);
		}
		return null;
	}

	/**
	 * Retrieves an attribute from a WebElement
	 *
	 * @param element   the WebElement
	 * @param attribute the Attribute
	 * @param <T>       either a 'By' or a 'WebElement'
	 * @return {@link String }
	 */
	public <T> String getAttribute(T element, String attribute) {
		try {
			if (null != element) {
				if (element instanceof WebElement) {
					return ((WebElement) element).getAttribute(attribute);
				} else {
					return findElement((By) element).getAttribute(attribute);
				}
			}
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element : [" + element + "] could not be found", e);
		}
		return null;
	}

	public <T> String getAttributeWithJavaScript(T element, String attribute) {
		info("Getting the attribute [" + attribute + "] from the element with JavaScript");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (String) js.executeScript("return arguments[0].getAttribute(arguments[1]);",
				element, attribute);
	}

	/**
	 * Select an option from a Select Dropdown using the visible text
	 *
	 * @param element     the WebElement
	 * @param visibleText the Visible Text
	 * @param <T>         either a 'By' or a 'WebElement'
	 */
	public <T> void selectByVisibleText(T element, String visibleText) {
		debug("Selecting " + visibleText + " from the list");
		Select select = null;
		if (element instanceof WebElement) {
			if (isDisplayed((WebElement) element)) {
				select = new Select((WebElement) element);
			} else {
				info("The select element is not displayed");
			}
		} else if (element instanceof By) {
			WebElement tempElement = findElement((By) element);
			if (isDisplayed(tempElement)) {
				select = new Select(tempElement);
			} else {
				info("The select element is not displayed");
			}
		}
		waitStandard();
		if (null != select) {
			try {
				List<WebElement> options = select.getOptions();

				// For debugging purposes only
				debug("The available options are :");
				for (WebElement e : options) {
					debug(getText(e));
				}

				// Avoids the problem options being case-sensitive
				for (WebElement option : options) {
					debug(getText(option));
					if (getText(option).trim().equalsIgnoreCase(visibleText)) {
						select.selectByVisibleText(visibleText);
						waitStandard();
						break;
					}
				}
			} catch (InvalidElementStateException
					 | StaleElementReferenceException
					 | TimeoutException
					 | NoSuchElementException e) {
				error("The 'option' : [" + visibleText
						+ "] was not available in the Select element : [" + element + "]", e);
			}
		}
	}

	public <T> void selectByValue(T selectElement, String value) {
		info("Selecting by value : " + value);
		Select select = null;
		if (selectElement instanceof WebElement) {
			select = new Select((WebElement) selectElement);
		} else if (selectElement instanceof By) {
			select = new Select(findElement((By) selectElement));
		}
		if (null != select) {
			try {
				select.selectByValue(value);
			} catch (InvalidElementStateException
					 | StaleElementReferenceException
					 | TimeoutException
					 | NoSuchElementException e) {
				error("The 'value' : [" + value
						+ "] was not available in the Select element : [" + selectElement + "]", e);
			}
		}
	}

	/**
	 * Returns a List of WebElements representing the selected options in the
	 * Select dropdown
	 *
	 * @param xpath Path to the Select WebElement
	 * @return {@link List<WebElement> }
	 */
	protected List<WebElement> getSelectedOptions(String xpath) {
		debug("Returning Selected Options from " + xpath);
		waitStandard();
		Select select = null;
		try {
			select = new Select(driver.findElement(By.xpath(xpath)));
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element at XPATH : [" + xpath + "] could not be found", e);
		}
		waitStandard();
		return null != select ? select.getAllSelectedOptions() : null;
	}

	/**
	 * Returns a List of WebElements representing the selected options in the
	 * Select dropdown
	 *
	 * @param xpath Path to the Select WebElement
	 * @return {@link List<WebElement> }
	 */
	protected List<WebElement> getSelectOptions(String xpath) {
		debug("Returning Selected Options from " + xpath);
		waitStandard();
		Select select = null;
		try {
			select = new Select(driver.findElement(By.xpath(xpath)));
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element at XPATH : [" + xpath + "] could not be found", e);
		}
		waitStandard();
		return null != select ? select.getOptions() : null;

	}

	/**
	 * Navigate to a URL
	 *
	 * @param url the URL
	 */
	public void goToURL(String url) {
		if (null != url && !url.isEmpty()) {
			info("Go to URL : [" + url + "]");
			driver.navigate().to(url);
		}
	}

	/***
	 * Returns the URL of the current page
	 * @return the URL of this page
	 */
	public String getPageURL() {
		String currentURL = driver.getCurrentUrl();
		debug("Returning Current URL " + currentURL);
		waitStandard();
		return currentURL;
	}

	/***
	 * Returns the Title of the page
	 * @return the title of the current page
	 */
	public String getTitle() {
		String title = driver.getTitle();
		debug("Returning Title of Page " + title);
		waitStandard();
		waitForPageToLoad();
		return title;
	}

	/**
	 * Refresh the Current window (Same as pressing F5)
	 */
	public void refreshPage() {
		driver.navigate().refresh();
	}

	/**
	 * Add a class to an element
	 *
	 * @param element   The element to add the class to
	 * @param className The class to add
	 */
	public void addToClassList(WebElement element, String className) {
		try {
			debug("Adding : " + className + " to element : " + element.toString());
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].classList.add(arguments[1])", element, className);
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element : [" + element + "] could not be found", e);
		}
	}

	/**
	 * Sets the 'attribute' of the 'element' to the 'value'
	 *
	 * @param element   - The element with the attribute
	 * @param attribute - The Attribute
	 * @param value     - The value
	 */
	protected void setAttribute(WebElement element, String attribute, String value) {
		try {
			debug("Setting Attribute " + attribute + " to " + value);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attribute, value);
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element : [" + element + "] could not be found", e);
		}
	}

	/**
	 * Removes the 'attribute' from the 'element'
	 *
	 * @param element   - The element to remove an attribute from
	 * @param attribute - The attribute to remove
	 */
	protected void removeAttribute(WebElement element, String attribute) {
		try {
			debug("Removing Attribute " + attribute + " from " + element.toString());
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].removeAttribute(arguments[1]);", element, attribute);
			waitStandard();
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element : [" + element + "] could not be found", e);
		}
	}

	/**
	 * Removes the 'element' from the DOM
	 *
	 * @param element - The element to remove from the DOM
	 */
	protected void removeElement(WebElement element) {
		try {
			debug("Removing Element " + element + " from the DOM");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("return arguments[0].remove();", element);
			waitStandard();
		} catch (InvalidElementStateException
				 | StaleElementReferenceException
				 | TimeoutException
				 | NoSuchElementException e) {
			error("The element : [" + element + "] could not be found", e);
		}
	}

	/**
	 * Write a Debug statement from inside a TestNG test
	 * during runtime
	 *
	 * @param message the Message to write to debug
	 */
	public void message(String message) {
		info("*** DEBUG MESSAGE: " + message);
	}

	/**
	 * Determine if the Export Window was opened
	 *
	 * @return {@link Boolean }
	 */
	public Boolean didExportWindowOpen() {
		info("Determining if the Export Window opened");
		waitStandard();
		Set<String> windowHandles = driver.getWindowHandles();

		// For all the Windows that are currently open (Counts tabs as windows)
		if (windowHandles.size() > 1) {
			ArrayList<String> windowTitles = new ArrayList<>();
			info("The following windows are open : ");

			// Collect the Title of each window
			for (String s : windowHandles) {
				windowTitles.add(driver.switchTo().window(s).getTitle());
				info("Window : " + driver.switchTo().window(s).getTitle());
			}

			// Compare the title of each window to the expected title
			for (int i = 0; i < windowTitles.size(); i++) {
				if (windowTitles.get(i).contains("Queued Report")) {
					driver.switchTo().window((String) windowHandles.toArray()[i]).close();
					driver.switchTo().window(windowHandle);
					return true;
				}
			}
		}
		driver.switchTo().window(windowHandle);
		return false;
	}

}
