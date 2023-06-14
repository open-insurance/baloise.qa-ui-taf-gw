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
package org.open.insurance.baloise.qa.ui.taf.gw.finder;

import static org.junit.Assert.assertNotNull;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baloise.testautomation.taf.browser.elements.BrFinder;

public class GWBrFinder extends BrFinder {

  private GWFrameworkVersion version = GWFrameworkVersion.gw9;

  public GWBrFinder(WebDriver driver, int timeoutInSeconds, GWFrameworkVersion version) {
    super(driver, timeoutInSeconds);
    this.version = version;
  }

  public GWBrFinder(WebDriver driver, int timeoutInSeconds) {
    super(driver, timeoutInSeconds);
  }

  public boolean isGW10() {
    return version.equals(GWFrameworkVersion.gw10);
  }

  public GWFrameworkVersion getVersion() {
    return version;
  }

  public Object executeJavascript(String script) {
    JavascriptExecutor js = (JavascriptExecutor)driver;
    Object result = "";
    try {
      result = js.executeScript(script);
    }
    catch (Throwable t) {}
    if (result == null) {
      result = "";
    }
    return result;
  }

  public boolean isAjaxDone() {
    if (version.equals(GWFrameworkVersion.gw9)) {
      String script = "return Ext.Ajax.isLoading()";
      String result = executeJavascript(script).toString();
      return result.contains("false");
    }
    if (version.equals(GWFrameworkVersion.gw10)) {
      try {
        driver.findElement(By.xpath("//div[@class='gw-click-overlay']"));
        return true;
      }
      catch (Throwable t) {
        return false;
      }
      finally {}
    }
    return true;
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

  public WebDriver getDriverWithoutWaitUntilLoadingComplete() {
    return driver;
  }

}
