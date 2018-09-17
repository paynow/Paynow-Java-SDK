package webdev.helpers;

import java.util.*;

public final class Hash
{
	/** 
		 Hash the values in the given dictonary
	 
	 @param values Values to value
	 @param integrationKey Paynow integration key
	 @return 
	*/
	public static String Make(Map<String, String> values, UUID integrationKey)
	{
		var concat = values.Aggregate("", (accumulator, pair) -> accumulator += !pair.Key.toLowerCase().equals("hash") ? pair.Value : "");

		concat += integrationKey.toString();


		byte[] hash = new byte[0];
		try (System.Security.Cryptography.SHA512 sha = SHA512.Create())
		{
			hash = sha.ComputeHash(Encoding.UTF8.GetBytes(concat));
		}

		return GetStringFromHash(hash);
	}

	private static String GetStringFromHash(byte[] hash)
	{
		StringBuilder result = new StringBuilder();

		for (byte t : hash)
		{
			result.append(String.format("%02X", t));
		}
		return result.toString();
	}

	public static boolean Verify(Map<String, String> data, UUID integrationKey)
	{
		return Make(data, integrationKey).equals(data.get("hash"));
	}
}