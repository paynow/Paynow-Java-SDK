# Java SDK for Zimbabwe's leading payment gateway, Paynow

![paynow](https://user-images.githubusercontent.com/9304071/54472720-c0d06600-47d5-11e9-8995-23276d3acc28.png)

This repository contains the official Paynow SDK for use with Java.

> Note: We have just released a new version of our Java SDK, 1.1.0 which represents a major refactor of the code, with the goal of making this SDK easier to use. Some package structures and object names have been changed and you may need to update your code if you were previously using version 1.0.0. See the Wiki section for details on how to use the newer version SDK.

## Prerequisites

In order to make use of this project, the following prerequisites must be met for it to work.

1. [Setup your developer account](https://github.com/paynow/Paynow-Java-SDK/wiki/Setting-up-your-developer-account) to obtain a integration ID and key
2. In your project, make use of Java JDK 7 or higher

## Import the latest release into your project
To use the Java Paynow SDK, you need to add the latest release of this project as a dependency.

#### Gradle
```gradle
repositories {
	mavenCentral()
}
dependencies {
	compile group: ‘zw.co.paynow’, name: ‘java-sdk’, version: ‘1.0.0’
}
```

#### Maven
```xml
<dependency>
    <groupId>zw.co.paynow</groupId>
    <artifactId>java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Get Started
To see a full code demo of how to use the SDK, see this [example](https://github.com/paynow/Paynow-Java-SDK/wiki/Full-usage-example). Otherwise, for a more in depth explanation on various usages of this SDK, have a look at the pages below.

- [<sup>\[PDF\]</sup> Download Paynow API Documentation](https://www.paynow.co.zw/Content/Paynow%203rd%20Party%20Site%20and%20Link%20Integration%20Documentation.pdf)
- [Initiate a web transaction](https://github.com/paynow/Paynow-Java-SDK/wiki/Web-based-transaction)
- [Initiate a mobile transaction](https://github.com/paynow/Paynow-Java-SDK/wiki/Mobile-based-transaction)
- [Poll your transaction to check for the payment status](https://github.com/paynow/Paynow-Java-SDK/wiki/Checking-transaction-status)

## Using the SDK with Spring Boot
A Spring Boot SDK which makes use of this Java SDK has been developed by the community. View the project [here](https://github.com/tzifudzi/SpringBoot-Java-SDK) if you would like to use it in your project.

## Contributions
Pull requests and new issues are very welcome. If you identify an issue with the SDK please raise it in the [issues](https://github.com/paynow/Paynow-Java-SDK/issues) section. If you have solved any issues or have suggestions to improve the SDK, please create a pull request.