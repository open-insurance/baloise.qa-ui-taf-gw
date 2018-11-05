/*
 ===========================================================================
 @    $Author$
 @  $Revision$
 @      $Date$
 @
 ===========================================================================
 */
package org.open.insurance.qa.ui.taf.gw.elements;

import com.baloise.testautomation.taf.base.types.TafDouble;
import com.baloise.testautomation.taf.browser.elements.BrDoubleInput;

/**
 * 
 */
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

}
