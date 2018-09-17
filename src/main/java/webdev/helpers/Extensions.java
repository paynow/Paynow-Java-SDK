package Webdev.Helpers;

import webdev.core.*;
import webdev.payments.*;
import java.util.*;


public final class Extensions
{
	public static Map<String, String> ToDictionary(NameValueCollection col)
	{
		Map<String, String> dict = new HashMap<String, String>();
		for (String k : col.AllKeys)
		{
			dict.put(k, col.get(k));
		}

		return dict;
	}

	public static String GetString(MobileMoneyMethod method)
	{
		switch (method)
		{
			case Ecocash:
				return Constants.MobileMoneyMethodEcocash;
				break;
			default:
				throw new IndexOutOfBoundsException("method", method, null);
		}
	}
}