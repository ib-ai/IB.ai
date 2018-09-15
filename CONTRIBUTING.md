# Contributing

If you are reading this page, then thank you for your interest in contributing towards the bot. 
We are grateful for any help, however, please ensure you follow the guidelines laid out below.
Ensure that any code you produce for IB.ai is licensed with the Apache License 2.0. 

# Contributions

Not all contribution PRs (read below) will be accepted. For ideas as to what to contribute please refer to the issues section.

# VCS

1) Create a new branch. Label it appropriately.
2) Make the changes on that branch.
3) Commit to and push the branch.
4) Create a PR.

# Code

## Formatting

* Use Java-style braces (moustache-bois). 
This means that the opening brace is on the same line, but the closing brace is on a new line. 
There must be a space before an opening brace.
* Do not use a space between a keyword and the opening parentheses `if(that)` rather than `if (that)`.
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

## Access and mofification

* Use the lowest possible access modifier.
* Use the `final` modifier on all classes at the end of the inheritance chain.
* All class fields defined by constructor parameters should be labled as `private final`, unless not possible due to reflection.
* Do not label in-method scoped variables as `final` unless (optioanl) they are required to be effectively final to be used inside a lambda.

## Singletons

* Use an enumeration, with the only field being the value `INSTANCE`. 
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
