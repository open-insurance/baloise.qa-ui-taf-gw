package org.open.insurance.baloise.qa.ui.taf.gw.elements;

import org.junit.Assert;
import org.open.insurance.baloise.qa.ui.taf.gw.finder.GWBrFinder;
import org.openqa.selenium.WebElement;

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
      String value = action.replace("{isreadonly}", "");
      WebElement element = find();
      Assert.assertTrue("Element is not 'readonly', but it should be", "div".equalsIgnoreCase(element.getTagName()));
      GWBrFinder finder = (GWBrFinder)component.getBrowserFinder();
      if (finder.isGW10()) {
        // Numbers are having different separators
        Assert.assertEquals("Text does not match", value, element.getText().replace("’", "'"));
      }
      else {
        Assert.assertEquals("Text does not match", value, element.getText());
      }
      return;
    }
    Assert.fail("custom action not supported yet: " + action);
  }

}
