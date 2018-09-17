package webdev.core;

import webdev.exceptions.*;
import java.util.*;

public abstract class CanFail
{
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
	 
	 @param separator
	 @return 
	*/

	public final String Errors()
	{
		return Errors(',');
	}


	public final String Errors(char separator)
	{
		return _errors.Aggregate("", (accumulator, value) -> accumulator += String.format("%1$s%2$s ", value, separator)).trim();
	}
}