package org.open.insurance.qa.ui.taf.gw.factory;

import org.junit.Assert;

import com.baloise.testautomation.taf.base._interfaces.IComponent;

public class GWFactory {

  public static <T extends IComponent> T newInstance(Class<T> clazz, IComponent parent) {
    T instance;
    try {
      instance = (T)clazz.newInstance();
      instance.setComponent(parent);
      return instance;
    }
    catch (InstantiationException | IllegalAccessException e) {}
    Assert.fail("No instance could be created for: " + clazz);
    return null;
  }

}
