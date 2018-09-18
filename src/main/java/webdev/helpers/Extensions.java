package webdev.helpers;

import webdev.core.Constants;
import webdev.payments.MobileMoneyMethod;

import java.math.*;
import java.util.*;


public final class Extensions
{
//	public static Map<String, String> ToDictionary(NameValueCollection col)
//	{
//		Map<String, String> dict = new HashMap<String, String>();
//		for (String k : col.AllKeys)
//		{
//			dict.put(k, col.get(k));
//		}
//
//		return dict;
//	}

	public static String FlattenCollection(HashMap<String, BigDecimal> items)
	{
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<String, BigDecimal> pair: items.entrySet()) {
			sb.append(pair.getKey());
			sb.append( ", ");
		}

		return sb.toString();
	}

	public static BigDecimal AddCollectionValues(HashMap<String, BigDecimal> items)
	{
		BigDecimal number = BigDecimal.ZERO;

		for (Map.Entry<String, BigDecimal> pair: items.entrySet()) {
			number = number.add(pair.getValue());
		}

		return number;
	}

	public static HashMap<String, String> ParseQueryString(String qs)
    {
        // TODO: Implement

        return new HashMap<String, String>();
    }

	public static String GetString(MobileMoneyMethod method)
	{
		switch (method)
		{
			case Ecocash:
				return Constants.MobileMoneyMethodEcocash;
			default:
				throw new IndexOutOfBoundsException();
		}
	}
}