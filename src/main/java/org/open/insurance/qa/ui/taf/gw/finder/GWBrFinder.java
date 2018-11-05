/*
 ===========================================================================
 @    $Author$
 @  $Revision$
 @      $Date$
 @
 ===========================================================================
 */
package org.open.insurance.qa.ui.taf.gw.finder;

import static org.junit.Assert.assertNotNull;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baloise.testautomation.taf.browser.elements.BrFinder;

/**
 * 
 */
public class GWBrFinder extends BrFinder {

  public GWBrFinder(WebDriver driver, int timeoutInSeconds) {
    super(driver, timeoutInSeconds);
  }

  public Object executeJavascript(String script) {
    JavascriptExecutor js = (JavascriptExecutor)driver;
    Object result = "";
    try {
      result = js.executeScript(script);
    }
    catch (Exception e) {}
    return result;
  }

  public boolean isAjaxDone() {
    String script = "return Ext.Ajax.isLoading()";
    String result = executeJavascript(script).toString();
    return result.contains("false");
  }

  public void waitForAjaxDone(int seconds) {
    try {
      Thread.sleep(50);
    }
    catch (InterruptedException e) {}
    long time = System.currentTimeMillis();
    boolean done = false;
    while (!done) {
      done = isAjaxDone();
      if (System.currentTimeMillis() > time + 1000 * seconds) {
        Logger logger = LoggerFactory.getLogger("GWBrFinder");
        logger.warn("Ajax NOT completed after " + seconds + " seconds --> further actions may not be correct anymore");
        done = true;
      }
    }
  }

  @Override
  public void waitUntilLoadingComplete() {
    assertNotNull("Driver is NOT assigend --> no waitingUntilLoadingComplete possible", driver);
    waitForAjaxDone(120);
  }

}
