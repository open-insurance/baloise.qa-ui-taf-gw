package org.open.insurance.baloise.qa.ui.taf.gw.elements;

import org.junit.Assert;
import org.open.insurance.baloise.qa.ui.taf.gw.finder.GWBrFinder;

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
    if (((GWBrFinder)component.getBrowserFinder()).isGW10()) {
      return ((GWBrFinder)component.getBrowserFinder()).safeInvoke(() -> {
        System.out.println(find().getAttribute("id"));
        return "true".equals(find().getAttribute("aria-disabled"));
      });
    }
    return ((GWBrFinder)component.getBrowserFinder()).safeInvoke(() -> {
      return find().getAttribute("class").contains("x-btn-disabled");
    });
  }

  public boolean isFocused() {
    return ((GWBrFinder)component.getBrowserFinder()).safeInvoke(() -> {
      return find().getAttribute("class").contains("x-btn-focus")
          // TODO: GW10 after '||'
          || find().getAttribute("class").contains("gw-focus");
    });
  }

  @Override
  public void click() {
    waitUntilEnabled();
    super.click();
  }
}
