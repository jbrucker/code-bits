## Contents

1. How to use a properties file for configuration data.
2. A simpler count-down timer for TypingThrower.

## Use a properties file for configuration data

You should not hardcode IP addresses or login credentials as string constants in Java.  Use the guides "*No magic numbers*" and "*Separate what varies from what stays the same*", and remove configuration data from Java code.

A standard solution is to use a properties file.  Things you should include in a properties file are:

* IP address and port numbers
* URLs, including JDBC URL
* Authentication parameters (login, password). **Never** put these is Java code.
* JDBC SQL Driver class name (so you can change it!)
* Anything that is likely to vary, even during testing

Many open-source projects do this; for example, Log4J uses log4j.properties.

If you put authentication data in Java code, anyone with access to your source code 
on Github can steal your database URL and password.
You can also steal them from a JAR file by scanning for Strings in the Java bytecode.

### Example properties file

The format is the same for both "properties" and "resource bundles".
Here is an example for the Typing Thrower application:
```
# Properties used in TypingThrower
# Add this file to .gitignore

# key = value (don't put quotes around values)
jdbc.url = jdbc:mysql://10.11.22.33:9999/names
jdbc.user = foo
jdbc.password = fatchance
# Needed for JDBC, maybe not necessary for ORMlite
jdbc.drivers = com.mysql.jdbc.Driver
# Client side should have a separate properties file
server.addr = 10.11.22.99
server.port = 3007
```
Comment lines (#) and blank lines are allowed. The property keys (on left side of '=' sign) may contain '.' to avoid name collisions, such as `jdbc.user`.  The right side can be any string, without quotes.  By convention, property names (keys) are lowercase.

A properties file is usually named `myapp.properties` or `myapp.config`
and put at the top of the `src` tree so its easy to find.
For example, Log4J uses a config file named log4j.properties.
As explained below, its good to give user a way to override this.

### Example Property.java and PropertyManager.java

See the code in the `util` package. PropertyManager provides static
access to properties.  The `PropertyManager.main` method is a demo.

The file named `Property` is just an enum for the property names,
so you don't need to use strings for property names.  
The problem with Strings for property names is that if you spell the
name wrong you get back a null property, and the mistake can be hard to find.
Using an enum avoids this problem.

In PropertyManager you can get property values using the enum names
or String names (in case you want to add a property not in the enum).
For example:
```java
// get values for database connection:
public class DatabaseConnect {
    //TODO For security, don't use static constants because they are easier to steal from JVM memory.
    // better to use local variables and discard them.
	private final static String USERNAME = PropertyManager.getProperty(Property.DATABASE_USER);
	private final static String PASSWORD = PropertyManager.getProperty(Property.DATABASE_PASSWORD);
	private final static String URL = PropertyManager.getProperty(Property.DATABASE_URL);
```

or, using Strings to get the same properties:

```java
public class DatabaseConnect {
	private final static String USERNAME = PropertyManager.getProperty("jdbc.user");
	private final static String PASSWORD = PropertyManager.getProperty("jdbc.password");
	private final static String URL = PropertyManager.getProperty("jdbc.url");
```

The code for chatapplication has the string values repeatedly used in several classes,
for example:
```java
Class.forName("com.mysql.jdbc.Driver").newInstance();
con = DriverManager.getConnection("jdbc:mysql://10.11.22.33:3306/projectdb?useSSL=false",
			"root",
			"dont_use_root!");
```

First (of course), use properties instead of string constants.
Second, don't spread JDBC code throughout your appliation.  There should be **only one place**
where you load the JDBC driver and create a connection.  The entire app can share one connection.
If you use ORMlite it handles this for you.

If you need to change the database user URL or password you have to change it many places.
Plus, anyone can look at your source code on Github and steal your password.

### Specify the properties file on command line or via a system property

You should provide a way for someone to override the name of the properties file
without changing the code. This is useful for testing and demos.
In PropertyManager it gets the properties filename like this:
```
// the name of the properties file to read
String filename = System.getProperty( "properties", Property.PROPERTY_FILENAME.name);
```
This code looks for a system property named "properties".  The user can
specify this by setting an environment variable or as a Java command line `-D` option:
```
java -Dproperties=mystuff.config  TypingThrower.jar
```
In this case, the app will read properties data from `mystuff.config`. If the user doesn't specify a "properties" value, then the default filename is used.
In my code, the default properties filename in contained in the value of Property.PROPERTY_FILENAME so its easy to find and change it.

You could also make PropertyManager be a singleton and use instance methods, to avoid using static storage for Properties object. For better security, encrypt sensitive strings in the properties file.

### Don't Commit the Properties file into Github

Since the properties file contains private information, add it to `.gitignore` to avoid committing to git.

## Timer and TimerTask example

In TypingThrower `HomeUI.java` the code for countdown timer is too complex.  `Countdown.java` is example how to simplify it.

# Math in Markdown?

A test of math rendering on Github: $a^2x^2 + bx + c = g_0(x)$ or maybe  \(a^2x^2 + bx + c = g_0(x)\)

Using external renderer: ![a_{n}x^{n}+a_{n-1}x^{n-1}+...+a_{0}](https://latex.codecogs.com/gif.latex?a_{n}x^{n}&plus;a_{n-1}x^{n-1}&plus;...&plus;a_{0})
