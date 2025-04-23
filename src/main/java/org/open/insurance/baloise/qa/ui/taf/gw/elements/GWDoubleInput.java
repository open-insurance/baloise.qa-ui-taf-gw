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
package org.open.insurance.baloise.qa.ui.taf.gw.elements;

import com.baloise.testautomation.taf.base.types.TafDouble;
import com.baloise.testautomation.taf.browser.elements.BrDoubleInput;

public class GWDoubleInput extends BrDoubleInput {

  public int nachkommastellen = 2;

  @Override
  public String fillValueAsString() {
    if (fillValue != null) {
      String s = "";
      for (int i = 0; i < nachkommastellen; i++) {
        s = s + "0";
      }
      // Cast to TafDouble is ok because BrDoubleInput IS ALWAYS a TafDouble based input
      return ((TafDouble)fillValue).asString("0." + s);
    }
    return null;
  }

  @Override
  public TafDouble get() {
    String text = getFinder().safeInvoke(() -> find().getAttribute("value"));
    // might be a readonly field... if so -> get the plain text
    if (text == null) {
      text = getFinder().safeInvoke(() -> find().getText());
    }
    return new TafDouble(text);
  }

}
