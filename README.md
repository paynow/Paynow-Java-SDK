# Java SDK for Paynow API

## Sign in to Paynow and get integration details

> Before you can start making requests to Paynow's API, you need to get an integration ID and integration Key from Paynow. Details about how you can retrieve the ID and key are explained in detail on [this page](generation.md)

## Prerequisites

In order to make use of this project, the following prerequisites must be met for it to work.

1. Java JDK 7 or higher

## Installation
To use the Java Paynow SDK, you need to add the latest release as a dependency to your project. The latest release will be in the [Maven Central Repository](https://mvnrepository.com/artifact/zw.co.paynow/java-sdk).

#### Gradle
```gradle
repositories {
	mavenCentral()
}
dependencies {
	implementation 'zw.co.paynow:java-sdk:1.1.0'
}
```

#### Maven
```xml
<dependency>
    <groupId>zw.co.paynow</groupId>
    <artifactId>java-sdk</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Getting started
Create an instance of `Paynow` associated with your integration ID and integration key as supplied by Paynow. 

```java
Paynow paynow = new Paynow("INTEGRATION_ID", "INTEGRATION_KEY");
```

## Initiating a web based transaction
A web based transaction is made over the web, via the Paynow website.

You can optionally set the result and return URLs. 

* Result URL is the URL on the merchant website where Paynow will post transaction results to.
* Return URL is the URL where the customer will be redirected to after the transaction has been processed. If you do not specify a return URL, you will have to rely solely on polling status updates to determine if the transaction has been paid.

```java
paynow.setResultUrl("http://example.com/gateways/paynow/update");
paynow.setReturnUrl("http://example.com/return?gateway=paynow&merchantReference=1234");
```

Create a new payment using any of the `createPayment(...)` methods, ensuring you pass your own unique reference for that payment (e.g invoice id). If you also pass in the email address, Paynow will attempt to auto login the customer at the Paynow website using this email address if it is associated with a registered account.

```java
Payment payment = paynow.createPayment("Invoice 35");
```

You can then start adding items to the payment cart.

```java
// Passing in the name of the item and the price of the item
payment.add("Bananas", 2.5);
payment.add("Apples", 1.0);
```

When you are ready to submit the payment request, initiate the transaction by calling the `send(...)` method. You can optionally set the description of the cart which will be shown to the user at the Paynow website, otherwise a default description will be generated. 

```java
payment.setCartDescription("Some custom description");//this is optional

// Save the response from paynow in a variable
WebInitResponse response = paynow.send(payment);
```

The `WebInitResponse` response from Paynow will contain various information including:
* redirect URL where you should redirect the customer to make the payment
* poll URL to check if the transaction has been paid

If the request was successful, you should consider saving the poll URL sent from Paynow in your database so that you can use it later to check if the transaction has been paid.

```java
if (response.success()) {   
    // Get the url to redirect the user to so they can make payment
    String redirectUrl = response.redirectURL();
    
    // Get the poll URL of the transaction
    String pollUrl = response.pollUrl();
} else {
    // Ahhhhhhhhhhhhhhh
    // *freak out*
}
```

## Initiating a mobile based transaction
A mobile transaction is a transaction made using mobile money e.g. using Ecocash

> Note: Mobile based transactions currently only work for Ecocash with Econet numbers

Create a new payment using the `createPayment(...)` method that requires a unique merchant reference and the email address of the user making the payment.
 
```java
Payment payment = paynow.createPayment("Invoice 32", "user@example.com");
```

Adding items to the cart is the same as in web based transactions.

```java
// Passing in the name of the item and the price of the item
payment.add("Bananas", 2.5);
payment.add("Apples", 1.0);
```

When you are ready to submit the payment request, initiate the transaction by calling the `sendMobile(...)` method. 
```java
MobileInitResponse response = paynow.sendMobile(payment, "0771234567", MobileMoneyMethod.ECOCASH);
```


The `MobileInitResponse` response from Paynow will contain various information including:
* instructions for your customer on how to make the payment on their mobile phone
* poll URL to check if the transaction has been paid

If the request was successful, you should consider saving the poll URL sent from Paynow in your database so that you can use it later to check if the transaction has been paid.

```java
if (response.success()) {   
    // Get the instructions to show to the user
    String instructions  = response.instructions();

    // Get the poll URL of the transaction
    String pollUrl = response.pollUrl();
} else {
    // Ahhhhhhhhhhhhhhh
    // *freak out*
}
```

## Poll the transaction to check for the payment status
It is possible to check the status of a transaction i.e. if the payment has been paid. To do this, make sure after initiating the transaction, you take note of the poll URL in the response. With this URL, call the `pollTransaction(...)` method of the `Paynow` object you created as follows. Note that checking transaction status is the same for web and mobile based transasctions.

```java
// Check the status of the transaction with the specified pollUrl
StatusResponse status = paynow.pollTransaction(pollUrl);

if (status.isPaid()) {
  // Yay! Transaction was paid for
} else {
  System.out.println("Why you no pay?");
}
```

## Full usage example
The following is a full usage example for web based transactions. 

```java
// MakingFirstPayment.java
import zw.co.paynow.core.*;
import zw.co.paynow.core.Paynow;
import zw.co.paynow.core.Payment;

public class MakingFirstPayment {

    public static void main(String[] args) {
        Paynow paynow = new Paynow("INTEGRATION_ID", "INTEGRATION_KEY");

        Payment payment = paynow.createPayment("Invoice 35");

        // Passing in the name of the item and the price of the item
        payment.add("Bananas", 2.5);
        payment.add("Apples", 3.4);

        //Initiating the transaction
        WebInitResponse response = paynow.send(payment);
        
        //If a mobile transaction,
        //MobileInitResponse response = paynow.sendMobile(payment, "0771234567", MobileMoneyMethod.ECOCASH);

        if (response.isRequestSuccess()) {
            // Get the url to redirect the user to so they can make payment
            String redirectURL = response.redirectURL();

            // Get the poll url of the transaction
            String pollUrl = response.pollUrl();

            //checking if the payment has been paid
            StatusResponse status = paynow.pollTransaction(pollUrl);

            if (status.paid()) {
                // Yay! Transaction was paid for
            } else {
                System.out.println("Why you no pay?");
            }

        } else {
            // Something went wrong
            System.out.println(response.errors());
        }
    }

}
```