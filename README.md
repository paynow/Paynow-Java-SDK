# Java SDK for Zimbabwe's leading payment gateway, Paynow

## Prerequisites

This library has the following set of prerequisites that must be met for it to work.

1.  Java JDK 6 or higher

## Integration

To use the Java Paynow SDK, you need to add the latest release of this project as a dependency.

#### Gradle
```gradle
repositories {
	mavenCentral()
}
dependencies {
	compile group: ‘zw.co.paynow’, name: ‘java-sdk’, version: ‘1.+’
}
```

#### Maven
```xml
<dependency>
    <groupId>zw.co.paynow</groupId>
    <artifactId>java-sdk</artifactId>
    <version>[1.0.0,)</version>
</dependency>
```

## Usage

To get started using the SDK, do the following.
### Importing library
Import the required classes as follows.

```java
import zw.co.paynow.core.*;
import zw.co.paynow.core.Paynow;
import zw.co.paynow.core.Payment;
```

As shown below, create an instance of `Paynow` associated with the integration ID and integration key as supplied by Paynow. The Paynow object you create can be used for multiple transactions.

```java
Paynow paynow = new Paynow("INTEGRATION_ID", "INTEGRATION_KEY");
```

## Web transaction usage example

The usage example shown in this section is a demonstration of how to initiate and complete a web based transaction.

After creating the Paynow object, optionally set the result and return URLs. The return URL can be set at later stages, for example, you might want to do this if you want to pass data to the return url ( the merchantReference of the transaction as shown below).

```java
paynow.setResultUrl("http://example.com/gateways/paynow/update");
paynow.setReturnUrl("http://example.com/return?gateway=paynow&merchantReference=1234");
```

Create a new payment using any of the `createPayment(...)` methods, passing in the merchantReference for that payment (e.g invoice id, or anything that you can use to identify the transaction). For web based transactions, only the merchantReference is required when creating the Payment object.

```java
Payment payment = paynow.createPayment("Invoice 35");
```

You can then start adding items to the payment cart

```java
// Passing in the name of the item and the price of the item
payment.add("Bananas", 2.5);
payment.add("Apples", 3.4);
payment.setCartDescription("custom desc");
```

Once you're done building up your cart and you're finally ready to submit the payment to Paynow, initiate the transaction. This is done by calling the `send(Payment payment)` method using the `Paynow` object you created, passing the `Payment` object you created as an argument.

```java
// Save the response from paynow in a variable
InitResponse response = paynow.send(payment);
```

The send method will return an instance of the `InitResponse` class. The InitResponse object returned will contain information about the response from Paynow like whether the transaction initiation request was successful or not. For example, if it was successful, it contains the URL to redirect the user to, so they can make the payment.

If the request was successful, you should consider saving the poll URL sent from Paynow in your database. To tell if the request was successful, check the success status of the transaction by calling the `success()` method.

```java
InitResponse response = paynow.send(payment);

if (response.success()) {   
    // Get the url to redirect the user to so they can make payment
    String link = response.redirectLink();
    
    // Get the poll URL of the transaction
    String pollUrl = response.pollUrl(); 
}
```

## Mobile transaction usage example

> Note: Mobile based transactions currently only work for Ecocash with Econet numbers

The usage example shown in this section is a demonstration of how to initiate and complete a mobile based transaction.

If you want to send an express (mobile) checkout request instead, when creating a payment you make a call to a `createPayment(...)` method that requires both the merchantReference and email address of the user making the payment. These two arguments are required for a mobile based transaction otherwise an exception will be thrown when submitting the payment. 

```java
Payment payment = paynow.createPayment("Invoice 32", "user@example.com");
```

Adding items to the cart is the same as in web based transactions.

```java
// Passing in the name of the item and the price of the item
payment.add("Bananas", 2.5);
payment.add("Apples", 3.4);
```

To submit the payment to Paynow for the transaction to be initiated, instead of using the `send(...)` method, call the the `sendMobile(...)` method using the Paynow object you created, passing the Payment object you created as an argument, the phone number of the user, and the mobile money payment method. 

```java
InitResponse response = paynow.sendMobile(payment, "0772XXXXXX", MobileMoneyMethod.ECOCASH)
```

With mobile money transactions, the same `InitResponse` class is used to create the response object, however, the `redirectLink()` method will not return anything. Rather, make use of the `instructions()` method which will contain instructions that should ideally be shown to the user instructing them how to make the payment on their mobile phone. 

```java

// Check if request was successful
if (response.success()) 
{   
    // Get the instructions to show to the user
    String instructions  = response.instructions();
    
    // Get the poll url (used to check the status of a transaction). You might want to save this in your DB
    String pollUrl = response.pollUrl(); 
    
} else {
    // Ahhhhhhhhhhhhhhh
    // *freak out*
}
```

## Checking transaction status

It is possible to check the status of a transaction i.e. if the payment has been paid. To do this, make sure after initiating the transaction, you take note of the poll URL. With this URL, call the `pollTransaction(...)` method of the `Paynow` object you created as follows. Note that checking transaction status is the same for web and mobile based transasctions.

```java
// Check the status of the transaction with the specified pollUrl
// Now you see why you need to save that URL ;-)
StatusResponse status = paynow.pollTransaction(pollUrl);

if (status.paid()) {
  // Yay! Transaction was paid for
} else {
  System.out.println("Why you no pay?");
}
```

## Full Usage Example

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
        InitResponse response = paynow.send(payment);

        if (response.success()) {   
            // Get the url to redirect the user to so they can make payment
            String link = response.redirectLink();
            
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
              System.out.println(response.error());
        }
    }

}
```
