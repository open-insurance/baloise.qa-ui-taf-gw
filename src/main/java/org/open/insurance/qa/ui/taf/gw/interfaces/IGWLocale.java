package org.open.insurance.qa.ui.taf.gw.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IGWLocale {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE, ElementType.METHOD})
  public @interface Locale {
    Class<? extends IGWLocale> value();
  }

  public String getUrlPart();

}
