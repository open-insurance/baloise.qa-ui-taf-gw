package org.open.insurance.baloise.qa.ui.taf.gw.elements;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.baloise.testautomation.taf.browser.elements.BrStringInput;

public class GWStringInput extends BrStringInput {
  
  @Override
  public void fillCustom() {
    String action = fillValue.getCustom();
    if (action.startsWith("{isreadonly}")) {
      String value = action.replace("{isreadonly}", "");
      WebElement element = find();
      Assert.assertTrue("Element is not 'readonly', but it should be", "div".equalsIgnoreCase(element.getTagName()));
      Assert.assertEquals("Text does not match", value, element.getText());
      return;
    }
    Assert.fail("custom action not supported yet: " + action);
  }

}
