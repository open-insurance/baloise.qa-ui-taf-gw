package org.open.insurance.baloise.qa.ui.taf.gw.elements;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.baloise.testautomation.taf.base.types.TafString;
import com.baloise.testautomation.taf.browser.elements.BrStringInput;

public class GWStringInput extends BrStringInput {

  @Override
  public void fillCustom() {
    String action = fillValue.getCustom();
    if (action.equals("{notvisible}")) {
      Long timeoutInMsecs = component.getBrowserFinder().getTimeoutInMsecs();
      try {
        component.getBrowserFinder().setTimeoutInMsecs(200L);
        find();
      }
      catch (Throwable e) {
        return;
      }
      finally {
        component.getBrowserFinder().setTimeoutInMsecs(timeoutInMsecs);
      }
      Assert.fail("element was found but should NOT: " + name);
    }
    if (action.startsWith("{isreadonly}")) {
      doIsReadOnly(action);
      return;
    }
    if (action.startsWith("{isfilledwith}")) {
      String value = action.replace("{isfilledwith}", "");
      WebElement element = find();
      Assert.assertEquals("Element is not correctly filled: " + name, value, element.getAttribute("value"));
      return;
    }
    if (action.equals("{isdisabled}")) {
      Assert.assertFalse("String input is enabled, but should be disabled: " + name, find().isEnabled());
      return;
    }
    Assert.fail("custom action not supported yet: " + name + " -> " + action);
  }

  @Override
  public void checkCustom() {
    String action = checkValue.getCustom();
    if (action.startsWith("{isreadonly}")) {
      doIsReadOnly(action);
      return;
    }
    Assert.fail("custom action not supported yet: " + action);
  }

  private void doIsReadOnly(String action) {
    String value = action.replace("{isreadonly}", "");
    WebElement element = find();
    Assert.assertTrue("Element is not 'readonly', but it should be: " + name,
        "div".equalsIgnoreCase(element.getTagName()));
    Assert.assertEquals("Text does not match: " + name, value, element.getText());
  }

  @Override
  public WebElement find() {
    if (by instanceof ByLabel) {
      String text = ((ByLabel)by).value();
      return getDriver().findElement(
          By.xpath("//div[contains(@class, 'gw-InputWidget') and .//div[@class='gw-label' and text()='" + text
              + "']]//input" + "|//div[contains(@class, 'gw-InputWidget') and .//div[@class='gw-label' and text()='"
              + text + "']]//div[@class='gw-value-readonly-wrapper']"));
    }
    return super.find();
  }

  @Override
  public TafString get() {
    if (find().getTagName().equals("div")) {
      return TafString.normalString(find().getText());
    }
    return super.get();
  }
}
