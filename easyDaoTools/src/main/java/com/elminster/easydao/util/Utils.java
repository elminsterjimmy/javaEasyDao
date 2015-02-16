package com.elminster.easydao.util;

abstract public class Utils {
  
  public static String normalizeName(String name) {
    StringBuilder sb = new StringBuilder();
    int length = name.length();
    boolean upper = false;
    boolean first = true;
    for (int i = 0; i < length; i++) {
      char ch = name.charAt(i);
      if (Character.isLetter(ch)) {
        if (first) {
          first = false;
          ch = Character.toUpperCase(ch);
        }
        if (upper) {
          upper = false;
          ch = Character.toUpperCase(ch);
        }
        sb.append(ch);
      } else if (Character.isDigit(ch)) {
        if (first) {
          first = false;
          continue;
        } else {
          sb.append(ch);
        }
      } else {
        upper = true;
        continue;
      }
    }
    return sb.toString();
  }
}
