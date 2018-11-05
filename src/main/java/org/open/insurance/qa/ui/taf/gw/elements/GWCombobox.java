package org.open.insurance.qa.ui.taf.gw.elements;

import java.util.List;

import org.junit.Assert;
import org.open.insurance.qa.ui.taf.gw.finder.GWBrFinder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.baloise.testautomation.taf.browser.elements.BrStringInput;

public class GWCombobox extends BrStringInput {

  @Override
  public void checkCustom() {
    String custom = checkValue.getCustom();
    if ("{notvisible}".equalsIgnoreCase(custom)) {
      Long timeoutInMsecs = component.getBrowserFinder().getTimeoutInMsecs();
      try {
        component.getBrowserFinder().setTimeoutInMsecs(200L);
        find();
      }
      catch (Throwable t) {
        return;
      }
      finally {
        component.getBrowserFinder().setTimeoutInMsecs(timeoutInMsecs);
      }
      Assert.fail("Component ist visible but should NOT: " + name);
    }
    Assert.fail("Command not implemented yet: " + name + " --> " + checkValue.getCustom());
  }

  @Override
  public void fill() {
    if (!canFill()) {
      return;
    }
    GWBrFinder finder = (GWBrFinder)component.getBrowserFinder();
    long timeout = finder.getTimeoutInMsecs();
    boolean filled = false;
    try {
      finder.setTimeoutInMsecs(2000L);
      for (int i = 0; i < 5; i++) {
        if (tryToFill()) {
          filled = true;
          break;
        }
        else {
          try {
            Thread.sleep(1000);
          }
          catch (Exception e) {}
        }
      }
    }
    finally {
      finder.setTimeoutInMsecs(timeout);
    }
    Assert.assertTrue("Not correctly filled: " + name, filled);
  }

  private void log(String s) {
    System.out.println(s);
  }

  private boolean tryToFill() {
    String xpath = "./../..//div[contains(@class, 'x-form-arrow-trigger')]";
    if ("{checkthatnotexists}".equalsIgnoreCase(fillValueAsString())) {
      try {
        log("Try to fill: " + name + " --> " + fillValueAsString());
        find().findElement(By.xpath(xpath));
        Assert.fail("Element exists but should NOT: " + name);
      }
      catch (Throwable t) {
        if (t.getMessage().startsWith("Element exists")) {
          throw t;
        }
        else {
          log("Element '" + name + "' not found --> this is the expected behavior");
        }
      }
      return true;
    }
    try {
      if (canFill()) {
        log("Try to fill: " + name + " --> " + fillValueAsString());
        String text = find().getAttribute("value");

        if (text != null) {
          if (text.equals(fillValueAsString())) {
            log("Combobox is already filled correctly with: " + text + " --> no further action");
            return true;
          }
        }

        find().click();
        ((GWBrFinder)component.getBrowserFinder()).waitUntilLoadingComplete();
        List<WebElement> lis = getDriver().findElements(
            By.xpath("//div[not(contains(@class, 'x-unselectable')) and not(contains(@style, 'display: none'))]//li"));
        for (WebElement li : lis) {
          if (fillValueAsString().equals(li.getText())) {
            log("List entry found -> select directly");
            li.click();
            Assert.assertTrue("List entry was not selected correctly (no exact match)",
                fillValueAsString().trim().equals(find().getAttribute("value").trim()));
            return true;
          }
          if (li.getText().startsWith(fillValueAsString())) {
            log("List entry found with text starting according to text in datapool --> select directly");
            li.click();
            Assert.assertTrue("List entry was not selected correctly (text does not match starting text from datapool)",
                find().getAttribute("value").trim().startsWith(fillValueAsString().trim()));
            return true;
          }
        }
        log("No matching list entry found --> enter value directy as string into combobox input field");
        find().click();
        find().clear();
        find().sendKeys(fillValueAsString());
        // Click on blue list element... (instead of Enter, because this would go wrong in case more than one
        // entry start with the same text...
        getDriver().findElement(By.xpath("//li[contains(@class, x-boundlist-item-over)]")).click();
      }
    }
    catch (Throwable t) {
      t.printStackTrace();
      return false;
    }
    return true;
  }

}
