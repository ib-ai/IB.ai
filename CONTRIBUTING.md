If you are reading this page, then thank you for your interest in contributing towards the bot.
We are grateful for any help, however, please ensure you follow the guidelines laid out below
and ensure that any code you produce for IB.ai is licensed with the GNU GPL v3.

# Contributions

Not all contribution PRs (read below) will be accepted. 
For ideas as to what to contribute, please refer to the GitHub issues or contact a member of the development team.

Bug fixes and optimisations are also greatly appreciated!

# VCS

1) Create a new branch (and fork if applicable). Label it appropriately.
2) Make the changes on that branch. Make sure to test these changes thoroughly.
3) Commit to and push the changes, once you are sure the code is functional.
4) Create a PR from your branch to master.
5) A maintainer will then review your PR.

If you have questions, please ask a maintainer.

# Code

## Formatting

* Use Java-style braces (moustache-bois).
This means that the opening brace is on the same line, but the closing brace is on a new line.
There must be a space before an opening brace.
* For methods that return `this`, chain them on a new line, as shown below:
![Formatting](https://i.imgur.com/7kZPU4O.png)
* For long methods, feel free to split the statement over multiple statements, with one parameter per line.
* First, declare all fields.
Then declare the constructor(s) (public, package-private, protected, private), followed by private methods, package-private methods, protected methods, private methods).
* Leave an empty line between the class opening brace and the first statement, and between the class closing brace and the last closing brace.
* Leave an empty line between the last field and the first method.
* Leave an empty line between methods.
* Feel free to use empty lines within methods to group statements.

## Naming conventions

* Packages are to be declared in all `lowercase`. Avoid using multi-word package names, if this is not possible, use `snake_case`.
* Classes are to be declared in `TitleCase`.
* Static final variables (constants) are to be declared in `ALL_CAPITALS`, using underscores as delimiters.
* Variables are to be declared in `camelCase`. They should be be somewhat descriptive. Abbreviations are strongly discouraged.
Exceptions exist for loop variables (i.e. `for(int i = 0; i < 10; i++) {}`, in this case `i` is perfectly acceptable).

## Access and modification

* Use the lowest possible access modifier.
* Use the `final` modifier on all classes at the end of the inheritance chain.
* All class fields defined by constructor parameters should be labelled as `private final`, unless not possible due to reflection.
* Do not label in-method scoped variables as `final` unless (optional) they are required to be effectively final to be used inside a lambda.

## JavaDocs

 * Code commenting is required and necessary.
 * Documentation should be in JavaDoc format...
   * HTML tags should be used for formatting within the JavaDocs.
   * 'br' is the tag for a new line.
   * 'p' is the tag for a paragraph.
   * A description of the method should come before a list of JavaDoc tags.
   * Tags should be listed in the same order as the below example.
 * Example:

```java
/**
 * LICENSE HEADER
 * LICENSE HEADER
 */

package com.discord.etc;

import com.etc;

/** @author name1, name2, etc.
 * @since YYYY.MM.DD (date of class creation)
 */

public class Example {

  /** <p>Example line
   * on the same line before a line break. <br>
   * Final line of the paragraph.</p>
   * <p>This is a second paragraph.</p>
   * @throws ExceptionName Description of exception.
   * @return {@link OtherClass} description of returned value.
   * @param nameOfParam description of param. It is a {@link String}.
   * @see Classes#andReferencedMethods()
   * @see ClassName
   */
  @Override
  public static OtherClass methodOne(String nameOfParam) {
      // Do something
      return new OtherClass(...);
  }
}
```

## Singletons

* Use an enumeration, with the only field being the value `INSTANCE`.
  * An example of this is seen in the main `IBai` class.
* Alternatively, enumerations can be created in the following fashion:

```java
public final class Singleton {

  private static final Object MUTEX = new Object();
  private static Singleton instance;

  private Singleton() {}

  public static Singleton getInstance() {
    if(instance == null) {
      synchronized(MUTEX) {
        if(instance == null) {
          instance = new Singleton();
        }
      }
    }
    return instance;
  }

}
```
