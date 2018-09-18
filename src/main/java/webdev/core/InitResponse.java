package webdev.core;

import webdev.exceptions.*;
import java.util.*;


/**
	This class is a wrapper around the response sent from Paynow
	when initiating a transaction
 */
public class InitResponse extends CanFail
{
	/** 
		 InitResponse constructor.
	 
	 @param response Response data sent from Paynow
	 @exception InvalidIntegrationException If the error returned from paynow is
	*/
	public InitResponse(Map<String, String> response)
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

	private boolean HasRedirect;
	protected final boolean getHasRedirect()
	{
		return HasRedirect;
	}
	protected final void setHasRedirect(boolean value)
	{
		HasRedirect = value;
	}

	/** 
		 Reads through the response data sent from Paynow
	*/
	private void Load()
	{
		if (getData().containsKey("status"))
		{
			setWasSuccessful(getData().get("status").toLowerCase().equals(Constants.ResponseOk));
		}

		if (getData().containsKey("browserurl"))
		{
			setHasRedirect(true);
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


	/** 
		 Gets a boolean indicating whether a request succeeded or failed
	 
	 	@return 
	*/
	public final boolean Success()
	{
		return getWasSuccessful();
	}

	/** 
		 Returns the url the user should be taken to so they can make a payment
	 
	 @return 
	*/
	public final String RedirectLink()
	{
		return getHasRedirect() ? getData().get("browserurl") : "";
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