package org.open.insurance.baloise.qa.ui.taf.gw.elements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.baloise.testautomation.taf.base._interfaces.IAnnotations.By;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@By
public @interface ByLabel {
  public String value();
}
