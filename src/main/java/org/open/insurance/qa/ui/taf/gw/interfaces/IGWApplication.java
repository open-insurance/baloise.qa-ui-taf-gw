package org.open.insurance.qa.ui.taf.gw.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IGWApplication {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE, ElementType.METHOD})
  public @interface Application {
    Class<? extends IGWApplication> value();
  }

  public String getUrl(IGWEnvironment environment, IGWStack stack, IGWLocale locale);

}
