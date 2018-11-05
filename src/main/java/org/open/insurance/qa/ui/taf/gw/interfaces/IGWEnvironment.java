package org.open.insurance.qa.ui.taf.gw.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IGWEnvironment {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE, ElementType.METHOD})
  public @interface Environment {
    Class<? extends IGWEnvironment> value();
  }

  public String getUrlPart();

  public void setTafGlobalMandant();

}
