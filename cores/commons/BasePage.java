package commons;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BasePage {
	//1- Access Modifier: public
	//2 - Kieu tra ve cua ham
	//2.1 - void - Action (Click, clear, sendkey/ open)
	//2.2 - lấy dữ liệu ra : #void: Srtinh/int/boolean/ object
	// getXXX : getCurrentUrl/ getTitle / getCssValue / getText/ getAttribute/ getSize/ ...
	//3. Tên hàm
	// 3.1 Tính năng này dùng làm gì -> Tên
	//4- Tham số truyền vào
	// Khai báo 1 biến bên trong : kiểu dữ liệu - tên dữ liệu : String addressName, String pageUrl
	//5- Kiểu dữ liệu trả về trong hàm - tương ứng với kiểu trả về của hàm
	//5.1 void : không cần return
	//5.1 $#void thì return đúng capacity
	// Note: 
	// 1 - Tham so dau tien bat buoc cua 1 ham


	/**
	 * Open any page Url
	 * @author QuanNT
	 * @param driver
	 * @param pageUrl
	 */
	public static BasePage getBasePageInstance() {
		return new BasePage();
	}
	public void openPageUrl(WebDriver driver, String pageUrl) {
		driver.get(pageUrl);
	}
	
	/**
	 * get page Url
	 * @param driver
	 */
	public String getPageUrl(WebDriver driver) {
		return driver.getCurrentUrl();
	}
	public String getPageTitle(WebDriver driver) {
		return driver.getTitle();
	}
	public String getPageSourceCode(WebDriver driver) {
		 return driver.getPageSource();
	}
	public void backToPage(WebDriver driver) {
		driver.navigate().back();
	}
	public void forwardToPage(WebDriver driver) {
		driver.navigate().forward();
	}
	public void refreshToPage(WebDriver driver) {
		driver.navigate().refresh();
	}
	public Alert waitForAlertPresence(WebDriver driver) {
		return new WebDriverWait(driver, longTimeout).until(ExpectedConditions.alertIsPresent());
	}
	public void acceptAlert(WebDriver driver) {
		waitForAlertPresence(driver).accept();
	}
	public void canceltAlert(WebDriver driver) {
		waitForAlertPresence(driver).dismiss();
	}
	public void sendkeyToAlert(WebDriver driver, String valueToSendKey) {
		waitForAlertPresence(driver).sendKeys(valueToSendKey);
	}
	public String getAlertText(WebDriver driver) {
		return waitForAlertPresence(driver).getText();
	}
	public void switchToWindowByID(WebDriver driver, String expectedID) {
		Set<String> allTabIDs = driver.getWindowHandles();
		for (String id : allTabIDs) {
			if(!id.equals(expectedID)) {
				driver.switchTo().window(id);
				break;
			}
		}
	}
	public void switchToWindowyTitle(WebDriver driver, String expectedTitle) {
		Set<String> allTabIDs = driver.getWindowHandles();
		for (String id : allTabIDs)
		{
			driver.switchTo().window(id);
			String actualTitle = driver.getTitle();
			if (actualTitle.equals(expectedTitle)) {
				break;
			}
		}
	}
	public boolean closeAllWindowsWithoutParent(WebDriver driver, String parentID) {
		Set<String> allWindows = driver.getWindowHandles();
		for(String runWindows : allWindows) {
			if(!runWindows.equals(parentID)) {
				driver.switchTo().window(runWindows);
				driver.close();
			}
		}
		driver.switchTo().window(parentID);
		if (driver.getWindowHandles().size()==1)
			return true;
		else
			return false;
	}
	
	/* Web Element */
	//Note:
	//1- Tham số đầu tiên bắt buộc của 1 hàm tương tác với Web Browser là "Web Driver"
	//2- Tham số thứ 2 bắt buộc của 1 hàm tương tác vs Web Element là "String locator"
	//3 - Những step nào có dùng element lại >= 2 lần trở lên -> khai báo biến local
	// 4- Verify true/false
	// Các hàm kiểu boolean luôn có tiền tố is
	//isDisplay/isEnable/isSelected/isMultiple
	
	public By getByXpath(String locator) {
		return By.xpath(locator);
	}
	public WebElement getWebElement(WebDriver driver, String locator) {
		return driver.findElement(getByXpath(locator));
	}
	public List<WebElement> getListElement(WebDriver driver, String locator) {
		return driver.findElements(getByXpath(locator));
	}
	
	// Hàm dùng chung để click vào bất kỳ 1 element nào đó
	public void clickToElement(WebDriver driver, String locator) {
		getWebElement(driver, locator).click();
	}
	public void sendKeyToElement(WebDriver driver, String locator, String valueToInput) {
		WebElement element = getWebElement(driver, locator);
		element.clear();
		element.sendKeys(valueToInput);
	}
	public String getElementText(WebDriver driver, String locator) {
		return getWebElement(driver, locator).getText();
	}
	public void selectItemInDefaultDropdown(WebDriver driver, String locator, String itemText) {
		Select select = new Select(getWebElement(driver, locator));
		select.selectByVisibleText(itemText);
		
	}
	public String getFirstSelectedTextItem(WebDriver driver, String locator) {
		Select select = new Select(getWebElement(driver, locator));
		return select.getFirstSelectedOption().getText();
		
	}
	public boolean isDropdownMultiple(WebDriver driver, String locator) {
		Select select = new Select(getWebElement(driver, locator));
		return select.isMultiple();
		
	}
	public void selectItemInCustomDropdown(WebDriver driver, String parentXpath, String childXpath, String expectedItemText) {
		getWebElement(driver, parentXpath).click();
		sleepInSecond(3);		
		// chờ cho các item được load lên / hiển thị thành công
		List<WebElement> childItems = new WebDriverWait(driver, longTimeout).until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByXpath(childXpath)));		
		// Duyệt qua từng item để kiểm tra đúng với cái mình chọn hay không
		for (WebElement tempElement : childItems) {
			if (tempElement.getText().trim().equals(expectedItemText)) {
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(false);", tempElement);
				sleepInSecond(1);
				tempElement.click();
				sleepInSecond(2);
				break;
				
			}
		}
	}
	public String getElementAttriute(WebDriver driver, String locator, String attributeName) {
		return getWebElement(driver, locator).getAttribute(attributeName);
		
	}
	public String getElementCssValue(WebDriver driver, String locator, String propertyName) {
		return getWebElement(driver, locator).getCssValue(propertyName);
		
	}
	public int getListElementSize(WebDriver driver, String locator) {
		return getListElement(driver, locator).size();
	}
	public void checkToCheckboxOrRadio(WebDriver driver, String locator) {
		WebElement element = getWebElement(driver, locator);
		if(!element.isSelected()) {
			element.click();
		}		
	}
	public void uncheckToCheckbox(WebDriver driver, String locator) {
		WebElement element = getWebElement(driver, locator);
		if(element.isSelected()) {
			element.click();
		}
	}
	public boolean isElementDisplayed(WebDriver driver, String locator) {
		return getWebElement(driver, locator).isDisplayed();
	}
	public boolean isElementEnaled(WebDriver driver, String locator) {
		return getWebElement(driver, locator).isEnabled();
	}
	public boolean isElementSelected(WebDriver driver, String locator) {
		return getWebElement(driver, locator).isSelected();
	}
	public void switchToIframe(WebDriver driver, String locator) {
		driver.switchTo().frame(getWebElement(driver, locator));
	}
	public void switchToDefaultContent(WebDriver driver) {
		driver.switchTo().defaultContent();
	}
	public void hoverMouseToElement(WebDriver driver, String locator) {
		Actions action = new Actions(driver);
		action.moveToElement(getWebElement(driver, locator)).perform();
	}
	public void rightClickToElement(WebDriver driver, String locator) {
		Actions action = new Actions(driver);
		action.contextClick(getWebElement(driver, locator)).perform();
	}
	public void doubleClickToElement(WebDriver driver, String locator) {
		Actions action = new Actions(driver);
		action.doubleClick(getWebElement(driver, locator)).perform();
	}
	public void dragAndDropElement(WebDriver driver, String sourceLocator, String tagetLocator) {
		Actions action = new Actions(driver);
		action.dragAndDrop(getWebElement(driver, sourceLocator), getWebElement(driver, tagetLocator)).perform();
		
	}
	public void pressKeyToElement(WebDriver driver, String locator, Keys key) {
		Actions action = new Actions(driver);
		action.sendKeys(getWebElement(driver, locator), key).perform();
		
	}
	public void hightlightElement(WebDriver driver, String locator) {
		WebElement element = getWebElement(driver, locator);
		String originalStyle = element.getAttribute("style");
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttriute('style', arguments[1])", element,"border:2px solid red; order-style: dashed;");
		sleepInSecond(2);
		jsExecutor.executeScript("arguments[0].setAttriute('style', arguments[1])", element, originalStyle);
	}
	public void clickToElementByJS(WebDriver driver, String locator) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", getWebElement(driver, locator));
	}
	public void scrollToElementOnTop(WebDriver driver, String locator) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, locator));
	}
	public void scrollToElementOnDown(WebDriver driver, String locator) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", getWebElement(driver, locator));
	}
	public void sendkeyToElementByJS(WebDriver driver, String locator, String value) {
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value',"+value + "')", getWebElement(driver, locator));
	}
	public void removeAttributeInDOM(WebDriver driver, String locator, String attributeRemove) {
		((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('"+ attributeRemove + "')", getWebElement(driver, locator));
	}
	public String getElementValidationMessage(WebDriver driver, String locator) {
		return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].validationMessage", getWebElement(driver, locator));
	}
//	public void isImageLoaded(WebDriver driver, String locator) {
//		boolean status = (boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].complete && typeof arguments[0 ]", null)
//	}
	public void waitForElementVisible(WebDriver driver, String locator) {
		new WebDriverWait(driver, longTimeout).until(ExpectedConditions.visibilityOfElementLocated(getByXpath(locator)));
	}
	public void waitForElementInvisible(WebDriver driver, String locator) {
		new WebDriverWait(driver, longTimeout).until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(locator)));
	}
	public void waitForElementClickable(WebDriver driver, String locator) {
		new WebDriverWait(driver, longTimeout).until(ExpectedConditions.elementToBeClickable(getByXpath(locator)));
	}
	
	
	public void sleepInSecond(long timeInSecond) {
		try {
		Thread.sleep(timeInSecond * 1000);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private long longTimeout = 30;
//	private long shotTimeout = 5;
}
