/*
 ===========================================================================
 @    $Author$
 @  $Revision$
 @      $Date$
 @
 ===========================================================================
 */
package org.open.insurance.qa.ui.taf.gw.elements;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.baloise.testautomation.taf.browser.elements.BrRadiobutton;

/**
 * 
 */
public class GWRadioButton extends BrRadiobutton {

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
      Assert.fail("Component visible, but should NOT: " + name);
    }
    Assert.fail("not yet implemented: " + checkValue.getCustom());
  }

  @Override
  public boolean isSelected() {
    log("Check if radiobutton is selected: " + name);
    boolean found = false;
    WebElement parent = find();
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
