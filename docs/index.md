## Overview

{% include ddc-abbreviations.md %}

{% include assignment-info.md %}

Many data communication tasks involve the use of a _check sum_, as a simple guard against inadvertent corruption of the data.[^inadvertent-corruption] Some of these techniques predate electronic computers by centuries. For this problem, you will implement a classic algorithm (not centuries, but several decades old) for detecting certain kinds of data corruption---in particular, those that frequently occur in the manual entry of credit card numbers.

[^inadvertent-corruption]: Our focus here is not on malicious or otherwise deliberate forms of data corruption, but primarily on types of corruption introduced by simple, unintentional mistakes in data transmission or transcription.

One simple type of check sum is used to enable a basic level of validation of a string containing only digit characters: 

1. An arithmetic operation is applied to all the digits in the original string. This operation incorporates the $mod$ operator to produce a result expressable in a single digit; this is a _check digit_.

2. The check digit is appended to the string to produce the full digit string that will be used from that point on.

3. At some later moment, after the digit string has been provided for use by another party (e.g., as a credit card number used to make a purchase), the formula used in step 1 is applied again to all of the digits _besides_ the check digit, and if the result matches the check digit, then the digit string is considered sufficiently valid to proceed to the next step of its intended use; otherwise, it is a mathematical certainty that one or more digits have been added, removed, interchanged, or modified in the transcription, and the digit string is rejected as invalid. 

    Alternatively, a formula similar to the one used in step 1 may be applied to all of the digits, including the check digit, with the result compared to a fixed, expected value (usually 0). Once again, if the values match, the digit string is accepted as valid;[^not-invalid] otherwise, it is rejected as invalid.

[^not-invalid]: More correctly, the digit string does not appear to have been corrupted by any of several types of errors that are especially common in manual entry. It may still, in fact, be invalid for its intended use; for example, it could be a numerically valid credit card number that isn't actually assigned to any account---thus, any charge to that card number would be declined. 

