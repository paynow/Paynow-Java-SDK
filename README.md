# Java SDK for Zimbabwe's leading payment gateway, Paynow

This repository contains the official Paynow SDK for use with Java. For more details on the Paynow API, have a look at the official documentation at the [Paynow Developer Hub](https://developers.paynow.co.zw/), or [download](https://www.paynow.co.zw/Content/Paynow%203rd%20Party%20Site%20and%20Link%20Integration%20Documentation.pdf) the PDF version of the documentation.

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

## Get Started
As shown below, create an instance of `Paynow` associated with the integration ID and integration key as supplied by Paynow. The Paynow object you create can be used for multiple transactions.

```java
Paynow paynow = new Paynow("INTEGRATION_ID", "INTEGRATION_KEY");
```

## Initiating a web based transaction
The usage example shown in this section is a demonstration of how to initiate a web based transaction. A web based transaction is made over the web, via the Paynow website. Many forms of payments are accepted including ZIPIT, VPayments, MasterCard and VISA.

After creating the `Paynow` object, optionally set the result and return URLs. 
Result URL is the URL on the merchant website where Paynow will post transaction results to.
Return URL is the URL on the merchant website the customer will be returned to after the transaction has been processed. If you do not specify a result and return URL, you will have to poll for the status of your transaction to check if the transaction is paid.

```java
paynow.setResultUrl("http://example.com/gateways/paynow/update");
paynow.setReturnUrl("http://example.com/return?gateway=paynow&merchantReference=1234");
```

Create a new payment using any of the `createPayment(...)` methods, while ensuring you pass your own unique reference for that payment (e.g invoice id, or anything that you can use to identify the transaction). For web based transactions, only this reference is required when creating the Payment object. If you also pass in the email address, Paynow will attempt to auto login the customer at the Paynow website using this email address if it is used with a registered account.

```java
Payment payment = paynow.createPayment("Invoice 35");
```

You can then start adding items to the payment cart

```java
// Passing in the name of the item and the price of the item
payment.add("Bananas", 2.5);
payment.add("Apples", 1.0);
```

Once you're done building up your cart and you're finally ready to submit the payment request, initiate the transaction by submitting it to Paynow. This is done by calling the `send(Payment payment)` method on the `Paynow` object you created, passing the `Payment` object you created earlier as an argument. You can also optionally set the description of the cart which will be shown to the user at the Paynow website, otherwise a default description will be used. 

```java
payment.setCartDescription("Some custom description");//this is optional

// Save the response from paynow in a variable
WebInitResponse response = paynow.send(payment);
```

The method will return an instance of the `WebInitResponse` class. The `WebInitResponse` object returned will contain parsed information about the response from Paynow. Among this information will be the redirect URL, and you should redirect the user to this URL on the Paynow website so that they can make the payment and then return to your website or application at the return URL you would have specified.

If the request was successful, you should consider saving the poll URL sent from Paynow in your database so that you can use it later to check if the transaction has been paid. To tell if the request was successful, check the success status of the transaction by calling the `isRequestSuccess()` method.

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
The usage example shown in this section is a demonstration of how to initiate a mobile based transaction. A mobile transaction is a transaction made using mobile money i.e. Using EcoCash, TeleCash, or OneMoney.

> Note: Mobile based transactions currently only work for Ecocash with Econet numbers

Create a new payment using the `createPayment(...)` method that requires a reference and email address of the user making the payment. These two arguments are required for a mobile based transaction otherwise an exception will be thrown when submitting the transaction initiation. You can also optionally set the description of the cart which will be shown to the user at the Paynow website, otherwise a default description will be used.
 
```java
Payment payment = paynow.createPayment("Invoice 32", "user@example.com");
```

Adding items to the cart is the same as in web based transactions.

```java
// Passing in the name of the item and the price of the item
payment.add("Bananas", 2.5);
payment.add("Apples", 1.0);
```

Once you're done building up your cart and you're finally ready to submit the payment request, initiate the transaction by submitting it to Paynow. This is done by calling the `sendMobile(Payment payment) ` method on the Paynow object you created, passing the Payment object you created earlier as an argument, the phone number of the user, and the mobile money payment method. 

```java
MobileInitResponse response = paynow.sendMobile(payment, "0771234567", MobileMoneyMethod.ECOCASH);
```

The method will return an instance of the `MobileInitResponse` class. The `MobileInitResponse` object returned will contain parsed information about the response from Paynow. Among this information will be the instructions for your customer on how to make the payment on their mobile phone.

If the request was successful, you should consider saving the poll URL sent from Paynow in your database so that you can use it later to check if the transaction has been paid. To tell if the request was successful, check the success status of the transaction by calling the `isRequestSuccess()` method.

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

        if (response.isRequestSuccess()) {
            // Get the url to redirect the user to so they can make payment
            String link = response.redirectURL();

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