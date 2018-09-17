package webdev.payments;

import java.util.*;
import java.math.*;

/** 
	 Represents a single transaction to be sent to Paynow
*/
public class Payment
{
	public Payment(String reference, String authEmail)
	{
		setReference(reference);
		Items = new HashMap<String, BigDecimal>();
		AuthEmail = authEmail;
	}

	public Payment(String reference, HashMap<String, BigDecimal> values, String authEmail)
	{
		setReference(reference);
		Items = values;
		AuthEmail = authEmail;
	}

	/** 
		 This is the reference for the transaction (like an id in the database)
	*/
	private String Reference;
	public final String getReference()
	{
		return Reference;
	}
	public final void setReference(String value)
	{
		Reference = value;
	}

	/** 
		 List of the items in the transaction
	*/
	private HashMap<String, BigDecimal> Items;
	private HashMap<String, BigDecimal> getItems()
	{
		return Items;
	}

	/** 
		 Get the total of the items in the transaction
	*/
	public final BigDecimal getTotal()
	{
		return GetTotal();
	}

	/** 
	 Email to be sent to Paynow with the transaction 
	*/
	public String AuthEmail = "";


	/** 
		 Add a new item to the transaction
	 
	 @param title The name of the item
	 @param amount The cost of the item
	*/
	public final Payment Add(String title, BigDecimal amount)
	{
		getItems().put(title, amount);

		return this;
	}

	/** 
		 Remove an item from the transaction
	 
	 @param title
	*/
	public final Payment Remove(String title)
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var exists = getItems().Any(item = title.equals(> item.Key));
		if (exists)
		{
			getItems().remove(title);
		}

		return this;
	}

	/** 
		 Get the string representation of the items in the transaction
	*/
	public final String ItemsDescription()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var sb = getItems().Aggregate(new StringBuilder(), (current, next) -> current.AppendFormat("{0}, ", next.Key));

		return sb.toString().trim();
	}

	/** 
		 Get the total cost of the items in the transaction
	*/
	private BigDecimal GetTotal()
	{
		return getItems().Aggregate(java.math.BigDecimal.ZERO, (current, next) -> current += next.Value);
	}

	/** 
		 Get the items in the transaction as a dictionary
	 
	 @return 
	*/
	public final HashMap<String, String> ToDictionary()
	{
		return new HashMap<String, String>(Map.ofEntries(Map.entry("resulturl", ""), Map.entry("returnurl", ""), Map.entry("reference", getReference()), Map.entry("amount", getTotal().toString(CultureInfo.CurrentCulture)), Map.entry("id", ""), Map.entry("additionalinfo", ItemsDescription()), Map.entry("authemail", AuthEmail), Map.entry("status", "Message")));
	}
}