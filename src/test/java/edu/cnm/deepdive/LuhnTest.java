package edu.cnm.deepdive;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class LuhnTest {

  @ParameterizedTest
  @CsvFileSource(resources = "check-digit-illegal.csv", useHeadersInDisplayName = true)
  void isValid_illegal(String digits, Class<? extends Throwable> expectedExpection) {
    assertThrows(expectedExpection, () -> Luhn.isValid(digits));
  }

  @ParameterizedTest
  @CsvFileSource(resources = "check-digit-legal.csv", useHeadersInDisplayName = true)
  void isValid_legal(String digits, boolean expected) {
    assertEquals(expected, Luhn.isValid(digits));
  }

}