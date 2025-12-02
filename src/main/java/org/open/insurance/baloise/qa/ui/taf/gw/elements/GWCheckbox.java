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

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.open.insurance.baloise.qa.ui.taf.gw.finder.GWBrFinder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.baloise.testautomation.taf.base.types.TafBoolean;
import com.baloise.testautomation.taf.browser.elements.BrCheckbox;

public class GWCheckbox extends BrCheckbox {

  @Override
  public void checkCustom() {
    doCustom(checkValue.getCustom());
  }

  @Override
  public void fillCustom() {
    doCustom(fillValue.getCustom());
  }

  @Override
  public WebElement find() {
    if (by instanceof ByLabel) {
      return brFindByLabel();
    }
    return super.find();
  }

  @Override
  public TafBoolean get() {
    return new TafBoolean(isSelected());
  }

  @Override
  public boolean isSelected() {
    GWBrFinder finder = (GWBrFinder)component.getBrowserFinder();
    if (finder.isGW10_2_3() || finder.isGW10_2_4()) {
      String attribute = find().getAttribute("class");
      Assert.assertFalse(
          "Seems to be a hidden input -> isSelected does NOT work! Please adapt way of searching this checkbox: "
              + name,
          attribute.contains("hidden"));
      return attribute.contains("gw-checked");
    }
    if (finder.isGW10()) {
      return super.isSelected();
    }
    boolean found = false;
    WebElement parent = find();
    while (!found) {
      parent = parent.findElement(By.xpath(".."));
      assertNotNull("checkbox does not have a parent --> selection state cannot be determined", parent);
      String klass = parent.getAttribute("class");
      if (klass != null) {
        if (klass.contains("x-form-type-checkbox")) {
          found = true;
        }
      }
    }
    return parent.getAttribute("class").contains("x-form-cb-checked");
  }

  private WebElement brFindByLabel() {
    String label = ((ByLabel)by).value();
    String xpath = "//div[contains(@class, 'gw-InputWidget') and .//div[@class='gw-label' and text()='" + label + "']]";
    GWBrFinder finder = (GWBrFinder)component.getBrowserFinder();
    return finder.getDriver().findElement(By.xpath(xpath + "//div[@role='checkbox']"));
  }

  private void doCustom(String custom) {
    System.out.println("Starting custom action: " + name + " --> " + custom);
    if ("{notvisible}".equalsIgnoreCase(custom)) {
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
    if ("{isselected}".equalsIgnoreCase(custom)) {
      Assert.assertTrue("is NOT selected, but should be: " + name, isSelected());
      return;
    }
    if ("{isnotselected}".equalsIgnoreCase(custom)) {
      Assert.assertFalse("is selected, but should NOT be: " + name, isSelected());
      return;
    }
    if ("{alwaysincluded}".equalsIgnoreCase(custom)) {
      String clazz = find().findElement(By.xpath("./../..")).getAttribute("class");
      Assert.assertNotNull(clazz);
      Assert.assertTrue("Checkbox seems to be visible, but should be hidden: " + name, clazz.contains("gw-hidden"));
      return;
    }
    if (custom.startsWith("{isreadonly}")) {
      custom = custom.replace("{isreadonly}", "");
      Assert.assertEquals("Label der Checkbox stimmt nicht: " + name, custom, find().getText());
      return;
    }
    Assert.fail("command not implemented yet: " + name + " --> " + custom);
  }

}
