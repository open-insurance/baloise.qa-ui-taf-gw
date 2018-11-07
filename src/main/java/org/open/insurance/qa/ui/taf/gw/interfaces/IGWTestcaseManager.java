package org.open.insurance.qa.ui.taf.gw.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IGWTestcaseManager {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE})
  public @interface TestcaseManager {
    Class<? extends IGWTestcaseManager> value();
  }

  public IGWApplication getApplication();

  public IGWBrowser getBrowser();

  public IGWEnvironment getEnvironment();

  public IGWLocale getLocale();

  public IGWStack getStack();

  public void reset();
  
  public void setApplication(IGWApplication application);

  public void setBrowser(IGWBrowser browser);

  public void setEnvironment(IGWEnvironment environment);

  public void setLocale(IGWLocale locale);

  public void setStack(IGWStack stack);

  public void startApplication();

  public void stopApplication();

}
