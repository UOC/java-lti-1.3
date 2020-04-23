# LTI 1.3 & LTI Advantage Java Library

Library implementing a full [LTI Advantage](https://www.imsglobal.org/activity/learning-tools-interoperability) tool.

## Install
This library depends on two additional libraries:

* [LTI 1.3 core](https://github.com/UOC/java-lti-1.3-core)
* [LTI 1.3 JWT](https://github.com/UOC/java-lti-1.3-jwt) for testing

Install it using maven:

```bash
./mvnw install
```

## Documentation

The basic class in the library is `edu.uoc.elc.lti.tool.Tool`, which defines a Tool. It has the basic methods:

* `public boolean validate(String token, String state)`
* `public AccessTokenResponse getAccessToken() throws IOException, BadToolProviderConfigurationException`
* `public NamesRoleService getNameRoleService()`
* `public DeepLinkingClient getDeepLinkingClient()` 
* `public AssignmentGradeService getAssignmentGradeService()`

It also has utility methods for getting claims in an agnostic way

## Configuration

Configuration of the Tool is made through the class `edu.uoc.elc.lti.tool.ToolDefinition`. 
There you can set the following parameters of the tool:

* `clientId`
* `name`: Name of the tool
* `platform`: Name of the platform
* `keySetUrl`:
* `accessTokenUrl` 
* `oidcAuthUrl`
* `privateKey`
* `publicKey`
* `deploymentId`

Tool uses [LTI 1.3 core](https://github.com/UOC/java-lti-1.3-core#about) interfaces for dealing 
with requests and JWT generation. The definition of the implementations of these interfaces are
in the `edu.uoc.elc.lti.tool.ToolBuilders` class
 
## Usage

1. Set your maven installation to work with Github packages, following https://help.github.com/en/packages/using-github-packages-with-your-projects-ecosystem/configuring-apache-maven-for-use-with-github-packages#authenticating-to-github-packages

Repository info:
  
  ```xml
        <repository>
          <id>github-uoc</id>
          <name>GitHub UOC Apache Maven Packages</name>
          <url>https://maven.pkg.github.com/uoc</url>
        </repository>				
  ```

2. Add the dependency to your `pom.xml` file:

```xml
  <dependency>
    <groupId>edu.uoc.elc.lti</groupId>
    <artifactId>lti-13</artifactId>
    <version>0.0.2</version>
  </dependency>
```  

## Contributing

Thanks for being interested in this project. The way of contributing is the common for almost all projects:

1. Fork the project to your account
2. Implement your changes
3. Make a pull request

If you need further information contact to `xaracil at uoc dot edu`
