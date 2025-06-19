/*
 * Copyright 2018 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.open.insurance.baloise.qa.ui.taf.gw.elements;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.open.insurance.baloise.qa.ui.taf.gw.finder.GWBrFinder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import com.baloise.testautomation.taf.base._interfaces.IAnnotations.ByCustom;
import com.baloise.testautomation.taf.base.types.TafString;
import com.baloise.testautomation.taf.browser.elements.BrCombobox;
import com.baloise.testautomation.taf.browser.elements.BrStringInput;

public class GWCombobox extends BrStringInput {

  @Override
  public void check() {
    if (!canCheck()) {
      return;
    }
    if (checkValue.isCustom()) {
      checkCustom();
      return;
    }
    GWBrFinder finder = (GWBrFinder)component.getBrowserFinder();
    if (finder.isGW10()) {
      System.out.println("Combo: " + name + " -> " + checkValueAsString());
      if (by instanceof ByCustom) {
        Select cb = new Select((WebElement)brFindByCustom());
        Assert.assertEquals("Contents does not fit", checkValueAsString(), cb.getFirstSelectedOption().getText());
        return;
      }
      BrCombobox combobox = new BrCombobox();
      combobox.setName(getName());
      combobox.setComponent(component);
      combobox.setBy(by);
      combobox.setCheck(checkValue.asTafString());
      try {
        combobox.check();
      }
      catch (UnexpectedTagNameException e) {
        Assert.fail("Probably, the combobox is read-only but should not be: " + name);
      }
      return;
    }
    super.check();
  }

  @Override
  public void checkCustom() {
    String custom = checkValue.getCustom();
    if (custom.startsWith("{isreadonly}")) {
      assertIsReadOnly(custom);
      return;
    }
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
    if (custom.startsWith("{isreadonly}")) {
      assertIsReadOnly(custom);
      return;
    }
    Assert.fail("Command not implemented yet: " + name + " --> " + checkValue.getCustom());
  }

  @Override
  public void fill() {
    if (!canFill()) {
      return;
    }
    if (fillValue.isCustom()) {
      fillCustom();
      return;
    }
    GWBrFinder finder = (GWBrFinder)component.getBrowserFinder();
    if (finder.isGW10()) {
      System.out.println("Combo: " + name + " -> " + fillValueAsString());
      // TODO GW10 -> braucht es den Code überhaupt? JA! Weil brFindByCustom HIER implementiert ist und nicht auf
      // BrCombobox, siehe weiter unten
      // TODO GW10 -> diesen Code noch korrigieren, da NICHT für {custom}... ausgelegt -> von...
      if (by instanceof ByCustom) {
        Select cb = new Select((WebElement)brFindByCustom());
        cb.selectByVisibleText(fillValueAsString());
        return;
      }

      // TODO GW10 - ...bis hierher
      // Ev. wenn GW9 nicht mehr gebraucht wird, dann GWCombobox ableiten von BrCombobox -> obiger Code wird hinfällig
      // sein, da dann brFindByCustom
      // am richtigen Ort implementiert ist und aufgerufen wird.

      if ("{checkthatnotexists}".equalsIgnoreCase(fillValue.asString())) {
        fillValue = TafString.normalString("{custom}{notvisible}");
        fillCustom();
        return;
      }
      if (!isReadOnly() && (fillValue.asString().equals(get().asString()))) {
        return;
      }
      for (int i = 0; i < 5; i++) {
        try {
          click();
          if (find().getAttribute("class").contains("gw-focus")) {
            break;
          }
          else {
            try {
              Thread.sleep(100L);
            }
            catch (Exception e) {}
          }
        }
        catch (Throwable t) {}
      }
      gwFill();
      return;
    }
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

  @Override
  public void fillCustom() {
    String custom = fillValue.getCustom();
    String[] actions = custom.split("->");
    for (String action : actions) {
      fillCustom(action);
    }
  }

  @Override
  public WebElement find() {
    if (by instanceof ByLabel) {
      return brFindByLabel();
    }
    return super.find();
  }

  @Override
  public TafString get() {
    WebElement cb = find();
    if (cb.getTagName().equals("select")) {
      Select s = new Select(cb);
      return TafString.normalString(s.getFirstSelectedOption().getText());
    }
    return TafString.normalString(cb.getText());
  }

  public List<String> getOptions() {
    List<String> result = new ArrayList<String>();
    List<WebElement> options = find().findElements(By.xpath(".//option"));
    for (WebElement option : options) {
      String fullText = option.getText();
      System.out.println(fullText);
      result.add(fullText);
    }
    return result;
  }

  private void assertIsReadOnly(String action) {
    String value = action.replace("{isreadonly}", "");
    WebElement element = find();
    Assert.assertTrue("Element is not 'readonly', but it should be: " + name,
        "div".equalsIgnoreCase(element.getTagName()));
    Assert.assertEquals("Text does not match", value, element.getText());
  }

  private WebElement brFindByLabel() {
    String label = ((ByLabel)by).value();
    String xpath = "//div[contains(@class, 'gw-InputWidget') and .//div[@class='gw-label' and text()='" + label + "']]";
    GWBrFinder finder = (GWBrFinder)component.getBrowserFinder();
    return finder.getDriver().findElement(
        By.xpath(xpath + "//select" + "|" + xpath + "//div[@data-gw-getset='select']//div[@class='gw-label']"));
  }

  private void fillCustom(String action) {
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
      assertIsReadOnly(action);
      return;
    }
    if (action.startsWith("{checkvalue}")) {
      action = action.replace("{checkvalue}", "");
      GWBrFinder finder = (GWBrFinder)component.getBrowserFinder();
      if (finder.isGW10()) {
        String text = finder.safeInvoke(() -> {
          return new Select(find()).getFirstSelectedOption().getText();
        });
        Assert.assertEquals("Value of input line does not match: " + name, action, text);
        return;
      }
      Assert.assertEquals("Value of input line does not match: " + name, action, find().getAttribute("value"));
      return;
    }
    if (action.startsWith("{checkexactly}")) {
      action = action.replace("{checkexactly}", "");
      String[] exactElements = action.split(";");
      List<WebElement> listElements;
      GWBrFinder finder = (GWBrFinder)component.getBrowserFinder();
      if (finder.isGW10()) {
        listElements = finder.safeInvoke(() -> {
          return new Select(find()).getOptions();
        });
      }
      else {
        find().click();
        WebElement list = getDriver()
            .findElement(By.xpath("//div[contains(@class, 'x-boundlist') and not(contains(@style, 'display: none'))]"));
        listElements = list.findElements(By.xpath(".//li"));
      }
      Assert.assertEquals("Number of elements in list is not equal to number of expected elements: " + name,
          exactElements.length, listElements.size());

      for (int i = 0; i < exactElements.length; i++) {
        WebElement e = listElements.get(i);
        Assert.assertEquals("Element does not fit: " + name, exactElements[i], e.getText());
      }
      find().click();
      return;
    }
    if (action.startsWith("{fill}")) {
      action = action.replace("{fill}", "");
      setFill(action);
      fill();
      return;
    }
    if (action.startsWith("{selectbypartialtext}")) {
      action = action.replace("{selectbypartialtext}", "");
      setFill(action);
      fillWithPartialText();
      return;
    }
    if (action.equals("{isdisabled}")) {
      Assert.assertFalse("Combobox is enabled, but should be disabled: " + name, find().isEnabled());
      return;
    }
    Assert.fail("custom action not supported yet: " + name + " -> " + action);
  }

  private void fillWithPartialText() {
    GWBrFinder finder = (GWBrFinder)component.getBrowserFinder();
    if (finder.isGW10()) {
      List<WebElement> options = find().findElements(By.xpath(".//option"));
      for (WebElement option : options) {
        String fullText = option.getAttribute("value");
        if (fullText != null && fullText.contains(fillValueAsString())) {
          fillWith(fullText);
          return;
        }
      }
      Assert.fail("Partial text not found in list: " + fillValueAsString());
    }
    else {
      Assert.fail("Not yet implemented for GW9");
    }
  }

  private void gwFill() {
    if (fillValue != null) {
      if (fillValue.isCustom()) {
        fillCustom();
        return;
      }
      if (!fillValue.isSkip() && fillValue.isNotNull()) {
        getFinder().safeInvoke(() -> {
          WebElement we = find();
          Select s = new Select(we);
          s.selectByVisibleText(fillValue.asString());
        });
      }
    }
  }

  private boolean isReadOnly() {
    WebElement element = find();
    return "div".equalsIgnoreCase(element.getTagName());
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
