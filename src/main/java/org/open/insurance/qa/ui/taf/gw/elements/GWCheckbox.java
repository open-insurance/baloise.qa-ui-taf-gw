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

import com.baloise.testautomation.taf.base.types.TafBoolean;
import com.baloise.testautomation.taf.browser.elements.BrCheckbox;

/**
 * 
 */
public class GWCheckbox extends BrCheckbox {

  @Override
  public void fillCustom() {
    String custom = fillValue.getCustom();
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
    Assert.fail("command not implemented yet: " + name + " --> " + custom);
  }

  @Override
  public TafBoolean get() {
    return new TafBoolean(isSelected());
  }

  @Override
  public boolean isSelected() {
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

}
