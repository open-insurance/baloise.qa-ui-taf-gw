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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.baloise.testautomation.taf.browser.elements.BrRadiobutton;

public class GWRadioButton extends BrRadiobutton {

  @Override
  public void checkCustom() {
    doCustom(checkValue.getCustom());
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
      Assert.assertTrue("Radio button NOT selected, but should be", isSelected());
      return;
    }
    if ("{isnotselected}".equalsIgnoreCase(custom)) {
      Assert.assertFalse("Radio button selected, but should NOT be", isSelected());
      return;
    }
    Assert.fail("command not implemented yet: " + name + " --> " + custom);
  }

  @Override
  public void fillCustom() {
    doCustom(fillValue.getCustom());
  }

  @Override
  public boolean isSelected() {
    log("Check if radiobutton is selected: " + name);
    boolean found = false;
    WebElement parent = find();
    if ("input".equalsIgnoreCase(parent.getTagName())) {
      if (!parent.getAttribute("class").contains("x-form-radio")) {
        return parent.isSelected();
      }
    }
    ;
    while (!found) {
      parent = parent.findElement(By.xpath(".."));
      assertNotNull("Radiobutton does not have parent --> selection state cannot be determined", parent);
      String klass = parent.getAttribute("class");
      if (klass != null) {
        if (klass.contains("x-form-type-radio")) {
          found = true;
        }
      }
    }
    return parent.getAttribute("class").contains("x-form-cb-checked");
  }

  private void log(String s) {
    System.out.println(s);
  }

  @Override
  public void select() {
    for (int i = 0; i < 5; i++) {
      find().click();
      if (isSelected()) {
        break;
      }
      log("NOT selected after click: " + name + " --> try again");
      try {
        Thread.sleep(1000);
      }
      catch (Exception e) {}
    }
  }

}
