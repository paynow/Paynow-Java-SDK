package webdev.core;

import webdev.exceptions.*;
import java.util.*;
import java.math.*;

public class StatusResponse extends CanFail implements IResponse
{
	/** 
		 InitResponse constructor.
	 
	 @param response Response data sent from Paynow
	 @exception InvalidIntegrationException If the error returned from paynow is
	*/
	public StatusResponse(Map<String, String> response)
	{
		Data = response;

		Load();
	}

	private Map<String, String> Data;
	protected final Map<String, String> getData()
	{
		return Data;
	}

	private boolean WasSuccessful;
	protected final boolean getWasSuccessful()
	{
		return WasSuccessful;
	}
	protected final void setWasSuccessful(boolean value)
	{
		WasSuccessful = value;
	}

	private String Reference;
	public final String getReference()
	{
		return Reference;
	}
	public final void setReference(String value)
	{
		Reference = value;
	}

	private BigDecimal Amount = new BigDecimal(0);
	public final BigDecimal getAmount()
	{
		return Amount;
	}
	public final void setAmount(BigDecimal value)
	{
		Amount = value;
	}

	private boolean WasPaid;
	public final boolean getWasPaid()
	{
		return WasPaid;
	}
	public final void setWasPaid(boolean value)
	{
		WasPaid = value;
	}


	/** 
		 Gets a boolean indicating whether a request succeeded or failed
	 
	 @return 
	*/
	public final boolean Success()
	{
		return getWasSuccessful();
	}

	/** 
		 Reads through the response data sent from Paynow
	*/
	private void Load()
	{
		if (!getData().containsKey("error"))
		{
			setWasSuccessful(true);
		}

		if (getData().containsKey("status"))
		{
			setWasPaid(getData().get("status").toLowerCase().equals(Constants.ResponsePaid));
		}

		if (getData().containsKey("amount"))
		{
			setAmount((BigDecimal)getData().get("amount"));
		}

		if (getData().containsKey("reference"))
		{
			setReference(getData().get("reference"));
		}

		if (getWasSuccessful())
		{
			return;
		}

		if (getData().containsKey("error"))
		{
			Fail(getData().get("error"));
		}
	}

	/** 
		 Returns the poll URL sent from Paynow
	 
	 @return 
	*/
	public final String PollUrl()
	{
		return getData().containsKey("pollurl") ? getData().get("pollurl") : "";
	}

	public final boolean Paid()
	{
		return getWasPaid();
	}


	/** 
		 Get the original data sent from Paynow
	 
	 @return 
	*/
	public final Map<String, String> GetData()
	{
		return getData();
	}
}