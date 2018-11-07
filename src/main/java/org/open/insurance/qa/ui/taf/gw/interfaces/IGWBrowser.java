package org.open.insurance.qa.ui.taf.gw.interfaces;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openqa.selenium.WebDriver;

public interface IGWBrowser {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE, ElementType.METHOD})
  public @interface Browser {
    Class<? extends IGWBrowser> value();
  }

  public static String getPath(String filename) {
    File f = new File(IGWBrowser.class.getClassLoader().getResource(filename).getFile());
    return f.toString();
  }

  public WebDriver getDriver();

  public void killDriver();

}
