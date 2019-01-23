package org.open.insurance.baloise.qa.ui.taf.gw.elements;

import org.junit.Assert;

import com.baloise.testautomation.taf.browser.elements.BrButton;

public class GWButton extends BrButton {

  private void waitUntilEnabled() {
    Long time = System.currentTimeMillis();
    while (isDisabled()) {
      if (System.currentTimeMillis() > time + 60000L) {
        Assert.fail("Button ist not enabled since more than one minute");
      }
    }
  }

  public boolean isDisabled() {
    return find().getAttribute("class").contains("x-btn-disabled");
  }

  public boolean isFocused() {
    return find().getAttribute("class").contains("x-btn-focus");
  }

  @Override
  public void click() {
    waitUntilEnabled();
    super.click();
  }
}
