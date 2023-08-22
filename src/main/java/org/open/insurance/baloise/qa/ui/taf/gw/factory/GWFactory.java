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
package org.open.insurance.baloise.qa.ui.taf.gw.factory;

import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;

import com.baloise.testautomation.taf.base._interfaces.IComponent;

public class GWFactory {

  public static <T extends IComponent> T newInstance(Class<T> clazz, IComponent parent) {
    T instance;
    try {
      instance = (T)clazz.getDeclaredConstructor().newInstance();
      instance.setComponent(parent);
      return instance;
    }
    catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e) {}
    Assert.fail("No instance could be created for: " + clazz);
    return null;
  }

}