One of the most widely used check-digit generation and validation algorithms is the [_Luhn algorithm_](https://en.wikipedia.org/wiki/Luhn_algorithm), invented in the 1950s by Hans Peter Luhn, a scientist at IBM. This algorithm is used for generating the check digits on most types of credit cards, as well as SIM card serial numbers. Your task is to implement this algorithm to validate a string of digit characters.

The Luhn algorithm can be expressed in a few different ways; for this problem, we suggest implementing the pseudocode given in the following section.

## Algorithm

### Objective

Evaluate a string of digit characters, which incorporates a previously computed check digit, to identify (and mark as invalid) any digit string in which:

- a single digit has been added;
- a single digit has been removed;
- a single digit has been replaced by a different digit;
- or _almost_ any single possible pair of adjacent digits has been transposed.

### Input 

1. $digits\text{: string}$ is a string of digit characters.

### Given

1. $\newcommand{\val}{\operatorname{val}}\val(c\text{: character})\text{: integer}$ returns the numeric value of a digit character $c$.

### Steps

1. $sum\text{: integer} \gets 0$.

2. $count\text{: integer} \gets 0$.

3. For each $digit\text{: character}$ of $digits$, starting from the last digit character, and proceeding in reverse order to the first digit character:

    1. $value\text{: integer} \gets \newcommand{\val}{\operatorname{val}}\val(digit)$

    2. $count \gets count + 1$.

    3. If $count$ is odd:

        - $sum \gets sum + value$;

       otherwise:

        1. $temp\text{: integer} \gets 2 \cdot value$.

        2. if $temp > 9$:

            - $temp \gets temp - 9$.

        3. $sum \gets sum + temp$.

4. If $sum$ is evenly divisible by 10:

    - $digits$ is valid;[^not-invalid]

   otherwise:

    - $digits$ is invalid.

## Basic problem

### Implementation

#### Declaration

In the `edu.cnm.deepdive.Luhn` class, the `isValid` method is declared as follows:

```java
public static boolean isValid(String digits) throws IllegalArgumentException
```

The implementation **must not** change the modifiers, return type, method name, parameter types/number/order, or possible exceptions shown above. For more method declaration details, see the [Javadoc documentation](api/edu/cnm/deepdive/Luhn.html#isValid(java.lang.String)).

#### Specifications

For the basic task, complete this method by implementing the [Luhn algorithm](#algorithm), as articulated above, with these specific requirements:

1. The implementation **must** treat all the characters in the base-10 range (`'0'`&#x2025;`'9'`)---and _only those characters_---as digit characters.

2. If any of characters of `digits` is an alphabetic character (i.e., an alphanumeric character that _is not_ a digit), `IllegalArgumentException` **must** be thrown.

3. If there are _no_ digit characters in `digits`, `IllegalArgumentException` **must** be thrown.

4. On the other hand, the presence of any non-alphanumeric characters---including whitespace---**must not** result in an exception. Instead, before computing and validating the sum (as specified [above](#algorithm)), _all non-alphanumeric characters_ (including "-", the most commonly used credit card number group delimiter) **must** be removed.

5. If no exception is thrown, then _if the digit string is valid, according to the [Luhn algorithm](#algorithm),_ the method **must** return `true`; otherwise, it **must** return `false`.

#### Tips

1. The `String.replaceAll` method can be used to remove characters from a `String`, using a regular expression.[^regular-expressions] Remember, however, that it _does not modify a string in-place;_ instead, it returns a new string, with the modified contents. Also, remember that the `\W` (non-word) regex character class matches _almost all---but not all_---non-alphanumeric characters: in regular expressions, the underscore (`_`) is considered a word character.

2. Remember that all `char` values are numeric. For example, `'0'` is located at the Unicode code point 48, `'1'` at 49, etc.; these numeric values are actually what a `char` value contains. Consider how you might take advantage of this to get the numeric value of a digit character without (for example) using a `switch` expression. Alternatively, you might investigate the methods of the `Character` class, to see if there is one that returns the numeric value of a digit character. 

3. You may find it useful to create one or more additional `private static` methods as "helpers"; the problem can be solved without doing so, however.

4. Don't hesitate to declare any `private static` fields (esp. `private static final` constants) that you feel might simplify or clarify your code.

5. The method to be completed includes a `TODO` comment to that effect.

[^regular-expressions]: A _regular expression_ (usually abbreviated to _regex_ or _regexp_) is a text pattern---expressed as a combination of literal values, quantitifers, character _classes_ (shorthand symbols for groups of characters), and other elements---that can be used to search to filter text, validate input, extract key portions, etc. [Regular-Expressions.info](https://www.regular-expressions.info/) provides a very good overview of regular expression syntax; the [API documentation for the `java.util.regex.Pattern` class](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html) summarizes the regex syntax supported by the Java standard library.

### Unit tests

For unit testing credit, use JUnit5 to verify your code with the inputs and expected outputs below.

#### Test cases

| `digits` | Expected value of `Luhn.isValid(digits)` | Expected exception |
|:--------:|:-----------------------------------------------:|:---------:|
| `"4242424242424242"` | `true` | _(none)_ |
| `"4242-4242-4242-4242"` | `true` | _(none)_ |
| `"4242_4242_4242_4242"` | `true` | _(none)_ |
| `"4000056655665556"` | `true` | _(none)_ |
| `" 4000056655665556 "` | `true` | _(none)_ |
| `"3782 822463 10005"` | `true` | _(none)_ |
| `"4242-4242-4242-4224"` | `false` | _(none)_ |
| `"4000056655665565"` | `false` | _(none)_ |
| `"4000056656565556"` | `false` | _(none)_ |
| `"4000056655656556"` | `false` | _(none)_ |
| `""` | _(none)_ | `IllegalArgumentException` |
| `"4242x4242x4242x4242"` | _(none)_ | `IllegalArgumentException` |
| `"40000565656655S6"` | _(none)_ | `IllegalArgumentException` |
| `"3782-822463-10005L"` | _(none)_ | `IllegalArgumentException` |

In evaluating your implementation, we reserve the right to include additional test cases; code that conforms to the specifications stated above should pass all such additional tests.
 
#### Tips 

1. The `Luhn.isValid` method is `static`, and the `Luhn` class is `abstract`. Do not attempt to create instances of `Luhn`.

2. Each test case has a single scalar input for the method under test, and a scalar expected value, so a CSV file referenced by a `@CsvFileSource` would be a suitable source of arguments for parameterized tests. If you choose that option (or a `@CsvSource`), there are a couple important points to remember:

    1. Any `String` value that contains leading or trailing spaces, or a comma, or no characters at all (i.e., an empty `String`), must be enclosed in double quotes (in `@CsvFileSource`) or single quotes (in `@CsvSource`).
    
    2. If a class name (such as the name of an exception class) is specified in the content of a `@CsvFileSource` or `@CsvSource`, corresponding to a `Class` parameter of a parameterized test method, the class name must be fully qualified.
