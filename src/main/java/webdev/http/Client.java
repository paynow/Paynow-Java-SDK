package webdev.http;

import java.util.*;

public class Client
{
	private HttpClient _client;

	public Client()
	{
		// TODO: Implement
		_client = new HttpClient();
	}

	/** 
		 /
	 
	 @param url
	 @param data
	 @return 
	*/

	public final String PostAsync(String url)
	{
		return PostAsync(url, null);
	}


	public final String PostAsync(String url, HashMap<String, String> data)
	{
		HashMap<String, String> content = new FormUrlEncodedContent((data != null) ? data : new HashMap<String, String>());

		TResult response = _client.PostAsync(url, content).Result;

		return response.Content.ReadAsStringAsync().Result;
	}
}