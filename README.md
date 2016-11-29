![Travis build status](https://api.travis-ci.org/mahnkong/byteman-junit-rules.svg?branch=develop)
[ ![Download](https://api.bintray.com/packages/mahnkong/maven/byteman-junit-rules/images/download.svg) ](https://bintray.com/mahnkong/maven/byteman-junit-rules/_latestVersion)
# byteman-test-utils

This project contains functionality allowing the usage of [byteman](http://byteman.jboss.org/) in JUnit tests by providing [JUnit](http://junit.org/) rules (install byteman agent, load/unload of the rules) and some annotations (byteman setup for the tests). Byteman can also be used in JUnit tests by using the BMUnitRunner class provided by byteman, but this will prevent the usage of other runners (i.e. the [Arquillian](http://arquillian.org/) runner)

The project requires byteman to be installed on the machine where the tests are executed. The path to byteman can be set as environment variable (BYTEMAN_HOME), so the rules will find it without specifying any parameter. Additionally, the path to byteman can also be specified when creating the rule objects.

The versions used developing (and testing) this project where:

 * Junit: 4.12
 * Byteman: 3.0.6
 

## BytemanAgentInstaller

This rule installs the byteman agent into the JVM of the test. **This rule must be annotated as ClassRule, so that the agent gets installed only once into the JVM!**

The builder allows the setting of the following options:

* bytemanHome: specifies the path to byteman
* bindAddress: address that the listener binds itself to (default = localhost)
* bindPort: port that the listener binds itself to (default = 9091)
* verbose: adds verbosity for the agent installation
* installIntoBootstrapClasspath: installs the byteman agent into the bootstrap classpath (bminstall -b)
* accessAllAreas: sets an access-all-areas security policy for the byteman rules (bminstall -s)
* transformAll: sets the "-Dorg.jboss.byteman.transform.all" property when installing the agent (see byteman docs for more details)

Example definition inside a test case:

```java
public class MyTest {

  @ClassRule
  public static BytemanAgentInstaller bytemanAgentInstaller = new BytemanAgentInstaller.Builder().build();

}
```

## BytemanRuleSubmitter

This rule loads the byteman rules specified for the tests into the byteman agent and unloads them on test completion.

The builder allows the setting of the following options:

* bytemanHome: specifies the path to byteman

Example definition inside a test case:

```java
public class MyTest {

  @Rule
  public BytemanRuleSubmitter bytemanRuleSubmitter = new BytemanRuleSubmitter.Builder().build();
    
}
```

## Annotations for the use of byteman

2 annotations exist for setting up the test class and test methods for the use of byteman:

### BytemanRuleFile

This annotation can be applied to classes and methods and specifies the rules to be loaded into the byteman agent before executing the test. Currently there exist 2 options to the annotation:

* filepath: mandatory, specifies where to find the rule file (can be relative to the project directory)
* verbose: optional, if active all byteman gets executed in verbose mode

Rules configured on class level are loaded before **each** test and unloaded afterwards. Rules configured on method level are loaded only before the test they are annotated to and unloaded after test execution. If the test class and the test method are both annotated with BytemanRuleFile, first the class rules are loaded and afterwards the method rules. Then again the test gets executed and afterwards the method rules are unloaded before the class rules. 

Example definition inside a test case (the rules here are stored inside *src/test/resources* and therefore get copied to *target/test-classes*:

```java
@BytemanRuleFile(filepath = "target/test-classes/myclassrule.btm")
public class MyTest {

  @Test
  @BytemanRuleFile(filepath = "target/test-classes/mymethodrule.btm", verbose = true)
  public void myTestMethod() {
    //the test
  }
}
```
### IgnoreBytemanClassRuleFile

This annotation can be applied to methods and specifies, that for the test it is annotated on no class level rules get executed (only the test method rules are applied)

Example definition inside a test case:

```java
@BytemanRuleFile(filepath = "target/test-classes/myclassrule.btm")
public class MyTest {

  @Test
  @BytemanRuleFile(filepath = "target/test-classes/mymethodrule.btm", verbose = true)
  @IgnoreBytemanClassRuleFile
  public void myTestMethod() {
    //the test
  }
}
```

Please check the integration tests (*IT.java) for complete examples showing how to utilise the project.
