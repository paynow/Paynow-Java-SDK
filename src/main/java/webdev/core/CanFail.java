package webdev.core;

import webdev.exceptions.*;
import java.util.*;

public abstract class CanFail
{
	/**
		The list of errors
	 */
	private final ArrayList<String> _errors = new ArrayList<String>();

	/** 
		Throws an exception for critical errors and stores other non-critical errors
		
		@param error
		@exception InvalidIntegrationException
	*/
	public final void Fail(String error)
	{
		switch (error)
		{
			case Constants.ResponseInvalidId:
				throw new InvalidIntegrationException();
			default:
				_errors.add(error);
				break;
		}
	}

	/** 
		Get the errors sent by Paynow
	 
		@return 
	*/

	public final String Errors()
	{
		return Errors(',');
	}

	/**
		Get the errors sent by payment

		@param separator
		
		@return The errors from paynow
	 */
	public final String Errors(char separator)
	{
		StringBuilder sb = new StringBuilder();
		for (String s : _errors)
		{
			sb.append(s);
			sb.append(separator);
		}

		return sb.toString();
	}
}