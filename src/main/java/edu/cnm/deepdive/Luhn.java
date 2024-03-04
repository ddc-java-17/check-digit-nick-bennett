/*
 *  Copyright 2024 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive;

import java.util.regex.Pattern;

/**
 * This class defines a single static method, {@link #isValid(String)} method, which implements the
 * <a href="https://en.wikipedia.org/wiki/Luhn_algorithm">Luhn algorithm</a> to check the validity
 * of a {@link String} of digits. Implementation of this method is an assignment, extra credit
 * opportunity, or practical exam problem of the Deep Dive Coding Java training programs.
 */
public abstract class Luhn {

  private static final Pattern NON_ALPHA_NUMERIC = Pattern.compile("[\\W_]+");
  private static final Pattern NON_DIGIT = Pattern.compile("\\D");

  private Luhn() {
    // NOTE: There is NO need to do anything with this constructor! The method you will implement in
    // this class is static; thus, there is no need to create instances of this class.
  }

  /**
   * Performs a check-digit validation of the provided digit string, using the Luhn algorithm.
   * {@code digits} must contain only digit characters, whitespace, and punctuation characters, or
   * {@link IllegalArgumentException} is thrown.
   *
   * @param digits {@code String} of digit characters.
   * @return {@code true} if {@code digits} is valid; {@code false} otherwise.
   * @throws IllegalArgumentException If {@code digits} contains any alphabetic characters, or no
   *                                  digit characters.
   */
  public static boolean isValid(String digits) throws IllegalArgumentException {
    digits = NON_ALPHA_NUMERIC.matcher(digits).replaceAll("");
    if (NON_DIGIT.matcher(digits).find() || digits.isEmpty()) {
      throw new IllegalArgumentException();
    }
    char[] digitChars = digits.toCharArray();
    int sum = 0;
    int count = 0;
    for (int i = digitChars.length - 1; i >= 0; i--) {
      char digit = digitChars[i];
      int value = digit - '0'; // int value = Character.getNumericValue(digit);
      count++;
      if (count % 2 != 0) {
        sum += value;
      } else {
        int temp = 2 * value;
        if (temp > 9) {
          temp -= 9;
        }
        sum += temp;
      }
    }
    return sum % 10 == 0;
  }

}
