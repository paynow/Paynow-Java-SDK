# Java SDK for Zimbabwe's leading payment gateway, Paynow

## Prerequisites

This library has a set of prerequisites that must be met for it to work

1.  Java JDK 6 or higher

## Installation

There are multiple ways to download PayPal Java SDK dependency, based on your dependency manager. Here are the most popular ones:

### Installing using [Gradle](https://gradle.org/install/)
```gradle
repositories {
	mavenCentral()
}
dependencies {
	compile 'zw.paynow:java-sdk:+'
}
```

### Installing using [Maven](https://maven.apache.org/)
```xml
<dependency>
	<groupId>zw.paynow</groupId>
	<artifactId>java-sdk</artifactId>
	<version>LATEST</version>
</dependency>
```


## Usage example

### Importing library

```java
import webdev.core.*;
import webdev.payments.Paynow;
import webdev.payments.Payment;
```

Create an instance of the Paynow class optionally setting the result and return url(s)

```java
Paynow paynow = new Paynow("INTEGRATION_ID", "INTEGRATION_KEY");

paynow.setResultUrl("http://example.com/gateways/paynow/update");
paynow.setReturnUrl("http://example.com/return?gateway=paynow");
// The return url can be set at later stages. You might want to do this if you want to pass data to the return url (like the reference of the transaction)
```

Create a new payment passing in the reference for that payment (e.g invoice id, or anything that you can use to identify the transaction.

```java
Payment payment = paynow.createPayment("Invoice 35");
```

You can then start adding items to the payment

```java
// Passing in the name of the item and the price of the item
payment.add("Bananas", 2.5);
payment.add("Apples", 3.4);
```

Once you're done building up your cart and you're finally ready to send your payment to Paynow, you can use the `Send` method in the `paynow` object.

```java
// Save the response from paynow in a variable
InitResponse response = paynow.send(payment);
```

The send method will return an instance of the `InitResponse` class, the InitResponse object being the response from Paynow and it will contain some useful information like whether the request was successful or not. If it was, for example, it contains the url to redirect the user so they can make the payment. You can view the full list of data contained in the response in our wiki

If request was successful, you should consider saving the poll url sent from Paynow in your database

```java
InitResponse response = paynow.send(payment);

if(response.success()) 
{   
    // Get the url to redirect the user to so they can make payment
    String link = response.redirectLink();
    
    // Get the poll url of the transaction
    String pollUrl = response.pollUrl(); 
}
```

## Mobile Transactions

If you want to send an express (mobile) checkout request instead, when creating a payment you make a call to the `CreateMobilePayment` instead of the `CreatePayment` method. The `CreateMobilePayment` method unlike the `CreatePayment` method requires that you pass in the email address of the user making the payment. 

Additionally, you send the payment to Paynow by making a call to the `SendMobile` in the `paynow` object
instead of the `Send` method. The `SendMobile` method unlike the `Send` method takes in two additional arguments i.e The phone number to send the payment request to and the mobile money method to use for the request. **Note that currently only ecocash is supported**

```java
// Create a mobile payment
Payment payment = paynow.createMobilePayment("Invoice 32", "user@example.com");

// Add items to the payment
payment.add("Bananas", 2.5);
payment.add("Apples", 3.4);

// Send the payment to paynow
paynow.sendMobile(payment)
```

The response object is almost identical to the one you get if you send a normal request. With a few differences, firstly, you don't get a url to redirect to. Instead you instructions (which ideally should be shown to the user instructing them how to make payment on their mobile phone)

```java
InitResponse response = paynow.sendMobile(payment);

// Check if request was successful
if(response.success()) 
{   
    // Get the url to redirect the user to so they can make payment
    String link = response.redirectLink();
    
    // Get the poll url (used to check the status of a transaction). You might want to save this in your DB
    String pollUrl = response.pollUrl(); 
    
    // Get the instructions
    String instructions =  response.instructions();
}
else
{
    // Ahhhhhhhhhhhhhhh
    // *freak out*
}
```

## Checking transaction status

The SDK exposes a handy method that you can use to check the status of a transaction. Once you have instantiated the Paynow class.

```java
// Check the status of the transaction with the specified pollUrl
// Now you see why you need to save that url ;-)
StatusResponse status = paynow.CheckTransactionStatus(pollUrl);

if (status.paid()) {
  // Yay! Transaction was paid for
} else {
  System.out.println("Why you no pay?");
}
```

## Full Usage Example

```java
// MakingFirstPayment.java
import webdev.core.*;
import webdev.payments.Paynow;
import webdev.payments.Payment;

public class MakingFirstPayment {

    public static void main(String[] args) {
        Paynow paynow = new Paynow("INTEGRATION_ID", "INTEGRATION_KEY");

        Payment payment = paynow.createPayment("Invoice 35");
    
        // Passing in the name of the item and the price of the item
        payment.add("Bananas", 2.5);
        payment.add("Apples", 3.4);
        
        InitResponse response = paynow.send(payment);

        if(response.success()) 
        {   
            // Get the url to redirect the user to so they can make payment
            String link = response.redirectLink();
            
            // Get the poll url of the transaction
            String pollUrl = response.pollUrl(); 
        }
        else
        {
            // Something went wrong
              System.out.println(response.error());
        }
    }

}
```
