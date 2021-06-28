# Contributions

Thank you for your interest in contributing to IB.ai. 
However, we ask for you to accept our GPLv3 licencing as well as to follow the contribution guidelines laid out below.
More specifically, please ensure you comply with the formatting and testing guidelines too.

### What to Contribute

Please note that not all features or changes will be accepted without prior discussion.
Obviously this does not extend to bugfixes, but generally, please brainstorm your ideas with the maintainers beforehand, otherwise it is a waste of everyone's time if the changes to do make it.
The best ways to engage with the development is through GitHub Issues or on the [Discord](https://discord.gg/ibo).

### Setting up the Project

IB.ai purely relies on Docker.
Please ensure that you have [Docker installed](https://docs.docker.com/get-docker/) before you move onto the next steps.

By default, IB.ai only allows maintainers to modify any branch on the repository.
Therefore, it is vital to fork the repository and keep an upstream remote of the `master` branch.
1. Create a fork (the easiest way to do this is clicking the "Fork" button on GitHub).
2. Clone the repository (e.g. `git clone https://github.com/YourUsername/IB.ai.git`).
3. Go into the cloned repository and open up a shell (e.g. `cd IB.ai`).
4. Add the `master` branch as an upstream: `git remote add upstream https://github.com/ib-ai/IB.ai.git`.
5. Create a new branch for each of your changes from `master`: `git checkout -b my-branch-name upstream/master` (where `my-branch name` is your branch name).

### Pushing Changes

Once you made your changes then you can push these and create a pull request.
1. Commit and push your changes.
2. Go to your fork, and press the "Pull request" button.
3. Set the "base fork" to `ib-ai/IB.ai`, "base" to `master`, "head fork" to your fork, and "compare" to your branch.
4. Create the pull request.
5. The maintainers will review it and request any specific changes.
6. Once it is ready, then the version will be updated, and it will be merged into the `master` branch.
7. The CI and CD will automatically deploy your changes.

# Code Formatting

Any code submitted needs to conform to our coding styles.
We use [1TBS](https://en.wikipedia.org/wiki/Indentation_style#Variant:_1TBS_(OTBS)) as our indentation style.

### Basic Formatting

* Use Java-style braces (moustache-bois).
This means that the opening brace is on the same line, but the closing brace is on a new line.
There must be a space before an opening brace.
* Do not use a space between a keyword and the opening parentheses `if(that)` rather than `if (that)`.
* For methods that return `this`, chain them on a new line, as shown below:
![Formatting](https://i.imgur.com/7mMjSNI.png)
* For long methods, feel free to split the statement over multiple statements, with one parameter per line.
* First, declare all fields.
Then declare the constructor(s) (public, package-private, protected, private), followed by private methods, package-private methods, protected methods, private methods).
* Leave an empty line between the class opening brace and the first statement, and between the class closing brace and the last closing brace.
* Leave an empty line between the last field and the first method.
* Leave an empty line between methods.
* Feel free to use empty lines within methods to group statements.

### Naming conventions

* Packages are to be declared in all `lowercase`. Avoid using multi-word package names, if this is not possible, use `snake_case`.
* Classes are to be declared in `TitleCase`.
* Static final variables (constants) are to be declared in `ALL_CAPITALS`, using underscores as delimiters.
* Variables are to be declared in `camelCase`. They should be be somewhat descriptive. Abbreviations are strongly discouraged.
Exceptions exist for loop variables (i.e. `for(int i = 0; i < 10; i++) {}`, in this case `i` is perfectly acceptable).

### Access and modification

* Use the lowest possible access modifier.
* Use the `final` modifier on all classes at the end of the inheritance chain.
* All class fields defined by constructor parameters should be labelled as `private final`, unless not possible due to reflection.
* Do not label in-method scoped variables as `final` unless (optional) they are required to be effectively final to be used inside a lambda.

### JavaDocs

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

# Testing

Tests are an important part of development.
Where applicable, please ensure that you write unit and/or integration tests to test your business logic.
In any case, please ensure that any new feature or bugfix is verified with a system test (i.e., run the bot manually and see if it works).